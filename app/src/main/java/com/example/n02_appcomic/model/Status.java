package com.example.n02_appcomic.model;

import java.io.IOException;

public enum Status {
    COMING_SOON, ONGOING;

    public String toValue() {
        switch (this) {
            case COMING_SOON: return "coming_soon";
            case ONGOING: return "ongoing";
        }
        return null;
    }

    public static com.example.n02_appcomic.model.Status forValue(String value) throws IOException {
        if (value.equals("coming_soon")) return COMING_SOON;
        if (value.equals("ongoing")) return ONGOING;
        throw new IOException("Cannot deserialize Status");
    }
}
