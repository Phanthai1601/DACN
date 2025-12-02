package com.example.n02_appcomic;

import com.example.n02_appcomic.model.ChapterImage;

import java.util.List;

public class ChapterItem {
    private String comic_name;
    private String chapter_name;
    private String chapter_title;
    private String chapter_path;
    private List<ChapterImage> chapter_image;

    public String getComic_name() {
        return comic_name;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public String getChapter_path() {
        return chapter_path;
    }

    public List<ChapterImage> getChapter_image() {
        return chapter_image;
    }
}
