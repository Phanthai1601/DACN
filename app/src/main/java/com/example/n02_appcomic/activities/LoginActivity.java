package com.example.n02_appcomic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.n02_appcomic.FragmentHome;
import com.example.n02_appcomic.MainActivity;
import com.example.n02_appcomic.R;
import com.example.n02_appcomic.database.DatabaseHelper;
import com.example.n02_appcomic.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView tv_other_signin;
    Button btnLogin;
    DatabaseHelper dbHelper;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        // Nếu đã đăng nhập rồi → bỏ qua màn đăng nhập
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Không cho quay lại Login bằng nút back
            return;
        }
        setContentView(R.layout.activity_login);

        // Ánh xạ view
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_pass_login);
        tv_other_signin = findViewById(R.id.tvOtherOptions);
        btnLogin = findViewById(R.id.btn_login);

        // Khởi tạo SQLite
        dbHelper = new DatabaseHelper(this);
        dbHelper.registerUser("Admin", "admin@gmail.com", "123456");

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);

        // Sự kiện nút đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isValid = dbHelper.loginUser(email, password);
            if (isValid) {
                int userId = dbHelper.getUserIdByEmail(email);
                sessionManager.createLoginSession(userId);
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
        //sign up
        tv_other_signin.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });
    }
}

