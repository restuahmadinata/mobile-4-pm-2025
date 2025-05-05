package com.example.kilogram;

public class Story {
    private String id;
    private String username;
    private String profileImageUrl;
    private boolean hasUnseenStory;

    public Story(String id, String username, String profileImageUrl, boolean hasUnseenStory) {
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.hasUnseenStory = hasUnseenStory;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public boolean isHasUnseenStory() {
        return hasUnseenStory;
    }

    public void setHasUnseenStory(boolean hasUnseenStory) {
        this.hasUnseenStory = hasUnseenStory;
    }
}