package com.example.n02_appcomic.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Item {
    @SerializedName("_id")
    private String id;
    private String name;
    private String slug;
    private String[] originName;
    private String content;
    private String status;
    @SerializedName("thumb_url")
    private String thumbURL;
    private boolean subDocquyen;
    private List<String> author;
    private List<Category> category;
    private List<Chapter> chapters;
    private String updatedAt;
    @SerializedName("chaptersLatest")
    private List<ChaptersLatest> chaptersLatests;

    public Item(String id, String name, String thumbURL, String author, String lastChapter) {
        this.id = id;
        this.name = name;
        this.thumbURL = thumbURL;

        // Tác giả dưới dạng List<String>
        this.author = new ArrayList<>();
        if (author != null && !author.isEmpty()) {
            this.author.add(author);
        }

        // Chap mới nhất dưới dạng List<ChaptersLatest>
        this.chaptersLatests = new ArrayList<>();
        if (lastChapter != null && !lastChapter.isEmpty()) {
            ChaptersLatest latest = new ChaptersLatest();
            latest.setChapterName(lastChapter);
            this.chaptersLatests.add(latest);
        }
    }


    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String value) {
        this.slug = value;
    }

    public String[] getOriginName() {
        return originName;
    }

    public void setOriginName(String[] value) {
        this.originName = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String value) {
        this.thumbURL = value;
    }

    public boolean getSubDocquyen() {
        return subDocquyen;
    }

    public void setSubDocquyen(boolean value) {
        this.subDocquyen = value;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String>  value) {
        this.author = value;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> value) {
        this.category = value;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
    public List<ChaptersLatest> getChaptersLatests() {
        return chaptersLatests;
    }
    public void setChaptersLatests(List<ChaptersLatest> value) {
        this.chaptersLatests = value;
    }

    public void setChapters(List<Chapter> value) {
        this.chapters = value;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String value) {
        this.updatedAt = value;
    }

}
