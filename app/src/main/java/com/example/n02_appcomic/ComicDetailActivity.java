package com.example.n02_appcomic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.n02_appcomic.adapter.ChapterAdapter;
import com.example.n02_appcomic.database.DatabaseHelper;
import com.example.n02_appcomic.model.Item;
import com.example.n02_appcomic.model.ServerData;
import com.example.n02_appcomic.utils.SessionManager;
import com.example.n02_appcomic.viewmodel.ComicViewModel;

import java.util.ArrayList;
import java.util.List;

public class ComicDetailActivity extends AppCompatActivity {
    TextView tvComicName;
    private ComicViewModel comicViewModel;
    private ChapterAdapter chapterAdapter;
    private ImageView imv, imgBack, imgFavorite;
    private Button btnReadFirstChapter, btnReadLastChapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

        RecyclerView recyclerView = findViewById(R.id.rvChapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvComicName = findViewById(R.id.tvName);
        imv = findViewById(R.id.imv);
        imgBack = findViewById(R.id.imgBack);
        imgFavorite = findViewById(R.id.imgFavorite);
        btnReadFirstChapter = findViewById(R.id.btnReadFirstChapter);
        btnReadLastChapter = findViewById(R.id.btnReadLastChapter);


        DatabaseHelper db = new DatabaseHelper(this);
        SessionManager session = new SessionManager(this);
        int userId = session.getUserId();

        String slug = getIntent().getStringExtra("slug");
        comicViewModel = new ViewModelProvider(this).get(ComicViewModel.class);

        comicViewModel.getComicDetail(slug).observe(this, item -> {
            if (item != null) {
                if (item.getChapters() != null && !item.getChapters().isEmpty()) {
                    List<ServerData> chapterList = item.getChapters().get(0).getServer_data();

                    // Set icon trái tim ban đầu
                    if (db.isFavorite(String.valueOf(userId), item.getID())) {
                        imgFavorite.setImageResource(R.drawable.ic_favorite);
                    } else {
                        imgFavorite.setImageResource(R.drawable.ic_favorite_border);
                    }
                    // Gắn sự kiện click yêu thích
                    imgFavorite.setOnClickListener(v -> toggleFavorite(imgFavorite, item));

                    /// ////
                    btnReadFirstChapter.setOnClickListener(v -> {
                        ArrayList<String> apiList = new ArrayList<>();
                        for (ServerData chapter : chapterList) {
                            apiList.add(chapter.getChapterAPIData());
                        }
                        Intent intent = new Intent(ComicDetailActivity.this, ReadChapterActivity.class);
                        intent.putStringArrayListExtra("chapter_api_list", apiList);
                        intent.putExtra("current_index", 0); // Chap đầu
                        startActivity(intent);
                    });

                    btnReadLastChapter.setOnClickListener(v -> {
                        ArrayList<String> apiList = new ArrayList<>();
                        for (ServerData chapter : chapterList) {
                            apiList.add(chapter.getChapterAPIData());
                        }
                        Intent intent = new Intent(ComicDetailActivity.this, ReadChapterActivity.class);
                        intent.putStringArrayListExtra("chapter_api_list", apiList);
                        intent.putExtra("current_index", chapterList.size() - 1); // Chap cuối
                        startActivity(intent);
                    });

                    // Khi click vào chap trong RecyclerView
                    chapterAdapter = new ChapterAdapter(chapterList, chapter -> {
                        ArrayList<String> apiList = new ArrayList<>();
                        for (ServerData c : chapterList) {
                            apiList.add(c.getChapterAPIData());
                        }
                        int index = chapterList.indexOf(chapter);

                        Intent intent = new Intent(ComicDetailActivity.this, ReadChapterActivity.class);
                        intent.putStringArrayListExtra("chapter_api_list", apiList);
                        intent.putExtra("current_index", index);
                        startActivity(intent);
                    });

                    tvComicName.setText(item.getName());
                    String imageUrl = "https://img.otruyenapi.com/uploads/comics/" + item.getThumbURL();
                    Glide.with(this).load(imageUrl).into(imv);

                    recyclerView.setAdapter(chapterAdapter);

                    Log.d("ITEM",item.getSlug() + item.getID() + " " + item.getCategory() + " " + item.getContent());
                }
            }
        });

        //imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());

    }

    private void toggleFavorite(ImageView imgFavorite, Item item) {
        DatabaseHelper db = new DatabaseHelper(this);
        SessionManager session = new SessionManager(this);
        int userId = session.getUserId();

        // Kiểm tra trạng thái hiện tại
        if (db.isFavorite(String.valueOf(userId), item.getID())) {
            // Nếu đã yêu thích -> Xóa
            db.removeFavorite(userId, item.getID());
            imgFavorite.setImageResource(R.drawable.ic_favorite_border);
            Toast.makeText(this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu chưa yêu thích -> Thêm
            String title = item.getName();
            String thumbnail = item.getThumbURL();
            String author = item.getAuthor() != null && !item.getAuthor().isEmpty() ? item.getAuthor().get(0) : "";
            String lastChapter = "";
            if (item.getChaptersLatests() != null && !item.getChaptersLatests().isEmpty()) {
                lastChapter = item.getChaptersLatests().get(0).getChapterName();
            }

            db.addFavorite(userId, item.getID(), item.getSlug(), title, thumbnail, author, lastChapter);
            imgFavorite.setImageResource(R.drawable.ic_favorite);
            db.logFavorites();
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        }
    }


}
