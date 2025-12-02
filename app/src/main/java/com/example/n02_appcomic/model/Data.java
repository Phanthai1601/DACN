package com.example.n02_appcomic.model;

public class Data {
    private SEOOnPage seoOnPage;
    private BreadCrumb[] breadCrumb;
    private String titlePage;
    private Item[] items;
    private Params params;
    private String typeList;
    private String appDomainFrontend;
    private String appDomainCDNImage;

    public SEOOnPage getSEOOnPage() {
        return seoOnPage;
    }

    public void setSEOOnPage(SEOOnPage value) {
        this.seoOnPage = value;
    }

    public BreadCrumb[] getBreadCrumb() {
        return breadCrumb;
    }

    public void setBreadCrumb(BreadCrumb[] value) {
        this.breadCrumb = value;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String value) {
        this.titlePage = value;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] value) {
        this.items = value;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params value) {
        this.params = value;
    }

    public String getTypeList() {
        return typeList;
    }

    public void setTypeList(String value) {
        this.typeList = value;
    }

    public String getAppDomainFrontend() {
        return appDomainFrontend;
    }

    public void setAppDomainFrontend(String value) {
        this.appDomainFrontend = value;
    }

    public String getAppDomainCDNImage() {
        return appDomainCDNImage;
    }

    public void setAppDomainCDNImage(String value) {
        this.appDomainCDNImage = value;
    }

}
