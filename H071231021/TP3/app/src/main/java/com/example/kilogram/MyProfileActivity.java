package com.example.kilogram;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    private ProfilePostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get current user profile instance
        UserProfile profile = UserProfile.getInstance();

        // Set up profile details
        TextView nameView = findViewById(R.id.my_profile_name);
        TextView bioView = findViewById(R.id.my_profile_bio);
        TextView linkView = findViewById(R.id.my_profile_link);
        TextView postCountView = findViewById(R.id.my_profile_post_count);
        TextView followerCountView = findViewById(R.id.my_profile_follower_count);
        TextView followingCountView = findViewById(R.id.my_profile_following_count);
        ImageView profileImageView = findViewById(R.id.my_profile_image);

        nameView.setText(profile.getName());
        bioView.setText(profile.getBio());
        linkView.setText(profile.getLink());
        postCountView.setText(String.valueOf(profile.getPostCount()));
        followerCountView.setText(String.valueOf(profile.getFollowersCount()));
        followingCountView.setText(String.valueOf(profile.getFollowingCount()));

        // Load profile image with Glide
        Glide.with(this)
                .load(profile.getProfileImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(profileImageView);

        // Set up the new post button
        ImageButton newPostButton = findViewById(R.id.new_post_button);
        newPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, NewPostActivity.class);
            startActivity(intent);
        });

        // Set up highlights
        setupHighlights();

        // Set up the RecyclerView for user's posts
        RecyclerView postsGrid = findViewById(R.id.my_profile_posts_grid);
        postsGrid.setLayoutManager(new GridLayoutManager(this, 3));

        // Get posts from UserPostDataProvider instead
        postsAdapter = new ProfilePostsAdapter(UserPostDataProvider.getUserPosts());
        postsGrid.setAdapter(postsAdapter);

        // Set up bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the posts grid when returning to this activity
        RecyclerView postsGrid = findViewById(R.id.my_profile_posts_grid);
        postsAdapter = new ProfilePostsAdapter(UserPostDataProvider.getUserPosts());
        postsGrid.setAdapter(postsAdapter);

        // Update the post count
        TextView postCountView = findViewById(R.id.my_profile_post_count);
        postCountView.setText(String.valueOf(UserProfile.getInstance().getPostCount()));
    }

    private void setupHighlights() {
        RecyclerView highlightsRecyclerView = findViewById(R.id.highlights_recycler_view);

        // Get user's highlights
        List<Highlight> highlights = HighlightDataProvider.getUserHighlights(
                UserProfile.getInstance().getUserId()
        );

        // Create and set adapter
        HighlightAdapter adapter = new HighlightAdapter(highlights, this);
        highlightsRecyclerView.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Navigate to MainActivity
                Intent homeIntent = new Intent(MyProfileActivity.this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Already on profile screen
                return true;
            }
            return false;
        });

        // Set the profile item as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }
}