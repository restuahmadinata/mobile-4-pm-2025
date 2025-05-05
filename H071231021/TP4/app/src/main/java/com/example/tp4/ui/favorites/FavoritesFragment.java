package com.example.tp4.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.tp4.data.Book;
import com.example.tp4.databinding.FragmentFavoritesBinding;
import com.example.tp4.repository.BookRepository;
import com.example.tp4.ui.detail.DetailActivity;
import com.example.tp4.ui.home.BookAdapter;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements BookAdapter.OnBookClickListener {

    private FragmentFavoritesBinding binding;
    private BookAdapter bookAdapter;
    private List<Book> favoriteBooks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update favorites when fragment becomes visible
        updateFavoritesList();
    }

    private void setupRecyclerView() {
        bookAdapter = new BookAdapter(getContext(), favoriteBooks, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);
        updateFavoritesList();
    }

    private void updateFavoritesList() {
        favoriteBooks.clear();
        BookRepository repository = BookRepository.getInstance();
        for (Book book : repository.getAllBooks()) {
            if (book.isLiked()) {
                favoriteBooks.add(book);
            }
        }

        // Show empty message when no favorites
        if (favoriteBooks.isEmpty()) {
            binding.tvEmptyFavorites.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.tvEmptyFavorites.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }

        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookClick(Book book) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("BOOK_ID", book.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}