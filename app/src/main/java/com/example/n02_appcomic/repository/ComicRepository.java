package com.example.n02_appcomic.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.n02_appcomic.model.Item;
import com.example.n02_appcomic.model.responsive.ApiResponsive;
import com.example.n02_appcomic.model.responsive.ChapterContentResponse;
import com.example.n02_appcomic.model.responsive.ComicDetailResponse;
import com.example.n02_appcomic.model.responsive.SearchResponse;
import com.example.n02_appcomic.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComicRepository {
    private static final String TAG = "ComicRepository";

    public LiveData<List<Item>> getComicList(int page, String type) {
        MutableLiveData<List<Item>> data = new MutableLiveData<>();

        RetrofitClient.getApiService().getComicList(type, page).enqueue(new Callback<ApiResponsive>() {
            @Override
            public void onResponse(Call<ApiResponsive> call, Response<ApiResponsive> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(List.of(response.body().getData().getItems()));
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponsive> call, Throwable t) {
                Log.e("ComicRepository", "API Call failed: " + t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }
    public LiveData<Item> getComicDetail(String slug) {
        MutableLiveData<Item> data = new MutableLiveData<>();

        RetrofitClient.getApiService().getComicDetail(slug).enqueue(new Callback<ComicDetailResponse>() {
            @Override
            public void onResponse(Call<ComicDetailResponse> call, Response<ComicDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getData().getItem());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ComicDetailResponse> call, Throwable t) {
                Log.e("ComicRepository", "API Call failed: " + t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }
    public LiveData<ChapterContentResponse> getChapterImages(String apiUrl) {
        MutableLiveData<ChapterContentResponse> data = new MutableLiveData<>();

        RetrofitClient.getApiService().getChapterImages(apiUrl).enqueue(new Callback<ChapterContentResponse>() {
            @Override
            public void onResponse(Call<ChapterContentResponse> call, Response<ChapterContentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChapterContentResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
    public LiveData<List<Item>> getSearchResults(String keywword) {
        MutableLiveData<List<Item>> data = new MutableLiveData<>();

        RetrofitClient.getApiService().searchComic(keywword).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(List.of(response.body().getData().getItems()));
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }


}
