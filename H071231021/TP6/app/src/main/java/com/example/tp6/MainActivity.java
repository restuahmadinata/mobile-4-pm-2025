package com.example.tp6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView recyclerView;
    private CharacterAdapter characterAdapter;
    private Button loadMoreButton;
    private View progressBar;
    private View errorLayout;
    private ImageView reloadIcon;
    private int currentPage = 1;
    private int totalPages = 1;
    private List<Character> characterList = new ArrayList<>();
    private boolean isInitialLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerView = findViewById(R.id.recyclerView);
        loadMoreButton = findViewById(R.id.loadMoreButton);
        progressBar = findViewById(R.id.progressBar);
        errorLayout = findViewById(R.id.errorLayout);
        reloadIcon = findViewById(R.id.reloadIcon);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        characterAdapter = new CharacterAdapter(characterList);
        recyclerView.setAdapter(characterAdapter);

        loadCharacters();


        loadMoreButton.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadMoreCharacters();
            } else {
                Toast.makeText(MainActivity.this, "No more characters to load", Toast.LENGTH_SHORT).show();
                loadMoreButton.setEnabled(false);
            }
        });

        reloadIcon.setOnClickListener(v -> {
            hideErrorLayout();
            if (isInitialLoad) {
                loadCharacters();
            } else {
                loadMoreCharacters();
            }
        });
    }

    private void loadCharacters() {
        showLoading();
        Call<CharacterResponse> call = apiService.getCharacters(currentPage);
        call.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    characterList.clear();
                    characterList.addAll(response.body().getResults());
                    characterAdapter.notifyDataSetChanged();

                    totalPages = response.body().getInfo().getPages();
                    updateLoadMoreButton();

                    Toast.makeText(MainActivity.this, "Loaded page " + currentPage, Toast.LENGTH_SHORT).show();
                } else {
                    showErrorLayout();
                }
            }

            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                hideLoading();
                showErrorLayout();
            }
        });
    }

    private void loadMoreCharacters() {
        loadMoreButton.setEnabled(false);
        findViewById(R.id.loadMoreProgressBar).setVisibility(View.VISIBLE);
        isInitialLoad = false;

        Call<CharacterResponse> call = apiService.getCharacters(currentPage);
        call.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {
                findViewById(R.id.loadMoreProgressBar).setVisibility(View.GONE);
                loadMoreButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    List<Character> newCharacters = response.body().getResults();
                    characterAdapter.addCharacters(newCharacters);
                    updateLoadMoreButton();
                    Toast.makeText(MainActivity.this, "Loaded page " + currentPage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load more data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                findViewById(R.id.loadMoreProgressBar).setVisibility(View.GONE);
                loadMoreButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_LONG).show();
                currentPage--;
            }
        });
    }

    private void showLoading() {
        recyclerView.setVisibility(View.GONE);
        loadMoreButton.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        recyclerView.setVisibility(View.VISIBLE);
        loadMoreButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showErrorLayout() {
        recyclerView.setVisibility(View.GONE);
        loadMoreButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void hideErrorLayout() {
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        loadMoreButton.setVisibility(View.VISIBLE);
    }

    private void updateLoadMoreButton() {
        loadMoreButton.setEnabled(currentPage < totalPages);
        if (currentPage >= totalPages) {
            loadMoreButton.setText("Congrats, you've reached the depths!");
        } else {
            loadMoreButton.setText("Load more");
        }
    }
}