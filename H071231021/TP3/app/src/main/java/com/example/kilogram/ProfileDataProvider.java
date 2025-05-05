package com.example.kilogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProfileDataProvider {

    private static Map<String, ProfileData> profiles = new HashMap<>();

    static {
        initializeProfiles();
    }

    private static void initializeProfiles() {
        List<Story> stories = StoryDataProvider.getDummyStories();
        Random random = new Random();

        // Create profiles for each story user
        for (Story story : stories) {
            String username = story.getUsername();
            String userId = "user_" + username.toLowerCase().replace(" ", "_");
            String name = generateName(username);
            String profileImageUrl = story.getProfileImageUrl();
            String bio = generateBio(username);
            String link = "https://" + username.toLowerCase().replace(" ", "") + ".com";
            int postCount = random.nextInt(20) + 1;
            int followersCount = random.nextInt(5000) + 100;
            int followingCount = random.nextInt(1000) + 50;

            // Generate some posts for this user
            List<Post> userPosts = generateUserPosts(userId, username, profileImageUrl, postCount);

            ProfileData profile = new ProfileData(
                    userId, username, name, profileImageUrl, bio, link,
                    postCount, followersCount, followingCount, userPosts
            );

            profiles.put(username, profile);
        }
    }

    private static String generateName(String username) {
        String[] nameParts = username.split(" ");
        if (nameParts.length > 1) {
            return username;
        } else {
            return username + " " + (char)('A' + new Random().nextInt(26)) + ".";
        }
    }

    private static String generateBio(String username) {
        String[] bios = {
                "Photography lover | Travel enthusiast",
                "Living life to the fullest ✨",
                "Coffee addict | Code creator",
                "Professional dreamer | Amateur everything else",
                "Just here to have fun 🎉",
                "Work hard, play harder 💪",
                "Capturing moments | Creating memories",
                "Digital nomad | Coffee enthusiast",
                "Always learning, always growing",
                "Lover of sunsets and good vibes"
        };
        return bios[Math.abs(username.hashCode() % bios.length)];
    }

    private static List<Post> generateUserPosts(String userId, String username, String profileImageUrl, int count) {
        List<Post> posts = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String postId = userId + "_post_" + i;
            int imageId = Math.abs((username.hashCode() + i) % 1000);
            String imageUrl = "https://picsum.photos/id/" + imageId + "/500/500";

            String[] captions = {
                    "Loving this day!",
                    "Perfect weather ☀️",
                    "Can't get enough of this view",
                    "New favorite spot",
                    "Take me back to this moment",
                    "Making memories",
                    "Weekend vibes",
                    "Just another day in paradise",
                    "Living my best life",
                    "This place is amazing!"
            };
            String caption = captions[random.nextInt(captions.length)];

            int likeCount = random.nextInt(500) + 10;
            boolean isLiked = random.nextBoolean();
            String timePosted = (random.nextInt(30) + 1) + " days ago";
            int commentCount = random.nextInt(50) + 1;
            int shareCount = random.nextInt(20);

            Post post = new Post(postId, username, profileImageUrl, imageUrl,
                    caption, likeCount, isLiked, timePosted,
                    commentCount, shareCount);
            posts.add(post);
        }

        return posts;
    }

    public static ProfileData getProfileByUsername(String username) {
        return profiles.get(username);
    }

    public static List<ProfileData> getAllProfiles() {
        return new ArrayList<>(profiles.values());
    }
}