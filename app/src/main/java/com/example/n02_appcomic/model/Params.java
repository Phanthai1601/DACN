package com.example.n02_appcomic.model;

public class Params {
    private String typeSlug;
    private String[] filterCategory;
    private String sortField;
    private String sortType;
    private Pagination pagination;

    public String getTypeSlug() { return typeSlug; }
    public void setTypeSlug(String value) { this.typeSlug = value; }

    public String[] getFilterCategory() { return filterCategory; }
    public void setFilterCategory(String[] value) { this.filterCategory = value; }

    public String getSortField() { return sortField; }
    public void setSortField(String value) { this.sortField = value; }

    public String getSortType() { return sortType; }
    public void setSortType(String value) { this.sortType = value; }

    public Pagination getPagination() { return pagination; }
    public void setPagination(Pagination value) { this.pagination = value; }

}
