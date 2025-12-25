package com.example.n02_appcomic.bottom_sheet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.R;
import com.example.n02_appcomic.adapter.ChapterAdapter;
import com.example.n02_appcomic.model.ServerData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class ChapterBottomSheet extends BottomSheetDialogFragment {

    private final List<ServerData> chapterList;
    private final int currentChapIndex;
    private final ChapterAdapter.OnChapterClickListener listener;

    public ChapterBottomSheet(
            List<ServerData> chapterList,
            int currentChapIndex,
            ChapterAdapter.OnChapterClickListener listener
    ) {
        this.chapterList = chapterList;
        this.currentChapIndex = currentChapIndex;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.bottom_sheet_chapter_list,
                container,
                false
        );
        ImageView btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dismiss());

        RecyclerView rcvChapter = view.findViewById(R.id.rcvChapter);
        rcvChapter.setLayoutManager(new LinearLayoutManager(getContext()));

        ChapterAdapter adapter = new ChapterAdapter(chapterList, listener);
        adapter.setCurrentChapterIndex(currentChapIndex);

        rcvChapter.setAdapter(adapter);

        // 👇 Tự cuộn tới chap đang đọc
        rcvChapter.scrollToPosition(currentChapIndex);

        return view;
    }
}
