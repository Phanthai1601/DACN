package com.example.n02_appcomic.model;

public class Category {
    private String id;
    private String name;
    private String slug;

    public Category(String id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
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

}
