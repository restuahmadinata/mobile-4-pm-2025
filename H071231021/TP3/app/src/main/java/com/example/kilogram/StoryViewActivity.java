package com.example.kilogram;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class StoryViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.story_view_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get data from intent
        String profileImageUrl = getIntent().getStringExtra("profileImageUrl");
        String username = getIntent().getStringExtra("username");

        // Set up UI elements
        ImageView storyImageView = findViewById(R.id.story_image_view);
        TextView usernameTextView = findViewById(R.id.story_username_text);
        ImageButton closeButton = findViewById(R.id.close_button);

        // Set username
        usernameTextView.setText(username);

        // Load profile image as full screen image
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(storyImageView);

        // Set close button action
        closeButton.setOnClickListener(v -> finish());
    }
}