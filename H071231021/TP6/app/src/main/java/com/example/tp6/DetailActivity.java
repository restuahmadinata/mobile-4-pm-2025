package com.example.tp6;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ApiService apiService;
    private ImageView characterImageView;
    private TextView nameTextView, statusTextView, speciesTextView, genderTextView;
    private ProgressBar detailProgressBar;
    private View infoCardView;
    private View detailErrorLayout;
    private ImageView detailReloadIcon;
    private int characterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getClient().create(ApiService.class);

        characterImageView = findViewById(R.id.characterImageView);
        nameTextView = findViewById(R.id.nameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        speciesTextView = findViewById(R.id.speciesTextView);
        genderTextView = findViewById(R.id.genderTextView);
        detailProgressBar = findViewById(R.id.detailProgressBar);
        infoCardView = findViewById(R.id.infoCardView);
        detailErrorLayout = findViewById(R.id.detailErrorLayout);
        detailReloadIcon = findViewById(R.id.detailReloadIcon);

        characterId = getIntent().getIntExtra("character_id", -1);
        if (characterId != -1) {
            loadCharacterDetails(characterId);
        } else {
            showError("Invalid character ID");
        }

        detailReloadIcon.setOnClickListener(v -> {
            hideErrorLayout();
            showLoading();
            loadCharacterDetails(characterId);
        });
    }

    private void loadCharacterDetails(int characterId) {
        Call<Character> call = apiService.getCharacterById(characterId);
        call.enqueue(new retrofit2.Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Character character = response.body();
                    displayCharacterDetails(character);
                } else {
                    showErrorLayout();
                }
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                showErrorLayout();
            }
        });
    }

    private void displayCharacterDetails(Character character) {
        nameTextView.setText(character.getName());
        statusTextView.setText("Status: " + character.getStatus());
        speciesTextView.setText("Species: " + character.getSpecies());
        genderTextView.setText("Gender: " + character.getGender());

        Picasso.get().load(character.getImage()).into(characterImageView, new Callback() {
            @Override
            public void onSuccess() {
                showContent();
            }

            @Override
            public void onError(Exception e) {
                showContent();
                Toast.makeText(DetailActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        hideContent();
        detailErrorLayout.setVisibility(View.GONE);
        detailProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideContent() {
        characterImageView.setVisibility(View.INVISIBLE);
        nameTextView.setVisibility(View.INVISIBLE);
        infoCardView.setVisibility(View.INVISIBLE);
        statusTextView.setVisibility(View.INVISIBLE);
        speciesTextView.setVisibility(View.INVISIBLE);
        genderTextView.setVisibility(View.INVISIBLE);
    }

    private void showContent() {
        detailProgressBar.setVisibility(View.GONE);
        detailErrorLayout.setVisibility(View.GONE);
        characterImageView.setVisibility(View.VISIBLE);
        nameTextView.setVisibility(View.VISIBLE);
        infoCardView.setVisibility(View.VISIBLE);
        statusTextView.setVisibility(View.VISIBLE);
        speciesTextView.setVisibility(View.VISIBLE);
        genderTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorLayout() {
        hideContent();
        detailProgressBar.setVisibility(View.GONE);
        detailErrorLayout.setVisibility(View.VISIBLE);
    }

    private void hideErrorLayout() {
        detailErrorLayout.setVisibility(View.GONE);
    }

    private void showError(String message) {
        detailProgressBar.setVisibility(View.GONE);
        Toast.makeText(DetailActivity.this, message, Toast.LENGTH_LONG).show();
    }
}