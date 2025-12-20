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

import com.example.n02_appcomic.adapter.ComicAdapter;
import com.example.n02_appcomic.model.ComicType;
import com.example.n02_appcomic.viewmodel.ComicViewModel;

public class FragmentHome extends Fragment {

    private ComicViewModel comicViewModel;

    private RecyclerView rvComics, rcvComicFull, rcvRelease, rcvCommingSoon;
    private ComicAdapter adapterNew, adapterFull, adapterRelease, adapterCommingSoon;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        comicViewModel = new ViewModelProvider(this).get(ComicViewModel.class);

        setControl(view);

        // 🔹 Init adapters
        adapterNew = new ComicAdapter();
        adapterFull = new ComicAdapter();
        adapterRelease = new ComicAdapter();
        adapterCommingSoon = new ComicAdapter();

        // 🔹 Setup RecyclerViews
        setupRecyclerView(rvComics, adapterNew);
        setupRecyclerView(rcvComicFull, adapterFull);
        setupRecyclerView(rcvRelease, adapterRelease);
        setupRecyclerView(rcvCommingSoon, adapterCommingSoon);

        // 🔹 Show shimmer before loading
        adapterNew.setLoading(true);
        adapterFull.setLoading(true);
        adapterRelease.setLoading(true);
        adapterCommingSoon.setLoading(true);

        // 🔹 Load data
        loadComics(ComicType.NEW, adapterNew);
        loadComics(ComicType.COMPLETED, adapterFull);
        loadComics(ComicType.ONGOING, adapterRelease);
        loadComics(ComicType.COMING_SOON, adapterCommingSoon);
    }

    private void setupRecyclerView(RecyclerView recyclerView, ComicAdapter adapter) {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerView.setAdapter(adapter);
    }

    private void loadComics(String type, ComicAdapter adapter) {
        comicViewModel.getComics(2, type)
                .observe(getViewLifecycleOwner(), items -> {

                    if (items != null && !items.isEmpty()) {
                        adapter.setComics(items); // 🔥 tự tắt shimmer
                    } else {
                        adapter.setLoading(false);
                        Toast.makeText(
                                requireContext(),
                                "Không tải được dữ liệu: " + type,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void setControl(View view) {
        rvComics = view.findViewById(R.id.rvComics);
        rcvComicFull = view.findViewById(R.id.rcvComicFull);
        rcvRelease = view.findViewById(R.id.rcvRelease);
        rcvCommingSoon = view.findViewById(R.id.rcvCommingSoon);
    }
}
