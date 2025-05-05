package com.example.kilogram;

import java.util.ArrayList;
import java.util.List;

public class PostDataProvider {
    private static List<Post> posts = null;

    /**
     * Initializes the posts list if not already done
     */
    private static void initPosts() {
        if (posts == null) {
            posts = new ArrayList<>();
            String currentUsername = UserProfile.getInstance().getUsername();

            // Get all profiles from ProfileDataProvider
            List<ProfileData> profiles = ProfileDataProvider.getAllProfiles();

            // For each profile that's not the current user, add one post
            for (ProfileData profile : profiles) {
                String username = profile.getUsername();

                // Skip if this is the current user's profile or "Your Story"
                if (!username.equals(currentUsername) && !username.equals("Your Story")) {
                    List<Post> userPosts = profile.getPosts();

                    // If the user has posts, add their first post to the home feed
                    if (userPosts != null && !userPosts.isEmpty()) {
                        posts.add(userPosts.get(0));
                    }
                }
            }
        }
    }

    /**
     * Gets all other users' posts for the feed (excludes current user)
     * @return List of all posts for the feed
     */
    public static List<Post> getAllPosts() {
        initPosts();

        // Double-check to make sure we're not including the current user's posts
        List<Post> filteredPosts = new ArrayList<>();
        String currentUsername = UserProfile.getInstance().getUsername();

        for (Post post : posts) {
            if (!post.getUsername().equals(currentUsername) && !post.getUsername().equals("Your Story")) {
                filteredPosts.add(post);
            }
        }

        return filteredPosts;
    }

    /**
     * Gets posts by a specific username
     * @param username The username to filter by
     * @return List of posts by the specified user
     */
    public static List<Post> getPostsByUsername(String username) {
        // Instead of filtering our own list, get the posts directly from profile data
        ProfileData profile = ProfileDataProvider.getProfileByUsername(username);
        if (profile != null) {
            return profile.getPosts();
        }
        return new ArrayList<>();
    }

    /**
     * Gets all posts for the main feed (only other users' posts)
     * @return List of other users' posts for the main feed
     */
    public static List<Post> getDummyPosts() {
        // This method now returns the same as getAllPosts() - only other users' posts
        return getAllPosts();
    }

    /**
     * Get a specific post by ID
     * @param id The ID of the post to retrieve
     * @return The Post object if found, null otherwise
     */
    public static Post getPostById(String id) {
        initPosts();
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                return post;
            }
        }
        return null;
    }
}