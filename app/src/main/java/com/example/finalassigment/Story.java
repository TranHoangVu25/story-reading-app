package com.example.finalassigment;

public class Story {
    private int imageRes;
    private String title;
    private long views;

    public Story(int imageRes, String title, long views) {
        this.imageRes = imageRes;
        this.title = title;
        this.views = views;
    }

    // Getter methods
    public int getImageRes() { return imageRes; }
    public String getTitle() { return title; }
    public long getViews() { return views; }
}