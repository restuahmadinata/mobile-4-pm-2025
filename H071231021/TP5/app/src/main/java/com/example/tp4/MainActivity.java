package com.example.tp4;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.tp4.databinding.ActivityMainBinding;
import com.example.tp4.ui.add.AddBookFragment;
import com.example.tp4.ui.favorites.FavoritesFragment;
import com.example.tp4.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    // View binding for accessing UI elements in the activity layout
    private ActivityMainBinding binding;

    // Variable to track the current tab position (0=Home, 1=Favorites, 2=Add)
    private int currentTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding to connect the layout XML with the activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply window insets (e.g., system bars) to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the bottom navigation bar and handle item selection
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null; // Fragment to display
                int newPosition = 0; // New tab position

                // Determine which fragment to load based on the selected menu item
                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                    newPosition = 0; // Home tab
                } else if (item.getItemId() == R.id.navigation_favorites) {
                    selectedFragment = new FavoritesFragment();
                    newPosition = 1; // Favorites tab
                } else if (item.getItemId() == R.id.navigation_add) {
                    selectedFragment = new AddBookFragment();
                    newPosition = 2; // Add tab
                }

                // If a fragment is selected, replace the current fragment with the new one
                if (selectedFragment != null) {
                    // Determine the animation direction based on tab navigation
                    boolean isMovingRight = newPosition > currentTabPosition;

                    if (isMovingRight) {
                        // Moving to the right: Slide current fragment out to the left, new fragment in from the right
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in_right,  // New fragment enters from the right
                                        R.anim.slide_out_left   // Current fragment exits to the left
                                )
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    } else {
                        // Moving to the left: Slide current fragment out to the right, new fragment in from the left
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in_left,   // New fragment enters from the left
                                        R.anim.slide_out_right  // Current fragment exits to the right
                                )
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }

                    // Update the current tab position
                    currentTabPosition = newPosition;
                    return true; // Indicate that the item selection was handled
                }
                return false; // Indicate that the item selection was not handled
            }
        });

        // Set the default fragment to the HomeFragment when the activity is first created
        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
            currentTabPosition = 0; // Default to the Home tab
        }
    }
}