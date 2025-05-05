package com.example.kilogram;

import java.util.List;

public class Highlight {
    private String id;
    private String title;
    private String coverImageUrl;
    private List<String> mediaUrls; // URLs to the media (images/videos) in this highlight
    private long createdAt;

    public Highlight(String id, String title, String coverImageUrl, List<String> mediaUrls) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.mediaUrls = mediaUrls;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}