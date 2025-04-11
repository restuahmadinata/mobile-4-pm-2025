package com.radinata.tp1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText nameEditView;
    private EditText usernameEditView;
    private EditText bioEditView;
    private String originalImageUri; // Keep track of the original URI


    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Set gambar ke CircleImageView
                        profileImageView.setImageURI(selectedImageUri);
                        profileImageView.setTag(selectedImageUri.toString()); // Simpan URI ke tag
                    } else {
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Menghilangkan background navigation bar
        getWindow().setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        profileImageView = findViewById(R.id.user_image);
        nameEditView = findViewById(R.id.name_edit);
        usernameEditView = findViewById(R.id.username_edit);
        bioEditView = findViewById(R.id.bio_edit);

        // Ambil URI gambar dari Intent
        originalImageUri = getIntent().getStringExtra("EXTRA_IMAGE_URI");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_profile);
        String name = getIntent().getStringExtra("EXTRA_NAME");
        String username = getIntent().getStringExtra("EXTRA_USERNAME");
        String bio = getIntent().getStringExtra("EXTRA_BIO");

        // Set gambar berdasarkan prioritas (URI dulu, imageResId sebagai fallback)
        if (originalImageUri != null && !originalImageUri.isEmpty()) {
            try {
                Uri parsedUri = Uri.parse(originalImageUri);
                profileImageView.setImageURI(parsedUri);
                profileImageView.setTag(originalImageUri); // Store the URI as tag
            } catch (Exception e) {
                // Fallback jika URI tidak valid
                profileImageView.setImageResource(imageResId);
                profileImageView.setTag(null);
            }
        } else {
            profileImageView.setImageResource(imageResId);
            profileImageView.setTag(null);
        }

        profileImageView.setOnClickListener(v -> {
            // Buka galeri untuk memilih gambar
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });


        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Kembali ke activity sebelumnya
            getOnBackPressedDispatcher().onBackPressed();
        });

        nameEditView.setText(name);
        usernameEditView.setText(username);
        bioEditView.setText(bio);

        // Add real-time validation using TextWatcher
        nameEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidName(s.toString())) {
                    nameEditView.setError("Nama hanya boleh berisi huruf, spasi, dan beberapa karakter khusus");
                } else {
                    nameEditView.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        usernameEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidUsername(s.toString())) {
                    usernameEditView.setError("Username harus dimulai dengan @ dan hanya boleh berisi huruf, angka, titik, dan garis bawah (maksimal 15 karakter)");
                } else {
                    usernameEditView.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        bioEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidBio(s.toString())) {
                    bioEditView.setError("Bio tidak boleh melebihi 150 karakter");
                } else {
                    bioEditView.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_profile_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            String nameText = nameEditView.getText().toString().trim();
            String usernameText = usernameEditView.getText().toString().trim();
            String bioText = bioEditView.getText().toString().trim();

            // Validate input using regex
            if (nameText.isEmpty() || usernameText.isEmpty() || bioText.isEmpty()) {
                Toast.makeText(EditProfileActivity.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidName(nameText)) {
                Toast.makeText(EditProfileActivity.this, "Nama hanya boleh berisi huruf, spasi, dan beberapa karakter khusus", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidUsername(usernameText)) {
                Toast.makeText(EditProfileActivity.this, "Username harus dimulai dengan @ dan hanya boleh berisi huruf, angka, titik, dan garis bawah (maksimal 15 karakter)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidBio(bioText)) {
                Toast.makeText(EditProfileActivity.this, "Bio tidak boleh melebihi 150 karakter", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("EXTRA_NAME", nameText);
            resultIntent.putExtra("EXTRA_USERNAME", usernameText);
            resultIntent.putExtra("EXTRA_BIO", bioText);

            // Kirim URI gambar jika ada (dari tag atau gunakan original jika tidak diubah)
            if (profileImageView.getTag() != null) {
                resultIntent.putExtra("EXTRA_IMAGE_URI", profileImageView.getTag().toString());
            } else if (originalImageUri != null && !originalImageUri.isEmpty()) {
                // If user didn't select a new image but had one before, keep the original
                resultIntent.putExtra("EXTRA_IMAGE_URI", originalImageUri);
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // Validation methods using regex
    private boolean isValidName(String name) {
        // Allow letters, spaces, and common name characters
        String regex = "^[\\p{L} .'-]+$";
        return !name.isEmpty() && name.matches(regex);
    }

    private boolean isValidUsername(String username) {
        // Username must start with '@' and have maximum 15 characters including '@'
        // After @ can contain letters, numbers, underscores, and periods
        String regex = "^@[a-zA-Z0-9._]{1,14}$";
        return username.matches(regex);
    }

    private boolean isValidBio(String bio) {
        // Allow most characters but limit length
        // This regex is less restrictive but checks length (max 150 chars)
        return bio.length() <= 150;
    }
}