package com.example.finalassigment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {

    private TextView tv_back;
    private EditText edt_Email, edt_password, edt_re_pass, edt_username,edt_otp;
    private Button btn_signup,btn_otp;
    private MyDatabaseHelper dbHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // 1. Khởi tạo helper
        dbHelper = new MyDatabaseHelper(this);

        // 2. Ánh xạ view
        tv_back      = findViewById(R.id.tvBack);
        edt_Email    = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_pass);
        edt_re_pass  = findViewById(R.id.edt_re_pass);
        edt_username = findViewById(R.id.edt_username);
//        edt_otp = findViewById(R.id.edt_otp);
        btn_signup   = findViewById(R.id.btn_signup);
//        btn_otp = findViewById(R.id.btn_otp);
        View root    = findViewById(R.id.main);

        // 3. Thiết lập EdgeToEdge padding
        if (root != null) {
            final int padL = root.getPaddingLeft();
            final int padR = root.getPaddingRight();
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(padL, bars.top, padR, bars.bottom);
                return insets;
            });
        }

        // 4. Quay lại Login
        tv_back.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, login.class));
            finish();
        });

        // 5. Xử lý sign up
        btn_signup.setOnClickListener(v -> handleSignUp());

        // 6. icon eye trong password
        edt_password.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edt_password.getRight() - edt_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (edt_password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        // Hiện mật khẩu
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        edt_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open, 0);
                    } else {
                        // Ẩn mật khẩu
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edt_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close, 0);
                    }
                    edt_password.setSelection(edt_password.length()); // Giữ con trỏ ở cuối
                    return true;
                }
            }
            return false;
        });
        edt_re_pass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edt_re_pass.getRight() - edt_re_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (edt_re_pass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        // Hiện mật khẩu
                        edt_re_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        edt_re_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open, 0);
                    } else {
                        // Ẩn mật khẩu
                        edt_re_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edt_re_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close, 0);
                    }
                    edt_re_pass.setSelection(edt_re_pass.length()); // Giữ con trỏ ở cuối
                    return true;
                }
            }
            return false;
        });
        LinearLayout editText = findViewById(R.id.container_email);
        GradientDrawable drawable1 = new GradientDrawable();
        drawable1.setShape(GradientDrawable.RECTANGLE);
        drawable1.setCornerRadius(24f);
        drawable1.setStroke(2, Color.parseColor("#CCCCCC")); // Viền xám
        drawable1.setColor(Color.WHITE);
        editText.setBackground(drawable1);

        LinearLayout edit_pass = findViewById(R.id.container_password);
        GradientDrawable drawable_pass = new GradientDrawable();
        drawable_pass.setShape(GradientDrawable.RECTANGLE);
        drawable_pass.setCornerRadius(24f);
        drawable_pass.setStroke(2, Color.parseColor("#CCCCCC")); // Viền xám
        drawable_pass.setColor(Color.WHITE);
        edit_pass.setBackground(drawable_pass);

        LinearLayout edit_re_pass = findViewById(R.id.container_re_password);
        GradientDrawable drawable_re_pass = new GradientDrawable();
        drawable_re_pass.setShape(GradientDrawable.RECTANGLE);
        drawable_re_pass.setCornerRadius(24f);
        drawable_re_pass.setStroke(2, Color.parseColor("#CCCCCC")); // Viền xám
        drawable_re_pass.setColor(Color.WHITE);
        edit_re_pass.setBackground(drawable_pass);

        LinearLayout edit_user_name = findViewById(R.id.container_username);
        GradientDrawable drawable_user_name = new GradientDrawable();
        drawable_user_name.setShape(GradientDrawable.RECTANGLE);
        drawable_user_name.setCornerRadius(24f);
        drawable_user_name.setStroke(2, Color.parseColor("#CCCCCC")); // Viền xám
        drawable_user_name.setColor(Color.WHITE);
        edit_user_name.setBackground(drawable_pass);
    }

    private void handleSignUp() {
        String email    = edt_Email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String re_pass  = edt_re_pass.getText().toString().trim();
        String username = edt_username.getText().toString().trim();

        // 1. Validate cơ bản
        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || re_pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!isValidPassword(password)) {
            Toast.makeText(this, "Mật khẩu phải ≥8 ký tự, chứa ít nhất 1 chữ hoa và 1 ký tự đặc biệt.", Toast.LENGTH_LONG).show();
            return;
        }
        else if (!password.equals(re_pass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không đúng", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!isValidUsername(username)) {
            Toast.makeText(this, "User name tối thiểu 5 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!isValidUsername1(username)) {
            Toast.makeText(this, "User name tối đa 20 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Mở DB
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 3. Kiểm tra email
        try (var cursorEmail = db.query(
                "users",
                new String[]{"id"},
                "email = ?",
                new String[]{ email },
                null, null, null
        )) {
            if (cursorEmail.moveToFirst()) {
                Toast.makeText(this, "Email này đã được đăng ký rồi.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 4. Kiểm tra username
        try (var cursorUser = db.query(
                "users",
                new String[]{"id"},
                "username = ?",
                new String[]{ username },
                null, null, null
        )) {
            if (cursorUser.moveToFirst()) {
                Toast.makeText(this, "User name này đã tồn tại.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 5. Chuyển sang ghi (writable)
        db.close();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("username", username);

        long newId = db.insert("users", null, values);
        db.close();

        if (newId != -1) {
            Toast.makeText(this, "Sign up thành công! ID = " + newId, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUp.this, login.class));
            finish();
        } else {
            Toast.makeText(this, "Sign up thất bại", Toast.LENGTH_SHORT).show();
        }
    }


    // phương thức kiểm tra email có hợp lệ hay không
    public boolean isValidEmail(CharSequence email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // kiểm tra username < 20 ký tự
    private boolean isValidUsername(String username) {

        return  username.length()>=5;
    }
    private boolean isValidUsername1(String username) {
        return  username.length() <= 20 ;
    }
    //kiểm tra password
    // - ít nhất 8 ký tự
    // - ít nhất 1 chữ hoa
    // - ít nhất 1 ký tự đặc biệt (non-alphanumeric)
    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
            if (hasUpper && hasSpecial) return true;
        }
        return false;
    }

    // Phương thức hỗ trợ để debug: in ra tất cả users trong log
    public void getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (var cursor = db.query("users", null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                int id         = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String email   = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String username= cursor.getString(cursor.getColumnIndexOrThrow("nickname"));
                Log.d("User", "ID:" + id + " - " + email + " - " + username);
            }
        }
        db.close();
    }
}
