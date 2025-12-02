package com.example.n02_appcomic.activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.n02_appcomic.R;
import com.example.n02_appcomic.database.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {

    private TextView tv_back;
    private EditText edt_Email, edt_password, edt_re_pass, edt_username,edt_otp;
    private Button btn_signup,btn_otp;
    private DatabaseHelper dbHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // 1. Khởi tạo helper
        dbHelper = new DatabaseHelper(this);

        // 2. Ánh xạ view
        tv_back      = findViewById(R.id.tvBack);
        edt_Email    = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_pass);
        edt_re_pass  = findViewById(R.id.edt_re_pass);
        edt_username = findViewById(R.id.edt_username);
        edt_otp = findViewById(R.id.edt_otp);
        btn_signup   = findViewById(R.id.btn_signup);
        btn_otp = findViewById(R.id.btn_otp);
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
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
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
    }

    private void handleSignUp() {
        String email    = edt_Email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String re_pass  = edt_re_pass.getText().toString().trim();
        String username = edt_username.getText().toString().trim();
        // Validate
        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || re_pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!isValidEmail(email)){
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
            Toast.makeText(this, "User name tối đa 20 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!edt_otp.getText().toString().equals("123456")) {
            Toast.makeText(this, "Mã OTP không đúng.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Insert vào bảng "users"
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("name", username);

        long newId = db.insert("users", null, values);
        db.close();

        if (newId != -1) {
            Toast.makeText(this, "Đăng kí thành công! ID = " + newId, Toast.LENGTH_SHORT).show();
            // Sau khi đăng ký xong, chuyển về màn Login
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Đăng kí thất bại, Email đã tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    // phương thức kiểm tra email có hợp lệ hay không
    public boolean isValidEmail(CharSequence email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // kiểm tra username < 20 ký tự
    private boolean isValidUsername(String username) {
        return username.length() <= 20;
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
}
