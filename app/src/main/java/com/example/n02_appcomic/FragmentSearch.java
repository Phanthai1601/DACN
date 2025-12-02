package com.example.n02_appcomic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.adapter.SearchAdapter;
import com.example.n02_appcomic.viewmodel.ComicViewModel;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

public class FragmentSearch extends Fragment {

    private SearchBar searchBar;
    private SearchView searchView;
    private RecyclerView rcvSearchResult;
    private ComicViewModel comicViewModel;
    private SearchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false); // tạo file này nếu chưa có
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel
        comicViewModel = new ViewModelProvider(requireActivity()).get(ComicViewModel.class);

        // Gắn view
        searchBar = view.findViewById(R.id.search_bar);
        searchView = view.findViewById(R.id.search_view);
        rcvSearchResult = view.findViewById(R.id.rcvSearchResult);

        // Set adapter
        adapter = new SearchAdapter(comic -> {
            String slug = comic.getSlug();
            comicViewModel.getComicDetail(slug);
        });

        rcvSearchResult.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rcvSearchResult.setAdapter(adapter);

        handleSearch();
    }

    private void handleSearch() {
        searchBar.setOnMenuItemClickListener(item -> {
            searchView.show();
            return true;
        });

        searchView.getEditText().setOnEditorActionListener((textView, actionId, event) -> {
            String query = searchView.getText().toString().trim();
            if (!query.isEmpty()) {
                searchBar.setText(query);
                searchView.hide();
                loadResult(query);
            }
            return true;
        });

        searchView.addTransitionListener((searchView, previousState, newState) -> {
            if (newState == SearchView.TransitionState.SHOWN) {
                searchView.getEditText().requestFocus();
            }
        });
    }

    private void loadResult(String keyword) {
        comicViewModel.getSearchResults(keyword).observe(getViewLifecycleOwner(), items -> {
            if (items != null && !items.isEmpty()) {
                adapter.setComics(items);
            } else {
                adapter.setComics(new java.util.ArrayList<>());
                Toast.makeText(requireContext(), "Không tìm thấy kết quả cho: " + keyword, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
