package com.example.n02_appcomic.model;

public class Pagination {
    private long totalItems;
    private long totalItemsPerPage;
    private long currentPage;
    private long pageRanges;

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long value) {
        this.totalItems = value;
    }

    public long getTotalItemsPerPage() {
        return totalItemsPerPage;
    }

    public void setTotalItemsPerPage(long value) {
        this.totalItemsPerPage = value;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long value) {
        this.currentPage = value;
    }

    public long getPageRanges() {
        return pageRanges;
    }

    public void setPageRanges(long value) {
        this.pageRanges = value;
    }

}
