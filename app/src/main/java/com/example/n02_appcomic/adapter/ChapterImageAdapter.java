package com.example.n02_appcomic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.n02_appcomic.R;
import com.example.n02_appcomic.model.ChapterImage;

import java.util.List;

public class ChapterImageAdapter extends RecyclerView.Adapter<ChapterImageAdapter.ImageViewHolder> {

    private final List<ChapterImage> imageList;
    private final String baseUrl;
    private final String chapterPath;
    private final Context context;

    public ChapterImageAdapter(Context context, List<ChapterImage> imageList, String baseUrl, String chapterPath) {
        this.context = context;
        this.imageList = imageList;
        this.baseUrl = baseUrl;
        this.chapterPath = chapterPath;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ChapterImage img = imageList.get(position);
        String fullImageUrl = baseUrl + "/" + chapterPath + "/" + img.getImage_file();

        // Reset trạng thái khi recycle
        holder.loadingView.setVisibility(View.VISIBLE);
        holder.imageView.setImageDrawable(null);

        Glide.with(context)
                .load(fullImageUrl)
                .fitCenter()
                .dontAnimate()
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable com.bumptech.glide.load.engine.GlideException e,
                            Object model,
                            com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                            boolean isFirstResource
                    ) {
                        holder.loadingView.setVisibility(View.GONE);
                        Log.e("GLIDE", "Load image failed: " + fullImageUrl, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            android.graphics.drawable.Drawable resource,
                            Object model,
                            com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                            com.bumptech.glide.load.DataSource dataSource,
                            boolean isFirstResource
                    ) {
                        holder.loadingView.setVisibility(View.GONE);
                        return false; // Glide set ảnh vào ImageView
                    }
                })
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View loadingView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chapterImageView);
            loadingView = itemView.findViewById(R.id.loadingView);
        }
    }

}

