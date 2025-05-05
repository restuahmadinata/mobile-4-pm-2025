package com.example.kilogram;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String username = getIntent().getStringExtra("username");

        if (username != null) {
            ProfileData profile = ProfileDataProvider.getProfileByUsername(username);

            if (profile != null) {
                // Set profile details
                TextView nameView = findViewById(R.id.profile_name);
                TextView bioView = findViewById(R.id.profile_bio);
                TextView linkView = findViewById(R.id.profile_link);
                TextView postCountView = findViewById(R.id.profile_post_count);
                TextView followerCountView = findViewById(R.id.profile_follower_count);
                TextView followingCountView = findViewById(R.id.profile_following_count);
                ImageView profileImageView = findViewById(R.id.profile_image);

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

                // Set up the RecyclerView for profile posts
                RecyclerView postsGrid = findViewById(R.id.profile_posts_grid);
                postsGrid.setLayoutManager(new GridLayoutManager(this, 3));
                ProfilePostsAdapter adapter = new ProfilePostsAdapter(profile.getPosts());
                postsGrid.setAdapter(adapter);
            }
        }
    }
}