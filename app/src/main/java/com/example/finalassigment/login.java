package com.example.finalassigment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class login extends AppCompatActivity {
    EditText edt_username, edt_password;
    Button btn_gg, btn_phone, btn_fb, btn_login;
    TextView tv_others;
    MyDatabaseHelper dbHelper;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Buộc logout Facebook session cũ ngay khi mở Activity
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        LoginManager.getInstance().logOut();

        EdgeToEdge.enable(this);
        setContentView(R.layout.login_form);

        // 2. Init CallbackManager và ép dùng WEB_ONLY flow
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);

        // 3. Khởi tạo FirebaseAuth & UI component
        mAuth = FirebaseAuth.getInstance();
        btn_phone = findViewById(R.id.btnPhoneNum);
        btn_gg    = findViewById(R.id.btnGoogle);
        btn_fb    = findViewById(R.id.btnFacebook);
        btn_login = findViewById(R.id.btn_login);
        tv_others = findViewById(R.id.tvOtherOptions);
        edt_username = findViewById(R.id.edt_username_login);
        edt_password = findViewById(R.id.edt_pass_login);
        dbHelper = new MyDatabaseHelper(this);

        // 4. Google Sign-In setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Listeners
        btn_gg.setOnClickListener(v -> signInWithGoogle());
        btn_phone.setOnClickListener(v -> {
            startActivity(new Intent(this, PhoneLoginActivity.class));
            finish();
        });
        tv_others.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUp.class));
            finish();
        });

        btn_login.setOnClickListener(v -> {
            String user = edt_username.getText().toString().trim();
            String pass = edt_password.getText().toString().trim();
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkLogin(user, pass)) {
                startActivity(new Intent(this, HomePage.class));
                finish();
            } else {
                Toast.makeText(this, "Tài khoản không tồn tại hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
            }
        });

        // Facebook login
        btn_fb.setOnClickListener(v -> {
            LoginManager.getInstance()
                    .logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult result) {
                        handleFacebookAccessToken(result.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(login.this, "Hủy đăng nhập Facebook", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(login.this, "Lỗi Facebook: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean checkLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username=? AND password=?",
                new String[]{username, password}
        );
        boolean ok = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return ok;
    }

    private void signInWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(t -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential cred = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(cred).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(login.this, HomePage.class));
                finish();
            } else {
                Toast.makeText(login.this,
                        "Đăng nhập Facebook thất bại: " + task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 1. Xử lý callback Facebook trước
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        // 2. Super
        super.onActivityResult(requestCode, resultCode, data);
        // 3. Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                if (acc != null) {
                    startActivity(new Intent(this, HomePage.class));
                    finish();
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
