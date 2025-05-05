package com.example.kilogram;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PostAdapter postAdapter;
    private RecyclerView postsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up stories RecyclerView
        RecyclerView storiesRecyclerView = findViewById(R.id.stories_recycler_view);
        storiesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Get dummy data from the provider class
        StoryAdapter storyAdapter = new StoryAdapter(StoryDataProvider.getDummyStories());
        storiesRecyclerView.setAdapter(storyAdapter);

        // Set up posts RecyclerView
        postsRecyclerView = findViewById(R.id.recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Make absolutely sure we're only getting other users' posts
        List<Post> otherUsersPosts = PostDataProvider.getAllPosts();
        String currentUsername = UserProfile.getInstance().getUsername();

        // Double-check that none of the posts are from the current user
        for (int i = otherUsersPosts.size() - 1; i >= 0; i--) {
            if (otherUsersPosts.get(i).getUsername().equals(currentUsername)) {
                otherUsersPosts.remove(i);
            }
        }

        // Set the adapter with filtered posts
        postAdapter = new PostAdapter(otherUsersPosts);
        postsRecyclerView.setAdapter(postAdapter);

        // Set up bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh with only other users' posts (with extra filtering)
        if (postAdapter != null && postsRecyclerView != null) {
            List<Post> otherUsersPosts = PostDataProvider.getAllPosts();
            String currentUsername = UserProfile.getInstance().getUsername();

            // Double-check that none of the posts are from the current user
            for (int i = otherUsersPosts.size() - 1; i >= 0; i--) {
                if (otherUsersPosts.get(i).getUsername().equals(currentUsername)) {
                    otherUsersPosts.remove(i);
                }
            }

            postAdapter = new PostAdapter(otherUsersPosts);
            postsRecyclerView.setAdapter(postAdapter);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Already on home screen
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Navigate to MyProfileActivity for current user
                Intent profileIntent = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });

        // Set the home item as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}