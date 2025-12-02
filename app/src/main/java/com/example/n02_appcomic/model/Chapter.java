package com.example.n02_appcomic.model;

import java.util.List;

public class Chapter {
    private String serverName;
    private List<ServerData> server_data;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String value) {
        this.serverName = value;
    }

    public List<ServerData> getServer_data() {
        return server_data;
    }

    public void setServer_data(List<ServerData> server_data) {
        this.server_data = server_data;
    }
}
