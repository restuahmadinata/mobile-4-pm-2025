package com.example.kilogram;

import java.util.List;

public class ProfileData {
    private String userId;
    private String username;
    private String name;
    private String profileImageUrl;
    private String bio;
    private String link;
    private int postCount;
    private int followersCount;
    private int followingCount;
    private List<Post> posts;

    public ProfileData(String userId, String username, String name, String profileImageUrl,
                       String bio, String link, int postCount,
                       int followersCount, int followingCount, List<Post> posts) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.link = link;
        this.postCount = postCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.posts = posts;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getBio() { return bio; }
    public String getLink() { return link; }
    public int getPostCount() { return postCount; }
    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() { return followingCount; }
    public List<Post> getPosts() { return posts; }
}