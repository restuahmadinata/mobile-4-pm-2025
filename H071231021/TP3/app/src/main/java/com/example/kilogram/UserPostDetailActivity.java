package com.example.kilogram;

import android.content.Intent;
import android.net.Uri;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPostDetailActivity extends AppCompatActivity {
    private static final String TAG = "UserPostDetailActivity";

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
        String postId = getIntent().getStringExtra("post_id");
        Log.d(TAG, "Looking for post ID: " + postId);

        Post post = null;

        // First check in user posts
        for (Post userPost : UserPostDataProvider.getUserPosts()) {
            if (userPost.getId().equals(postId)) {
                post = userPost;
                Log.d(TAG, "Found in user posts!");
                break;
            }
        }

        // If not found in user posts, check other posts
        if (post == null) {
            post = PostDataProvider.getPostById(postId);
            Log.d(TAG, post != null ? "Found in other posts!" : "Post not found anywhere!");
        }

        if (post != null) {
            final Post finalPost = post; // Create final reference for use in lambda

            // Log post details for debugging
            Log.d(TAG, "Post details: ID=" + post.getId() +
                    ", Username=" + post.getUsername() +
                    ", ImageURL=" + post.getImageUrl());

            // Set text fields
            usernameView.setText(post.getUsername());
            captionView.setText(post.getCaption());
            likesView.setText(String.valueOf(post.getLikeCount()) + " likes");
            timeView.setText(post.getTimePosted());

            // Load profile image
            Glide.with(this)
                    .load(post.getUserProfileImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(profileImageView);

            // Handle post image loading with better URI permission handling
            String imageUrl = post.getImageUrl();
            Log.d(TAG, "Loading image from: " + imageUrl);

            // Try different loading strategies based on URI type
            if (imageUrl != null && imageUrl.startsWith("content://")) {
                try {
                    Uri contentUri = Uri.parse(imageUrl);

                    // Try to take persistent permissions when possible
                    try {
                        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(contentUri, takeFlags);
                        Log.d(TAG, "Took persistable permission for URI");
                    } catch (Exception e) {
                        Log.w(TAG, "Could not take persistable permission for URI", e);
                    }

                    Log.d(TAG, "Loading content URI: " + contentUri);

                    // Load image with specific options for content URIs
                    Glide.with(this)
                            .load(contentUri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(postImageView);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading content URI image", e);
                    fallbackImageLoad(postImageView, imageUrl);
                }
            } else if (imageUrl != null && imageUrl.startsWith("file://")) {
                try {
                    Uri fileUri = Uri.parse(imageUrl);
                    Log.d(TAG, "Loading file URI: " + fileUri);

                    Glide.with(this)
                            .load(fileUri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(postImageView);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading file URI image", e);
                    fallbackImageLoad(postImageView, imageUrl);
                }
            } else {
                // For remote URLs or anything else
                fallbackImageLoad(postImageView, imageUrl);
            }
        } else {
            Log.e(TAG, "Post not found with ID: " + postId);
        }

        // Set back button
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Fallback method for loading images
     */
    private void fallbackImageLoad(ImageView imageView, String imageUrl) {
        Log.d(TAG, "Using fallback image loading for: " + imageUrl);
        try {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
        } catch (Exception e) {
            Log.e(TAG, "Fallback image loading failed", e);
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}