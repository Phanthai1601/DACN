package com.example.n02_appcomic.model.responsive;

import com.example.n02_appcomic.model.ChapterContentData;

public class ChapterContentResponse {
    private String status;
    private String message;
    private ChapterContentData data;

    // Getter
    public ChapterContentData getData() {
        return data;
    }
}
