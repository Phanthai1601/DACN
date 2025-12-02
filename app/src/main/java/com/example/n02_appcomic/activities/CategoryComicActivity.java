package com.example.n02_appcomic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.ComicDetailActivity;
import com.example.n02_appcomic.R;
import com.example.n02_appcomic.adapter.ComicAdapter;
import com.example.n02_appcomic.model.Item;
import com.example.n02_appcomic.model.responsive.ApiResponsive;
import com.example.n02_appcomic.network.ApiService;
import com.example.n02_appcomic.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryComicActivity extends AppCompatActivity {

    private RecyclerView rvCategoryComics;
    private TextView tvCategoryTitle;
    private ComicAdapter comicAdapter;
    private List<Item> comicList = new ArrayList<>();
    private String slug;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_comic);

        // Ánh xạ view
        rvCategoryComics = findViewById(R.id.rvCategoryComics);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        Toolbar toolbar = findViewById(R.id.toolbarCategoryComic);

        // Thiết lập Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        slug = intent.getStringExtra("slug");
        String name = intent.getStringExtra("name");

        if (slug == null || name == null) {
            Toast.makeText(this, "Không tìm thấy thể loại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Đặt tiêu đề và Toolbar title
        tvCategoryTitle.setText("Thể loại: " + name);
        getSupportActionBar().setTitle(name);

        // Thiết lập RecyclerView dạng lưới 3 cột
        rvCategoryComics.setLayoutManager(new GridLayoutManager(this, 3));
        comicAdapter = new ComicAdapter(comic -> {
            Intent detailIntent = new Intent(CategoryComicActivity.this, ComicDetailActivity.class);
            detailIntent.putExtra("slug", comic.getSlug());
            startActivity(detailIntent);
        });
        rvCategoryComics.setAdapter(comicAdapter);

        // Gọi API
        loadComicsByCategory(slug, page);
    }

    private void loadComicsByCategory(String slug, int page) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<ApiResponsive> call = apiService.getComicsByCategory(slug, page);
        call.enqueue(new Callback<ApiResponsive>() {
            @Override
            public void onResponse(Call<ApiResponsive> call, Response<ApiResponsive> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Item[] itemArray = response.body().getData().getItems();
                    List<Item> items = Arrays.asList(itemArray);
                    comicAdapter.setComics(items);
                } else {
                    Toast.makeText(CategoryComicActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponsive> call, Throwable t) {
                Toast.makeText(CategoryComicActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

