package com.example.tp4.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tp4.R;
import com.example.tp4.data.Book;
import com.example.tp4.databinding.FragmentFavoritesBinding;
import com.example.tp4.repository.BookRepository;
import com.example.tp4.ui.detail.DetailActivity;
import com.example.tp4.ui.home.BookAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FavoritesFragment extends Fragment implements BookAdapter.OnBookClickListener {

    // Binding to connect the layout XML with the fragment
    private FragmentFavoritesBinding binding;

    // Adapter to manage the data in the RecyclerView
    private BookAdapter bookAdapter;

    // List to store favorite books to be displayed
    private List<Book> favoriteBooks = new ArrayList<>();

    // ExecutorService for running background tasks
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Handler to post results from background threads to the main thread
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // Flag to ensure the fragment is still active
    private volatile boolean isFragmentActive = true;

    // Future to track the currently running background task
    private Future<?> loadingTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize binding to connect the layout XML with the fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFragmentActive = true;

        // Set up the RecyclerView to display favorite books
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        if (binding == null) return;

        // Optimize RecyclerView for better performance
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Apply "fall down" animation to RecyclerView items
        binding.recyclerView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down));

        // Set up the adapter and layout manager for the RecyclerView
        bookAdapter = new BookAdapter(getContext(), favoriteBooks, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentActive = true;

        // Load favorite books in the background
        loadFavoritesInBackground();
    }

    private void loadFavoritesInBackground() {
        if (binding == null || !isFragmentActive) return;

        // Cancel the previous task if it is still running
        if (loadingTask != null && !loadingTask.isDone()) {
            loadingTask.cancel(true);
        }

        // Show loading state
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.tvEmptyFavorites.setVisibility(View.GONE);

        // Run a task to load favorite books in the background
        loadingTask = executorService.submit(() -> {
            if (!isFragmentActive) return;

            // Fetch favorite books from the repository
            List<Book> favorites = new ArrayList<>();
            BookRepository repository = BookRepository.getInstance();
            for (Book book : repository.getAllBooks()) {
                if (book.isLiked()) {
                    favorites.add(book);
                }
            }

            // Add artificial delay for testing purposes
            try {
                Thread.sleep(1500);
                if (!isFragmentActive) return;
            } catch (InterruptedException e) {
                return; // Task was canceled
            }

            // Update the UI on the main thread
            mainHandler.post(() -> {
                if (binding == null || !isFragmentActive) return;

                // Update the list of favorite books
                favoriteBooks.clear();
                favoriteBooks.addAll(favorites);

                if (favoriteBooks.isEmpty()) {
                    // Show a message if there are no favorite books
                    binding.tvEmptyFavorites.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyFavorites.setVisibility(View.GONE);

                    // Reapply the animation before showing the RecyclerView
                    binding.recyclerView.setLayoutAnimation(
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down));

                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                // Notify the adapter that the data has changed
                bookAdapter.notifyDataSetChanged();

                // Hide the loading indicator
                binding.progressBar.setVisibility(View.GONE);
            });
        });
    }

    @Override
    public void onBookClick(Book book) {
        if (getActivity() == null || !isFragmentActive) return;

        // Open the DetailActivity for the selected book
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("BOOK_ID", book.getId());
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentActive = false;

        // Remove all pending callbacks
        mainHandler.removeCallbacksAndMessages(null);

        // Cancel the background task if it is still running
        if (loadingTask != null) {
            loadingTask.cancel(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentActive = false;

        // Clean up binding and cancel callbacks
        mainHandler.removeCallbacksAndMessages(null);
        if (loadingTask != null) {
            loadingTask.cancel(true);
        }
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentActive = false;

        // Shut down the ExecutorService
        executorService.shutdownNow();
    }
}