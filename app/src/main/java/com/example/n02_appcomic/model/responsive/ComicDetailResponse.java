package com.example.n02_appcomic.model.responsive;

import com.example.n02_appcomic.model.DataDetail;

public class ComicDetailResponse {
    private String status;
    private String message;
    private DataDetail data;

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }

    public DataDetail getData() { return data; }
    public void setData(DataDetail value) { this.data = value; }
}
