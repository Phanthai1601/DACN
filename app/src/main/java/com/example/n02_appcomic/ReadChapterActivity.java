package com.example.n02_appcomic;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.adapter.ChapterImageAdapter;
import com.example.n02_appcomic.bottom_sheet.ChapterBottomSheet;
import com.example.n02_appcomic.model.ChapterImage;
import com.example.n02_appcomic.model.ServerData;
import com.example.n02_appcomic.model.responsive.ChapterContentResponse;
import com.example.n02_appcomic.viewmodel.ComicViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReadChapterActivity extends AppCompatActivity {

    private ComicViewModel comicViewModel;
    private ChapterImageAdapter chapterImageAdapter;
    private RecyclerView recyclerView;

    private ArrayList<ServerData> chapterList = new ArrayList<>();
    private int currentIndex = 0;
    private String slug;

    private AppCompatTextView tvNameComic, txtCurrentChapter;
    private ImageView imvBack, btnPrev, btnNext;

    private Observer<ChapterContentResponse> chapterObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_chapter);

        // ===== Intent =====
        slug = getIntent().getStringExtra("slug");
        currentIndex = getIntent().getIntExtra("current_index", 0);
        String comicName = getIntent().getStringExtra("comic_name");

        // ===== View =====
        recyclerView = findViewById(R.id.rcvImageChapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        tvNameComic = findViewById(R.id.name_comic_txt);
        txtCurrentChapter = findViewById(R.id.txtCurrentChapter);
        imvBack = findViewById(R.id.imgBack);
        btnPrev = findViewById(R.id.btnPreviousChapter);
        btnNext = findViewById(R.id.btnNextChapter);

        tvNameComic.setText(comicName);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.activity_reading_chapter),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }
        );

        comicViewModel = new ViewModelProvider(this).get(ComicViewModel.class);

        initObserver();
        initAction();

        loadChapterList();
    }

    // ================= LOAD CHAPTER LIST =================
    private void loadChapterList() {
        comicViewModel.getComicDetail(slug).observe(this, item -> {
            if (item == null || item.getChapters() == null) return;

            chapterList.clear();
            chapterList.addAll(
                    item.getChapters().get(0).getServer_data()
            );

            loadChapter();
        });
    }

    // ================= OBSERVER =================
    private void initObserver() {
        chapterObserver = response -> {
            if (response == null || response.getData() == null) return;

            List<ChapterImage> images =
                    response.getData().getItem().getChapter_image();

            chapterImageAdapter = new ChapterImageAdapter(
                    this,
                    images,
                    response.getData().getDomain_cdn(),
                    response.getData().getItem().getChapter_path()
            );

            recyclerView.setAdapter(chapterImageAdapter);
            recyclerView.scrollToPosition(0);

            updateNavigationUI();
        };
    }

    // ================= ACTION =================
    private void initAction() {

        imvBack.setOnClickListener(v -> finish());

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                loadChapter();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < chapterList.size() - 1) {
                currentIndex++;
                loadChapter();
            }
        });

        txtCurrentChapter.setOnClickListener(v -> {
            ChapterBottomSheet bottomSheet = new ChapterBottomSheet(
                    chapterList,
                    currentIndex,
                    chapter -> {
                        currentIndex = chapterList.indexOf(chapter);
                        loadChapter();
                    }
            );
            bottomSheet.show(getSupportFragmentManager(), "ChapterBottomSheet");
        });
    }

    // ================= LOAD CHAPTER =================
    private void loadChapter() {
        if (chapterList.isEmpty()) return;

        String apiUrl = chapterList.get(currentIndex).getChapterAPIData();
        comicViewModel.getChapterImages(apiUrl)
                .observe(this, chapterObserver);
    }

    // ================= UI =================
    private void updateNavigationUI() {

        txtCurrentChapter.setText(
                "Chap " + (currentIndex + 1) + " / " + chapterList.size()
        );

        btnPrev.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < chapterList.size() - 1);

        btnPrev.setAlpha(btnPrev.isEnabled() ? 1f : 0.3f);
        btnNext.setAlpha(btnNext.isEnabled() ? 1f : 0.3f);
    }
}
