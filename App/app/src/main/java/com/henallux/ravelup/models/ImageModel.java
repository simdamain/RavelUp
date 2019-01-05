package com.henallux.ravelup.models;

public class ImageModel {
    private long id;
    private String url;

    public ImageModel(long id, String url) {
        setId(id);
        setUrl(url);
    }

    public ImageModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
