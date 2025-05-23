package com.example.tp4.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.tp4.R;
import com.example.tp4.data.Book;
import com.example.tp4.databinding.FragmentHomeBinding;
import com.example.tp4.repository.BookRepository;
import com.example.tp4.ui.detail.DetailActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment implements BookAdapter.OnBookClickListener {

    // Variables to store the current search query and selected genre
    private String currentQuery = "";
    private String currentGenre = "All Genres";

    // View binding for accessing UI elements
    private FragmentHomeBinding binding;

    // Adapter for managing the RecyclerView data
    private BookAdapter bookAdapter;

    // Repository for accessing book data
    private BookRepository bookRepository;

    // List to store the books displayed in the RecyclerView
    private List<Book> bookList = new ArrayList<>();

    // ExecutorService for running background tasks
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Handler for posting results back to the main thread
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // Flag to track if the fragment is active
    private volatile boolean isFragmentActive = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout and initialize binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFragmentActive = true;

        // Initialize the book repository
        bookRepository = BookRepository.getInstance();

        // Set up RecyclerView, search view, and genre filter
        setupRecyclerView();
        setupSearchView();
        setupGenreFilter();
    }

    private void setupRecyclerView() {
        if (binding == null) return;

        // Configure RecyclerView properties
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load all books and set up the adapter
        bookList = bookRepository.getAllBooks();
        bookAdapter = new BookAdapter(getContext(), bookList, this);
        binding.recyclerView.setAdapter(bookAdapter);

        // Apply fall-down animation to RecyclerView items
        binding.recyclerView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down));
    }

    private void setupSearchView() {
        if (binding == null) return;

        // Set up a listener for search query changes
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No action on query submission
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (binding == null || !isFragmentActive) return true;

                // Update the current query and reset any pending callbacks
                currentQuery = newText;
                mainHandler.removeCallbacksAndMessages(null);

                // Show loading indicator
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);

                // Schedule a filtering operation with a delay
                final Runnable filterOperation = () -> {
                    if (binding == null || !isFragmentActive) return;

                    executorService.execute(() -> {
                        if (!isFragmentActive) return;
                        final List<Book> filteredBooks = performFiltering();

                        mainHandler.post(() -> {
                            if (binding == null || !isFragmentActive) return;
                            updateBookList(filteredBooks);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                        });
                    });
                };

                binding.getRoot().post(() -> {
                    if (binding == null || !isFragmentActive) return;
                    mainHandler.postDelayed(filterOperation, 500);
                });

                return true;
            }
        });
    }

    private void setupGenreFilter() {
        if (binding == null) return;

        // Get all genres and add "All Genres" as the default option
        List<String> genres = bookRepository.getAllGenres();
        genres.add(0, "All Genres");

        // Set up the spinner with the genre list
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenre.setAdapter(genreAdapter);

        // Set up a listener for genre selection changes
        binding.spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding == null || !isFragmentActive) return;

                // Update the current genre and show loading indicator
                currentGenre = parent.getItemAtPosition(position).toString();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);

                // Schedule a filtering operation
                binding.getRoot().post(() -> {
                    if (binding == null || !isFragmentActive) return;

                    mainHandler.postDelayed(() -> {
                        if (binding == null || !isFragmentActive) return;

                        executorService.execute(() -> {
                            if (!isFragmentActive) return;
                            final List<Book> filteredBooks = performFiltering();

                            mainHandler.post(() -> {
                                if (binding == null || !isFragmentActive) return;
                                updateBookList(filteredBooks);
                                binding.recyclerView.setVisibility(View.VISIBLE);
                                binding.progressBar.setVisibility(View.GONE);
                            });
                        });
                    }, 500);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private List<Book> performFiltering() {
        if (!isFragmentActive) return new ArrayList<>();

        // Simulate a delay for testing the loading indicator
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            if (!isFragmentActive) return new ArrayList<>();
        }

        // Filter books by genre
        List<Book> filteredBooks;
        if (!currentGenre.equals("All Genres")) {
            filteredBooks = bookRepository.getBooksByGenre(currentGenre);
        } else {
            filteredBooks = bookRepository.getAllBooks();
        }

        // Further filter books by search query
        if (!currentQuery.isEmpty()) {
            List<Book> searchResults = new ArrayList<>();
            for (Book book : filteredBooks) {
                if (book.getTitle().toLowerCase().contains(currentQuery.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(currentQuery.toLowerCase())) {
                    searchResults.add(book);
                }
            }
            filteredBooks = searchResults;
        }

        return filteredBooks;
    }

    private void updateBookList(List<Book> books) {
        if (binding == null || !isFragmentActive) return;

        // Update the book list and notify the adapter
        bookList.clear();
        bookList.addAll(books);
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookClick(Book book) {
        if (getActivity() == null || !isFragmentActive) return;

        // Open the detail activity for the selected book
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("BOOK_ID", book.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentActive = true;

        if (binding == null) return;

        // Remove any pending callbacks
        mainHandler.removeCallbacksAndMessages(null);

        // Show loading indicator
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);

        // Reload the book list with animation
        binding.getRoot().post(() -> {
            if (binding == null || !isFragmentActive) return;

            executorService.execute(() -> {
                if (!isFragmentActive) return;
                final List<Book> filteredBooks = performFiltering();

                mainHandler.post(() -> {
                    if (binding == null || !isFragmentActive) return;
                    updateBookList(filteredBooks);

                    // Apply fall-down animation
                    binding.recyclerView.setLayoutAnimation(
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down));

                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                });
            });
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentActive = false;
        mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentActive = false;
        mainHandler.removeCallbacksAndMessages(null);
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentActive = false;
        executorService.shutdown();
    }
}