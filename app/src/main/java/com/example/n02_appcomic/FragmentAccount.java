package com.example.n02_appcomic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.n02_appcomic.R;
import com.example.n02_appcomic.activities.AboutActivity;
import com.example.n02_appcomic.activities.FavoriteActivity;
import com.example.n02_appcomic.activities.LoginActivity;
import com.example.n02_appcomic.activities.SettingActivity;
import com.example.n02_appcomic.database.DatabaseHelper;
import com.example.n02_appcomic.model.User;
import com.example.n02_appcomic.utils.SessionManager;

public class FragmentAccount extends Fragment {

    private TextView tvName, tvEmail;
    private Button btnyeuthich, btncaidat, btnabout, btnphanhoi, btnLogout;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    public FragmentAccount() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Ánh xạ view
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnyeuthich = view.findViewById(R.id.btn_yeuthich);
        btncaidat = view.findViewById(R.id.btn_caidat);
        btnabout = view.findViewById(R.id.btn_about);

        sessionManager = new SessionManager(requireContext());
        dbHelper = new DatabaseHelper(requireContext());

        // Kiểm tra người dùng đã đăng nhập chưa
        if (sessionManager.isLoggedIn()) {
            int userId = sessionManager.getUserId();

            // Truy vấn thông tin người dùng từ SQLite
            User user = dbHelper.getUserById(userId);
            if (user != null) {
                tvName.setText(user.getName());    // hoặc user.getUsername()
                tvEmail.setText(user.getEmail());
            }
        }
        btnyeuthich.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FavoriteActivity.class);
            startActivity(intent);
        });
        btncaidat.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SettingActivity.class);
            startActivity(intent);
        });

        btnabout.setOnClickListener(v -> {
            // Chuyển đến màn About
            Intent intent = new Intent(requireContext(), AboutActivity.class);
            startActivity(intent);
        });


        // Xử lý nút đăng xuất
        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}

