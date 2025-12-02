package com.example.n02_appcomic.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
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
import com.example.n02_appcomic.model.Category;
import com.example.n02_appcomic.model.ChaptersLatest;
import com.example.n02_appcomic.model.Item;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<Item> favorites;
    private OnFavoriteActionListener listener;

    public interface OnFavoriteActionListener {
        void onRemoveFavorite(int position);
    }

    public FavoriteAdapter(Context context, List<Item> favorites, OnFavoriteActionListener listener) {
        this.context = context;
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Item item = favorites.get(position);

        holder.txtTitle.setText(item.getName());

        if (item.getAuthor() != null && !item.getAuthor().isEmpty()) {
            holder.txtAuthor.setText(item.getAuthor().get(0));
        } else {
            holder.txtAuthor.setText("Không rõ");
        }

        if (item.getCategory() != null && !item.getCategory().isEmpty()) {
            StringBuilder categoriesText = new StringBuilder();
            for (Category category : item.getCategory()) {
                if (categoriesText.length() > 0) categoriesText.append(", ");
                categoriesText.append(category.getName());
            }
            holder.txtCategory.setText(categoriesText.toString());
        } else {
            holder.txtCategory.setText("Không rõ thể loại");
        }

        String imageUrl = "https://img.otruyenapi.com/uploads/comics/" + item.getThumbURL();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.bg_image)
                .into(holder.imgComic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ComicDetailActivity.class);
            intent.putExtra("slug", item.getSlug());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView imgComic;
        TextView txtTitle, txtAuthor, txtCategory;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgComic = itemView.findViewById(R.id.imgComic);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtCategory = itemView.findViewById(R.id.txtCategory);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Tùy chọn");
            menu.add(this.getAdapterPosition(), 101, 0, "Xóa khỏi yêu thích")
                    .setOnMenuItemClickListener(item -> {
                        if (listener != null) {
                            listener.onRemoveFavorite(getAdapterPosition());
                        }
                        return true;
                    });
        }
    }

    public void removeItem(int position) {
        favorites.remove(position);
        notifyItemRemoved(position);
    }
}



