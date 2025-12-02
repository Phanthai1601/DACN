package com.example.n02_appcomic.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.n02_appcomic.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about); // Gắn layout

        // Tìm ImageView và gắn sự kiện click để quay lại
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}

