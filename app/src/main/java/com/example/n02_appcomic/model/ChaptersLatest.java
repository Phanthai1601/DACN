package com.example.n02_appcomic.model;

import com.google.gson.annotations.SerializedName;

public class ChaptersLatest {
    private String filename;
    @SerializedName("chapter_name")
    private String chapterName;
    private String chapterTitle;
    private String chapterAPIData;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String value) {
        this.filename = value;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String value) {
        this.chapterName = value;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String value) {
        this.chapterTitle = value;
    }

    public String getChapterAPIData() {
        return chapterAPIData;
    }

    public void setChapterAPIData(String value) {
        this.chapterAPIData = value;
    }

}
