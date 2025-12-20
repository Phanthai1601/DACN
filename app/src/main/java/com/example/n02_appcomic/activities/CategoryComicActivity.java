package com.example.n02_appcomic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.ComicDetailActivity;
import com.example.n02_appcomic.R;
import com.example.n02_appcomic.adapter.ComicAdapter;
import com.example.n02_appcomic.model.Item;
import com.example.n02_appcomic.model.responsive.ApiResponsive;
import com.example.n02_appcomic.network.ApiService;
import com.example.n02_appcomic.network.RetrofitClient;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryComicActivity extends AppCompatActivity {

    private RecyclerView rvCategoryComics;
    private TextView tvCategoryTitle;
    private ComicAdapter comicAdapter;
    private String slug;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_comic);

        // View
        rvCategoryComics = findViewById(R.id.rvCategoryComics);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        Toolbar toolbar = findViewById(R.id.toolbarCategoryComic);

        // Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Intent data
        Intent intent = getIntent();
        slug = intent.getStringExtra("slug");
        String name = intent.getStringExtra("name");

        if (slug == null || name == null) {
            Toast.makeText(this, "Không tìm thấy thể loại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvCategoryTitle.setText("Thể loại: " + name);
        getSupportActionBar().setTitle(name);

        // RecyclerView (Grid 3 cột)
        rvCategoryComics.setLayoutManager(new GridLayoutManager(this, 3));
        comicAdapter = new ComicAdapter();
        rvCategoryComics.setAdapter(comicAdapter);

        // 🔥 Hiện shimmer
        comicAdapter.setLoading(true);

        // Load API
        loadComicsByCategory(slug, page);
    }

    private void loadComicsByCategory(String slug, int page) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getComicsByCategory(slug, page)
                .enqueue(new Callback<ApiResponsive>() {

                    @Override
                    public void onResponse(Call<ApiResponsive> call, Response<ApiResponsive> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getData() != null) {

                            Item[] itemArray = response.body().getData().getItems();

                            if (itemArray != null && itemArray.length > 0) {
                                List<Item> items = Arrays.asList(itemArray);
                                comicAdapter.setComics(items); // 🔥 tắt shimmer
                            } else {
                                comicAdapter.setLoading(false);
                                Toast.makeText(
                                        CategoryComicActivity.this,
                                        "Không có truyện trong thể loại này",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } else {
                            comicAdapter.setLoading(false);
                            Toast.makeText(
                                    CategoryComicActivity.this,
                                    "Không tải được dữ liệu",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponsive> call, Throwable t) {
                        comicAdapter.setLoading(false);
                        Toast.makeText(
                                CategoryComicActivity.this,
                                "Lỗi mạng: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}
