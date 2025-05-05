package com.example.kilogram;

public class Post {
    private String id;
    private String username;
    private String userProfileImageUrl;
    private String imageUrl;
    private String caption;
    private int likeCount;
    private boolean isLiked;
    private String timePosted;
    private int commentCount;
    private int shareCount;

    public Post(String id, String username, String userProfileImageUrl, String imageUrl,
                String caption, int likeCount, boolean isLiked, String timePosted,
                int commentCount, int shareCount) {
        this.id = id;
        this.username = username;
        this.userProfileImageUrl = userProfileImageUrl;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.timePosted = timePosted;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
    }

    public Post(String postId, String username, String profileImageUrl, String imageUrl, String caption, long l, int i, boolean b) {
    }

    // Adding getters and setters for the new fields
    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    // Existing methods
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void toggleLike() {
        isLiked = !isLiked;
        if (isLiked) {
            likeCount++;
        } else {
            likeCount--;
        }
    }

    public String getTimePosted() {
        return timePosted;
    }
}