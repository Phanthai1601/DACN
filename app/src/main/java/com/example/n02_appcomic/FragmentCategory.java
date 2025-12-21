package com.example.n02_appcomic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.adapter.CategoryAdapter;
import com.example.n02_appcomic.adapter.ComicAdapter;
import com.example.n02_appcomic.model.Category;
import com.example.n02_appcomic.model.Item;
import com.example.n02_appcomic.model.responsive.ApiResponsive;
import com.example.n02_appcomic.network.ApiService;
import com.example.n02_appcomic.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCategory extends Fragment {

    private RecyclerView rvCategories, rvComics;
    private CategoryAdapter categoryAdapter;
    private ComicAdapter comicAdapter;
    private List<Category> categoryList;
    private int page = 1;


    public FragmentCategory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return WindowInsetsCompat.CONSUMED;
        });

        // Init views
        rvCategories = view.findViewById(R.id.rvCategories);
        rvComics = view.findViewById(R.id.rvComics);

        // Category - horizontal
        rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        // Comic - grid
        rvComics.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Data
        categoryList = getMockCategories();

        // Adapters
        categoryAdapter = new CategoryAdapter(
                getContext(),
                categoryList,
                category -> loadComicByCategory(category.getSlug())
        );

        comicAdapter = new ComicAdapter();
        comicAdapter.setLoading(true);

        rvCategories.setAdapter(categoryAdapter);
        rvComics.setAdapter(comicAdapter);

        // Load default category (first item)
        if (!categoryList.isEmpty()) {
            loadComicByCategory(categoryList.get(0).getSlug());
        }

        return view;
    }

    /**
     * Load comic list by category slug
     * (sau này thay bằng API thật)
     */
    private void loadComicByCategory(String slug) {
        comicAdapter.setLoading(true);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getComicsByCategory(slug, page)
                .enqueue(new Callback<ApiResponsive>() {

                    @Override
                    public void onResponse(Call<ApiResponsive> call,
                                           Response<ApiResponsive> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getData() != null) {

                            Item[] itemArray = response.body()
                                    .getData()
                                    .getItems();

                            if (itemArray != null && itemArray.length > 0) {
                                List<Item> items = Arrays.asList(itemArray);
                                comicAdapter.setComics(items); // 🔥 tắt shimmer
                            } else {
                                comicAdapter.setLoading(false);
                                Toast.makeText(
                                        getContext(),
                                        "Không có truyện trong thể loại này",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } else {
                            comicAdapter.setLoading(false);
                            Toast.makeText(
                                    getContext(),
                                    "Không tải được dữ liệu",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponsive> call, Throwable t) {
                        comicAdapter.setLoading(false);
                        Toast.makeText(
                                getContext(),
                                "Lỗi mạng: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }


    // Mock category data
    private List<Category> getMockCategories() {
        List<Category> list = new ArrayList<>();

        list.add(new Category("6508654905d5791ad671a491", "Action", "action"));
        list.add(new Category("6508654905d5791ad671a4a3", "Adult", "adult"));
        list.add(new Category("6508654905d5791ad671a4a6", "Adventure", "adventure"));
        list.add(new Category("6508654905d5791ad671a4a9", "Anime", "anime"));
        list.add(new Category("6508654905d5791ad671a4ac", "Chuyển Sinh", "chuyen-sinh"));
        list.add(new Category("6508654905d5791ad671a4af", "Comedy", "comedy"));
        list.add(new Category("6508654905d5791ad671a4b2", "Comic", "comic"));
        list.add(new Category("6508654905d5791ad671a4b5", "Cooking", "cooking"));
        list.add(new Category("6508654905d5791ad671a4b8", "Cổ Đại", "co-dai"));
        list.add(new Category("6508654905d5791ad671a4bb", "Doujinshi", "doujinshi"));
        list.add(new Category("6508654905d5791ad671a4be", "Drama", "drama"));
        list.add(new Category("6508654905d5791ad671a4c1", "Đam Mỹ", "dam-my"));
        list.add(new Category("6508654905d5791ad671a4c4", "Ecchi", "ecchi"));
        list.add(new Category("6508654905d5791ad671a4c7", "Fantasy", "fantasy"));
        list.add(new Category("6508654905d5791ad671a4ca", "Gender Bender", "gender-bender"));
        list.add(new Category("6508654905d5791ad671a4cc", "Harem", "harem"));
        list.add(new Category("6508654905d5791ad671a4ce", "Historical", "historical"));
        list.add(new Category("6508654905d5791ad671a4d0", "Horror", "horror"));
        list.add(new Category("6508654905d5791ad671a4d2", "Josei", "josei"));
        list.add(new Category("6508654905d5791ad671a4d4", "Live action", "live-action"));
        list.add(new Category("6508654905d5791ad671a4d6", "Manga", "manga"));
        list.add(new Category("6508654905d5791ad671a4d8", "Manhua", "manhua"));
        list.add(new Category("6508654905d5791ad671a4da", "Manhwa", "manhwa"));
        list.add(new Category("6508654905d5791ad671a4dc", "Martial Arts", "martial-arts"));
        list.add(new Category("6508654905d5791ad671a4de", "Mature", "mature"));
        list.add(new Category("6508654905d5791ad671a4e0", "Mecha", "mecha"));
        list.add(new Category("6508654905d5791ad671a4e2", "Mystery", "mystery"));
        list.add(new Category("6508654905d5791ad671a4e4", "Ngôn Tình", "ngon-tinh"));
        list.add(new Category("6508654905d5791ad671a4e6", "One shot", "one-shot"));
        list.add(new Category("6508654905d5791ad671a4e8", "Psychological", "psychological"));
        list.add(new Category("6508654905d5791ad671a4ea", "Romance", "romance"));
        list.add(new Category("6508654905d5791ad671a4ec", "School Life", "school-life"));
        list.add(new Category("6508654905d5791ad671a4ee", "Sci-fi", "sci-fi"));
        list.add(new Category("6508654905d5791ad671a4f0", "Seinen", "seinen"));
        list.add(new Category("6508654905d5791ad671a4f2", "Shoujo", "shoujo"));
        list.add(new Category("6508654905d5791ad671a4f4", "Shoujo Ai", "shoujo-ai"));
        list.add(new Category("6508654905d5791ad671a4f6", "Shounen", "shounen"));
        list.add(new Category("6508654905d5791ad671a4f8", "Shounen Ai", "shounen-ai"));
        list.add(new Category("6508654a05d5791ad671a4fa", "Slice of Life", "slice-of-life"));
        list.add(new Category("6508654a05d5791ad671a4fc", "Smut", "smut"));
        list.add(new Category("6508654a05d5791ad671a4fe", "Soft Yaoi", "soft-yaoi"));
        list.add(new Category("6508654a05d5791ad671a500", "Soft Yuri", "soft-yuri"));
        list.add(new Category("6508654a05d5791ad671a502", "Sports", "sports"));
        list.add(new Category("6508654a05d5791ad671a504", "Supernatural", "supernatural"));
        list.add(new Category("6508654a05d5791ad671a506", "Tạp chí truyện tranh", "tap-chi-truyen-tranh"));
        list.add(new Category("6508654a05d5791ad671a508", "Thiếu Nhi", "thieu-nhi"));
        list.add(new Category("6508654a05d5791ad671a50a", "Tragedy", "tragedy"));
        list.add(new Category("6508654a05d5791ad671a50c", "Trinh Thám", "trinh-tham"));
        list.add(new Category("6508654a05d5791ad671a50e", "Truyện scan", "truyen-scan"));
        list.add(new Category("6508654a05d5791ad671a510", "Truyện Màu", "truyen-mau"));
        list.add(new Category("6508654a05d5791ad671a512", "Việt Nam", "viet-nam"));
        list.add(new Category("6508654a05d5791ad671a514", "Webtoon", "webtoon"));
        list.add(new Category("6508654a05d5791ad671a516", "Xuyên Không", "xuyen-khong"));
        list.add(new Category("6508654a05d5791ad671a518", "16+", "16"));

        return list;
    }

}
