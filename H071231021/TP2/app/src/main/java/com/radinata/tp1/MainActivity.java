package com.radinata.tp1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    String imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ImageButton editProfileButton = findViewById(R.id.btn_edit_profile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                de.hdodenhof.circleimageview.CircleImageView profileImageView = findViewById(R.id.profile_image);


                // Get image URI from tag if available
                if (profileImageView.getTag() != null) {
                    imageUri = profileImageView.getTag().toString();
                }


                int imageResId = R.drawable.profile; // Default image resource

                TextView nameTextView = findViewById(R.id.name);
                TextView usernameTextView = findViewById(R.id.username);
                TextView bioTextView = findViewById(R.id.bio);

                String nameText = nameTextView.getText().toString();
                String usernameText = usernameTextView.getText().toString();
                String bioText = bioTextView.getText().toString();



                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                intent.putExtra("EXTRA_IMAGE", imageResId);
                intent.putExtra("EXTRA_IMAGE_URI", imageUri);
                intent.putExtra("EXTRA_NAME", nameText);
                intent.putExtra("EXTRA_USERNAME", usernameText);
                intent.putExtra("EXTRA_BIO", bioText);
                startActivityForResult(intent, 1);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Ambil data dari EditProfileActivity
            String name = data.getStringExtra("EXTRA_NAME");
            String username = data.getStringExtra("EXTRA_USERNAME");
            String bio = data.getStringExtra("EXTRA_BIO");
            String imageUri = data.getStringExtra("EXTRA_IMAGE_URI");

            // Perbarui tampilan di MainActivity
            TextView nameTextView = findViewById(R.id.name);
            TextView usernameTextView = findViewById(R.id.username);
            TextView bioTextView = findViewById(R.id.bio);
            TextView namePostTextView1 = findViewById(R.id.name_post1);
            TextView namePostTextView2 = findViewById(R.id.name_post2);
            TextView namePostTextView3 = findViewById(R.id.name_post3);
            TextView usernamePostTextView1 = findViewById(R.id.username_post);
            TextView usernamePostTextView2 = findViewById(R.id.username_post1);
            TextView usernamePostTextView3 = findViewById(R.id.username_post2);
            de.hdodenhof.circleimageview.CircleImageView profileImageView = findViewById(R.id.profile_image);
            de.hdodenhof.circleimageview.CircleImageView profilePostImageView1 = findViewById(R.id.profile_post);
            de.hdodenhof.circleimageview.CircleImageView profilePostImageView2 = findViewById(R.id.profile_post1);
            de.hdodenhof.circleimageview.CircleImageView profilePostImageView3 = findViewById(R.id.profile_post2);

            nameTextView.setText(name);
            usernameTextView.setText(username);
            bioTextView.setText(bio);
            namePostTextView1.setText(name);
            namePostTextView2.setText(name);
            namePostTextView3.setText(name);
            usernamePostTextView1.setText(username);
            usernamePostTextView2.setText(username);
            usernamePostTextView3.setText(username);

            if (imageUri != null) {
                Uri parsedUri = Uri.parse(imageUri);

                // Set image and save URI as tag
                profileImageView.setImageURI(parsedUri);
                profileImageView.setTag(imageUri);

                profilePostImageView1.setImageURI(parsedUri);
                profilePostImageView1.setTag(imageUri);

                profilePostImageView2.setImageURI(parsedUri);
                profilePostImageView2.setTag(imageUri);

                profilePostImageView3.setImageURI(parsedUri);
                profilePostImageView3.setTag(imageUri);
            } else {
                profileImageView.setImageResource(R.drawable.profile);
                profileImageView.setTag(null);

                profilePostImageView1.setImageResource(R.drawable.profile);
                profilePostImageView1.setTag(null);

                profilePostImageView2.setImageResource(R.drawable.profile);
                profilePostImageView2.setTag(null);

                profilePostImageView3.setImageResource(R.drawable.profile);
                profilePostImageView3.setTag(null);
            }
        }
    }
}