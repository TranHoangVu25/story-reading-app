package com.example.finalassigment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.*;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private EditText edtPhone, edtOtp;
    private Button btnSendOtp, btnVerify;

    private FirebaseAuth auth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        // 1. Khởi tạo FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // 2. Ánh xạ view
        edtPhone   = findViewById(R.id.edt_phone);
        btnSendOtp = findViewById(R.id.btn_send_otp_p);
        edtOtp     = findViewById(R.id.edt_otp);
        btnVerify  = findViewById(R.id.btn_verify);

        // 3. Bắt sự kiện
        btnSendOtp.setOnClickListener(v -> sendVerificationCode());
        btnVerify.setOnClickListener(v -> verifyCode());
    }

    // Chuyển số điện thoại nhập vào thành chuẩn E.164 (ví dụ +84 cho Việt Nam)
    private String formatE164(String rawNumber) {
        String digits = rawNumber.replaceAll("[^0-9]", "");
        if (digits.startsWith("0")) {
            digits = digits.substring(1);
        }
        return "+84" + digits;
    }

    private void sendVerificationCode() {
        String rawPhone = edtPhone.getText().toString().trim();
        if (rawPhone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneE164 = formatE164(rawPhone);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneE164)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // Tự động hoàn tất (SMS auto-retrieval)
                    String code = credential.getSmsCode();
                    if (code != null) {
                        edtOtp.setText(code);
                        verifyWithCredential(credential);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(PhoneLoginActivity.this,
                            "Gửi OTP thất bại: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(String verifId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(verifId, token);
                    verificationId = verifId;
                    Toast.makeText(PhoneLoginActivity.this,
                            "OTP đã được gửi", Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode() {
        String code = edtOtp.getText().toString().trim();
        if (code.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, code);
        verifyWithCredential(credential);
    }

    private void verifyWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công
                        startActivity(new Intent(this, HomePage.class));
                        finish();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this,
                                "Đăng nhập thất bại: " +
                                        task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
