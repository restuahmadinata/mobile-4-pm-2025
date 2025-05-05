// app/src/main/java/com/example/tp4/ui/home/HomeFragment.java
package com.example.tp4.ui.home;

import android.content.Intent;
import android.os.Bundle;
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

public class HomeFragment extends Fragment implements BookAdapter.OnBookClickListener {

    // Add to HomeFragment.java at the class level
    private String currentQuery = "";
    private String currentGenre = "All Genres";

    private FragmentHomeBinding binding;
    private BookAdapter bookAdapter;
    private BookRepository bookRepository;
    private List<Book> bookList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookRepository = BookRepository.getInstance();
        setupRecyclerView();
        setupSearchView();
        setupGenreFilter();
    }

    private void setupRecyclerView() {
        // Set fixed size if content layout size doesn't change
        binding.recyclerView.setHasFixedSize(true);

        // Enable view caching
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Setup layout manager and adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookList = bookRepository.getAllBooks();
        bookAdapter = new BookAdapter(getContext(), bookList, this);
        binding.recyclerView.setAdapter(bookAdapter);

        // Add after setting adapter
        binding.recyclerView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(
                        getContext(), R.anim.layout_animation_fall_down));
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText;
                applyFilters();
                return true;
            }
        });
    }

    private void setupGenreFilter() {
        List<String> genres = bookRepository.getAllGenres();
        genres.add(0, "All Genres");

        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenre.setAdapter(genreAdapter);

        binding.spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentGenre = parent.getItemAtPosition(position).toString();
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void applyFilters() {
        List<Book> filteredBooks = bookRepository.getAllBooks();

        // First filter by genre if needed
        if (!currentGenre.equals("All Genres")) {
            filteredBooks = bookRepository.getBooksByGenre(currentGenre);
        }

        // Then filter by search query if needed
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

        updateBookList(filteredBooks);
    }

    private void updateBookList(List<Book> books) {
        bookList.clear();
        bookList.addAll(books);
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookClick(Book book) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("BOOK_ID", book.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        applyFilters();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}