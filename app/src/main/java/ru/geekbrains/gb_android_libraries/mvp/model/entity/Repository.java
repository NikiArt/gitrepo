package ru.geekbrains.gb_android_libraries.mvp.model.entity;

import com.google.gson.annotations.Expose;

public class Repository {
    @Expose
    private String id;
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
