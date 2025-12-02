package com.example.n02_appcomic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.n02_appcomic.model.Item;
import com.example.n02_appcomic.model.responsive.ChapterContentResponse;
import com.example.n02_appcomic.repository.ComicRepository;

import java.util.List;

public class ComicViewModel extends ViewModel {
    private final ComicRepository repository = new ComicRepository();

    public LiveData<List<Item>> getComics(int page, String type) {
        return repository.getComicList(page, type);
    }
    public LiveData<Item> getComicDetail(String slug) {
        return repository.getComicDetail(slug);
    }
    public LiveData<ChapterContentResponse> getChapterImages(String apiUrl) {
        return repository.getChapterImages(apiUrl);
    }
    public  LiveData<List<Item>> getSearchResults(String keyword) {
        return repository.getSearchResults(keyword);
    }
}
