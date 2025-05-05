package com.example.kilogram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.UUID;

public class NewPostActivity extends AppCompatActivity {
    private static final String TAG = "NewPostActivity";

    private ImageView imagePreview;
    private EditText captionInput;
    private Button uploadButton;
    private Uri selectedImageUri = null;

    // Activity result launcher for gallery
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Log.d(TAG, "Selected image URI: " + selectedImageUri);

                    // Take persistent permissions right after selection
                    if (selectedImageUri != null) {
                        try {
                            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(
                                    selectedImageUri, takeFlags);
                            Log.d(TAG, "Took persistable URI permission");
                        } catch (Exception e) {
                            Log.e(TAG, "Error taking persistable URI permission", e);
                            // Continue anyway as this might not be fatal
                        }
                    }

                    // Load preview image
                    Glide.with(this)
                            .load(selectedImageUri)
                            .centerCrop()
                            .into(imagePreview);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_post), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        imagePreview = findViewById(R.id.image_preview);
        captionInput = findViewById(R.id.caption_input);
        uploadButton = findViewById(R.id.upload_button);
        Button selectImageButton = findViewById(R.id.select_image_button);

        // Set up click listener for image selection
        selectImageButton.setOnClickListener(v -> openGallery());

        // Set up click listener for upload button
        uploadButton.setOnClickListener(v -> uploadPost());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        galleryLauncher.launch(intent);
    }

    private void uploadPost() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        String caption = captionInput.getText().toString();

        // Make sure URI permission is granted
        try {
            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
            Log.d(TAG, "Confirmed persistent URI permission for: " + selectedImageUri);
        } catch (Exception e) {
            Log.e(TAG, "Error taking persistable URI permission", e);
            // Continue anyway, as this might not be critical
        }

        String postId = UUID.randomUUID().toString();
        String imageUrl = selectedImageUri.toString();
        String timePosted = "Just now";

        Log.d(TAG, "Creating post with image URL: " + imageUrl);

        // Create a new post with correct data
        Post newPost = new Post(
                postId,
                UserProfile.getInstance().getUsername(),
                UserProfile.getInstance().getProfileImageUrl(),
                imageUrl,
                caption,
                0,  // initial like count
                false,  // not initially liked
                timePosted,
                0,  // initial comment count
                0   // initial share count
        );

        // Add to user's posts
        UserPostDataProvider.addNewPost(newPost);

        // Update post count in the user profile
        UserProfile.getInstance().setPostCount(UserPostDataProvider.getPostCount());

        // Show success message
        Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();

        // Gunakan OnResume lebih baik
        // Navigate to MyProfileActivity instead of UserPostDetailActivity
        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        // Close this activity
        finish();
    }
}