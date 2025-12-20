package com.example.n02_appcomic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.activities.CategoryComicActivity;
import com.example.n02_appcomic.adapter.CategoryAdapter;
import com.example.n02_appcomic.model.Category;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment {

    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private List<Category> categoryList;

    public FragmentCategory() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
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
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryList = getMockCategories();

        adapter = new CategoryAdapter(getContext(), categoryList, category -> {
            // Mở danh sách truyện theo thể loại khi click
            Intent intent = new Intent(getContext(), CategoryComicActivity.class);
            intent.putExtra("slug", category.getSlug());
            intent.putExtra("name", category.getName());
            startActivity(intent);
        });

        rvCategories.setAdapter(adapter);
        return view;
    }

    // Dữ liệu mẫu, bạn có thể gọi API để lấy danh sách thể loại thật
    private List<Category> getMockCategories() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("1", "Action", "action"));
        list.add(new Category("2", "Romance", "romance"));
        list.add(new Category("3", "Comedy", "comedy"));
        list.add(new Category("4", "Drama", "drama"));
        list.add(new Category("5", "Fantasy", "fantasy"));
        return list;
    }
}

