package com.example.kilogram;

import java.util.ArrayList;
import java.util.List;

public class UserPostDataProvider {
    private static List<Post> userPosts = null;

    /**
     * Initialize user posts if not already done
     */
    private static void initUserPosts() {
        if (userPosts == null) {
            userPosts = new ArrayList<>();
            String username = UserProfile.getInstance().getUsername();
            String profileImageUrl = UserProfile.getInstance().getProfileImageUrl();

            // Add some initial posts for the current user
            for (int i = 0; i < 6; i++) {
                userPosts.add(new Post(
                        "user_" + i,
                        username,
                        profileImageUrl,
                        "https://picsum.photos/id/" + (200 + i) + "/500/500",
                        "My personal post #" + i,
                        50 + i * 10,
                        i % 2 == 0,
                        (i + 1) + " days ago",
                        5 + i,
                        i
                ));
            }
        }
    }

    /**
     * Get all posts for the current user
     * @return List of current user's posts
     */
    public static List<Post> getUserPosts() {
        initUserPosts();
        return userPosts;
    }

    /**
     * Add a new post to the current user's posts
     * @param newPost The new post to add
     */
    public static void addNewPost(Post newPost) {
        initUserPosts();
        // Add the new post at the beginning of the list
        userPosts.add(0, newPost);
    }

    /**
     * Get user post count
     * @return Number of posts by current user
     */
    public static int getPostCount() {
        initUserPosts();
        return userPosts.size();
    }
}