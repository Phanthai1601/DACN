package com.example.n02_appcomic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.n02_appcomic.activities.LoginActivity;
import com.example.n02_appcomic.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private Fragment homeFragment = new FragmentHome();
    private Fragment searchFragment = new FragmentSearch();
    private Fragment categoryFragment = new FragmentCategory();
    private Fragment profileFragment = new FragmentAccount();
    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        // Add tất cả fragment, chỉ hiển thị home
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, profileFragment, "profile").hide(profileFragment)
                .add(R.id.container, categoryFragment, "category").hide(categoryFragment)
                .add(R.id.container, searchFragment, "search").hide(searchFragment)
                .add(R.id.container, homeFragment, "home")
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchFragment(homeFragment);
            } else if (itemId == R.id.nav_search) {
                switchFragment(searchFragment);
            } else if (itemId == R.id.nav_category) {
                switchFragment(categoryFragment);
            } else if (itemId == R.id.nav_profile) {
                switchFragment(profileFragment);
            }
            return true;
        });
    }

    private void switchFragment(Fragment targetFragment) {
        if (activeFragment != targetFragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment).show(targetFragment).commit();
            activeFragment = targetFragment;
        }
    }
    public void switchToHome() {
        bottomNav.setSelectedItemId(R.id.nav_home); // Tự động gọi lại switchFragment()
    }

}