package com.example.n02_appcomic.model;

import java.util.List;

public class DataDetail {
    private SEOOnPage seoOnPage;
    private List<BreadCrumb> breadCrumb;
    private Params params;
    private Item item;
    private String appDomainCDNImage;

    public SEOOnPage getSEOOnPage() {
        return seoOnPage;
    }

    public void setSEOOnPage(SEOOnPage value) {
        this.seoOnPage = value;
    }

    public List<BreadCrumb> getBreadCrumb() {
        return breadCrumb;
    }

    public void setBreadCrumb(List<BreadCrumb> value) {
        this.breadCrumb = value;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params value) {
        this.params = value;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item value) {
        this.item = value;
    }

    public String getAppDomainCDNImage() {
        return appDomainCDNImage;
    }

    public void setAppDomainCDNImage(String value) {
        this.appDomainCDNImage = value;
    }
}
