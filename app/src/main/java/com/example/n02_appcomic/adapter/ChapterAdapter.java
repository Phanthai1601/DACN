package com.example.n02_appcomic.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n02_appcomic.R;
import com.example.n02_appcomic.model.ServerData;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private final List<ServerData> chapterList;
    private final OnChapterClickListener listener;
    private int currentChapterIndex = -1;

    public ChapterAdapter(List<ServerData> chapterList, OnChapterClickListener listener) {
        this.chapterList = chapterList;
        this.listener = listener;
    }
    public interface OnChapterClickListener {
        void onChapterClick(ServerData chapter);
    }


    public void setCurrentChapterIndex(int index) {
        this.currentChapterIndex = index;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_name_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        ServerData chapter = chapterList.get(position);
        holder.chapterName.setText("Chapter " + chapter.getChapterName());

        if (position == currentChapterIndex) {
            holder.chapterName.setTextColor(
                    holder.itemView.getContext().getColor(R.color.black)
            );
            holder.itemView.setBackgroundResource(R.drawable.bg_chapter_selected);
        } else {
            holder.chapterName.setTextColor(
                    holder.itemView.getContext().getColor(android.R.color.black)
            );
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChapterClick(chapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterName;

        ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterName = itemView.findViewById(R.id.nameChapter);
        }
    }
}
