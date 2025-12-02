package com.example.n02_appcomic.model;

public class BreadCrumb {
    private String name;
    private String slug;
    private boolean isCurrent;
    private long position;

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

    public boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean value) {
        this.isCurrent = value;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long value) {
        this.position = value;
    }

}
