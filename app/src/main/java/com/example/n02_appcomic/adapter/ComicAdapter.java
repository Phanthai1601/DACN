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
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private List<Item> comics = new ArrayList<>();
    private boolean isLoading = true;

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyDataSetChanged();
    }

    public void setComics(List<Item> comics) {
        this.comics = comics;
        isLoading = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {

        if (isLoading) {
            holder.shimmerLayout.setVisibility(View.VISIBLE);
            holder.contentLayout.setVisibility(View.GONE);
            holder.shimmerLayout.startShimmer();
            return;
        }

        Item item = comics.get(position);

        holder.shimmerLayout.stopShimmer();
        holder.shimmerLayout.setVisibility(View.GONE);
        holder.contentLayout.setVisibility(View.VISIBLE);

        holder.name.setText(item.getName());

        List<ChaptersLatest> chapters = item.getChaptersLatests();
        if (chapters != null && !chapters.isEmpty()) {
            holder.chapterLatest.setText("Chương " + chapters.get(0).getChapterName());
        } else {
            holder.chapterLatest.setText("Chưa có chương");
        }

        String imageUrl = "https://img.otruyenapi.com/uploads/comics/" + item.getThumbURL();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ComicDetailActivity.class);
            intent.putExtra("slug", item.getSlug());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return isLoading ? 6 : comics.size();
    }

    static class ComicViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView name, chapterLatest;
        ShimmerFrameLayout shimmerLayout;
        View contentLayout;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.ivComicThumbnail);
            name = itemView.findViewById(R.id.tvComicName);
            chapterLatest = itemView.findViewById(R.id.tvChapterLatest);
            shimmerLayout = itemView.findViewById(R.id.shimmerLayout);
            contentLayout = itemView.findViewById(R.id.contentLayout);
        }
    }
}
