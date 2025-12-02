package com.example.n02_appcomic.network;


import com.example.n02_appcomic.model.responsive.ApiResponsive;
import com.example.n02_appcomic.model.responsive.ChapterContentResponse;
import com.example.n02_appcomic.model.responsive.ComicDetailResponse;
import com.example.n02_appcomic.model.responsive.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    @GET("/v1/api/danh-sach/{type}")
    Call<ApiResponsive> getComicList(@Path("type") String type, @Query("page") int page);

    @GET("/v1/api/truyen-tranh/{slug}")
    Call<ComicDetailResponse> getComicDetail(@Path("slug") String slug);

    @GET
    Call<ChapterContentResponse> getChapterImages(@Url String fullUrl);

    @GET("/v1/api/tim-kiem")
    Call<SearchResponse> searchComic(@Query("keyword") String keyword);

    @GET("/v1/api/the-loai/{slug}")
    Call<ApiResponsive> getComicsByCategory(@Path("slug") String slug, @Query("page") int page);

}