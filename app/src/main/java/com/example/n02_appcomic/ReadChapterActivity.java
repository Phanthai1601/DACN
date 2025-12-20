package com.example.n02_appcomic;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.adapter.ChapterImageAdapter;
import com.example.n02_appcomic.model.ChapterImage;
import com.example.n02_appcomic.model.responsive.ChapterContentResponse;
import com.example.n02_appcomic.viewmodel.ComicViewModel;

import java.util.List;

//public class ReadChapterActivity extends AppCompatActivity {
//    private ComicViewModel comicViewModel;
//    private ChapterImageAdapter chapterImageAdapter;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reading_chapter);
//        RecyclerView recyclerView = findViewById(R.id.rcvImageChapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        comicViewModel = new ViewModelProvider(this).get(ComicViewModel.class);
//        String chapterApiUrl = getIntent().getStringExtra("chapter_api_url");
//        comicViewModel.getChapterImages(chapterApiUrl).observe(this, response -> {
//            List<ChapterImage> chapterImages = response.getData().getItem().getChapter_image();
//            chapterImageAdapter = new ChapterImageAdapter(chapterImages, response.getData().getDomain_cdn(), response.getData().getItem().getChapter_path());
//            recyclerView.setAdapter(chapterImageAdapter);
//        });
//
//
//    }
//}
public class ReadChapterActivity extends AppCompatActivity {
    private ComicViewModel comicViewModel;
    private ChapterImageAdapter chapterImageAdapter;
    private RecyclerView recyclerView;

    private List<String> chapterApiList;
    private int currentIndex = 0;
    private Observer<ChapterContentResponse> chapterObserver;
    private AppCompatTextView tvNameComic;
    private ImageView imvBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_chapter);
        recyclerView = findViewById(R.id.rcvImageChapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        imvBack = findViewById(R.id.imgBack);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_reading_chapter), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        comicViewModel = new ViewModelProvider(this).get(ComicViewModel.class);

        chapterApiList = getIntent().getStringArrayListExtra("chapter_api_list");
        currentIndex = getIntent().getIntExtra("current_index", 0);
        String comicName = getIntent().getStringExtra("comic_name");
        tvNameComic = findViewById(R.id.name_comic_txt);
        tvNameComic.setText(comicName);

        Button btnPrev = findViewById(R.id.btnPreviousChapter);
        Button btnNext = findViewById(R.id.btnNextChapter);
        initAction();

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                loadChapter();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < chapterApiList.size() - 1) {
                currentIndex++;
                loadChapter();
            }
        });

        // Observer chỉ tạo 1 lần
        chapterObserver = response -> {
            if (response != null && response.getData() != null) {
                /// ///
                String chapterName = response.getData().getItem().getChapter_name();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Chap " + chapterName);
                }
                /// ///
                List<ChapterImage> chapterImages = response.getData().getItem().getChapter_image();
                chapterImageAdapter = new ChapterImageAdapter(
                        this,
                        chapterImages,
                        response.getData().getDomain_cdn(),
                        response.getData().getItem().getChapter_path()
                );
                recyclerView.setAdapter(chapterImageAdapter);
                recyclerView.scrollToPosition(0);
            }
        };

        loadChapter(); // load chap đầu tiên
    }

    private void initAction(){
        imvBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadChapter() {
        if (chapterApiList == null || chapterApiList.isEmpty()) return;
        // Cập nhật trạng thái nút
        Button btnPrev = findViewById(R.id.btnPreviousChapter);
        Button btnNext = findViewById(R.id.btnNextChapter);

        // Trạng thái nút Prev
        if (currentIndex > 0) {
            btnPrev.setEnabled(true);
            btnPrev.setBackgroundColor(getResources().getColor(R.color.purple_500)); // màu active
            btnPrev.setTextColor(Color.WHITE);
        } else {
            btnPrev.setEnabled(false);
            btnPrev.setBackgroundColor(Color.LTGRAY); // màu xám khi disable
            btnPrev.setTextColor(Color.DKGRAY);
        }

        // Trạng thái nút Next
        if (currentIndex < chapterApiList.size() - 1) {
            btnNext.setEnabled(true);
            btnNext.setBackgroundColor(getResources().getColor(R.color.purple_500));
            btnNext.setTextColor(Color.WHITE);
        } else {
            btnNext.setEnabled(false);
            btnNext.setBackgroundColor(Color.LTGRAY);
            btnNext.setTextColor(Color.DKGRAY);
        }

        String apiUrl = chapterApiList.get(currentIndex);
        comicViewModel.getChapterImages(apiUrl).observe(this, chapterObserver);
    }
}


