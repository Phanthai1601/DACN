package com.example.n02_appcomic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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

        // Preload ảnh để cuộn mượt
        Glide.with(context).load(fullImageUrl).preload();

        // Load ảnh
        Glide.with(context)
                .load(fullImageUrl)
                .dontAnimate() // tránh hiệu ứng gây nhấp nháy
                .fitCenter() // giữ tỉ lệ
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chapterImageView);
        }
    }
}

