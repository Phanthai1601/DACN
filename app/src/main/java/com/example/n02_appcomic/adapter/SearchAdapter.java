package com.example.n02_appcomic.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.n02_appcomic.ComicDetailActivity;
import com.example.n02_appcomic.R;
import com.example.n02_appcomic.model.ChaptersLatest;
import com.example.n02_appcomic.model.Item;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ComicViewHolder> {

    private List<Item> comics;
    private OnComicClickListener listener;

    public interface OnComicClickListener {
        void onComicClick(Item comic);
    }

    public SearchAdapter(OnComicClickListener listener) {
        this.listener = listener;
    }

    public void setComics(List<Item> comics) {
        this.comics = comics;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comic_search, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Item item = comics.get(position);
        holder.name.setText(item.getName());
        List<ChaptersLatest> latestChapters = item.getChaptersLatests();
        if (latestChapters != null && !latestChapters.isEmpty()) {
            String chapterText = latestChapters.get(0).getChapterName(); // hoặc getChapterTitle()
            holder.chapterLatest.setText("Chương "+ chapterText);
        } else {
            holder.chapterLatest.setText("Chưa có chương");
        }

        String imageUrl = "https://img.otruyenapi.com/uploads/comics/" + item.getThumbURL();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ComicDetailActivity.class);
            intent.putExtra("slug", item.getSlug());
            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return comics != null ? comics.size() : 0;
    }

    public static class ComicViewHolder extends RecyclerView.ViewHolder {
        TextView name, chapterLatest;
        ImageView thumbnail;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvComicName);
            thumbnail = itemView.findViewById(R.id.ivComicThumbnail);
            chapterLatest = itemView.findViewById(R.id.tvChapterLatest);
        }
    }
}
