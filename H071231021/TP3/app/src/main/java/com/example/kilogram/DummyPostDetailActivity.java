package com.example.kilogram;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Random;

public class DummyPostDetailActivity extends AppCompatActivity {
    private static final String TAG = "DummyPostDetailActivity";

    // Array of random captions for dummy posts
    private static final String[] RANDOM_CAPTIONS = {
            "Enjoying my day at this beautiful place!",
            "Sometimes the best moments are the unexpected ones",
            "Life is better with friends ❤️",
            "Weekend vibes! #weekendmood #relax",
            "Just another day in paradise",
            "Good food, good mood 🍕",
            "Blessed to witness such beautiful sights",
            "Adventure awaits at every corner",
            "Making memories that will last forever",
            "Taking a moment to appreciate the little things"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.post_detail_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get views
        ImageView postImageView = findViewById(R.id.detail_post_image);
        CircleImageView profileImageView = findViewById(R.id.detail_profile_image);
        TextView usernameView = findViewById(R.id.detail_username);
        TextView captionView = findViewById(R.id.detail_caption);
        TextView likesView = findViewById(R.id.detail_like_count);
        TextView timeView = findViewById(R.id.detail_time_posted);
        ImageButton backButton = findViewById(R.id.detail_back_button);

        // Get data from intent
        String imageUrl = getIntent().getStringExtra("image_url");
        String username = getIntent().getStringExtra("username");
        String profileImageUrl = getIntent().getStringExtra("profile_image_url");
        int likeCount = getIntent().getIntExtra("like_count", 0);

        // Generate random caption
        String caption = getRandomCaption();

        // Set data to views
        usernameView.setText(username);
        captionView.setText(caption);
        likesView.setText(String.format("%d likes", likeCount));
        timeView.setText(getRandomTimePosted());

        // Load profile image
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(profileImageView);

        // Load post image
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(postImageView);

        // Set back button
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Get a random caption from the predefined array
     */
    private String getRandomCaption() {
        Random random = new Random();
        int index = random.nextInt(RANDOM_CAPTIONS.length);
        return RANDOM_CAPTIONS[index];
    }

    /**
     * Generate a random time for the post
     */
    private String getRandomTimePosted() {
        Random random = new Random();
        int timeValue = random.nextInt(24) + 1;

        if (random.nextBoolean()) {
            return timeValue + " hours ago";
        } else {
            return timeValue + " days ago";
        }
    }
}