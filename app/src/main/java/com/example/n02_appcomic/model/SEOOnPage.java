package com.example.n02_appcomic.model;

import java.util.List;

public class SEOOnPage {
    private String ogType;
    private String titleHead;
    private String descriptionHead;
    private List<String> ogImage;
    private String ogURL;

    public String getOgType() {
        return ogType;
    }

    public void setOgType(String value) {
        this.ogType = value;
    }

    public String getTitleHead() {
        return titleHead;
    }

    public void setTitleHead(String value) {
        this.titleHead = value;
    }

    public String getDescriptionHead() {
        return descriptionHead;
    }

    public void setDescriptionHead(String value) {
        this.descriptionHead = value;
    }

    public List<String> getOgImage() {
        return ogImage;
    }

    public void setOgImage(List<String> value) {
        this.ogImage = value;
    }

    public String getOgURL() {
        return ogURL;
    }

    public void setOgURL(String value) {
        this.ogURL = value;
    }

}
