package com.example.tp4.ui.detail;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tp4.R;
import com.example.tp4.data.Book;
import com.example.tp4.databinding.ActivityDetailBinding;
import com.example.tp4.repository.BookRepository;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private BookRepository bookRepository;
    private Book book;
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bookRepository = BookRepository.getInstance();

        // Get book ID from intent
        bookId = getIntent().getStringExtra("BOOK_ID");
        if (bookId == null) {
            finish(); // Close activity if no book ID
            return;
        }

        // Find the book in repository
        List<Book> allBooks = bookRepository.getAllBooks();
        for (Book b : allBooks) {
            if (b.getId().equals(bookId)) {
                book = b;
                break;
            }
        }

        if (book == null) {
            finish(); // Close activity if book not found
            return;
        }

        displayBookDetails();
        setupFavoriteButton();
    }

    private void displayBookDetails() {
        // Set text fields
        binding.tvTitle.setText(book.getTitle());
        binding.tvAuthor.setText(book.getAuthor());
        binding.tvYear.setText(book.getYear());
        binding.tvGenre.setText(book.getGenre());
        binding.tvBlurb.setText(book.getBlurb());
        binding.tvReview.setText(book.getReview());

        // Set rating
        binding.ratingBar.setRating(book.getRating());
        binding.tvRatingValue.setText(String.format("%.1f/5", book.getRating()));

        // Set cover image
        try {
            binding.ivBookCover.setImageURI(Uri.parse(book.getCoverUri()));
        } catch (Exception e) {
            binding.ivBookCover.setVisibility(View.GONE);
        }

    }


    private void setupFavoriteButton() {
        // Initialize icon on load
        updateFavoriteButtonIcon();

        binding.btnFavorite.setOnClickListener(v -> {
            // Toggle state in repository
            bookRepository.toggleLike(book.getId());

            // Get the CURRENT state from repository (not toggle again!)
            for (Book b : bookRepository.getAllBooks()) {
                if (b.getId().equals(book.getId())) {
                    // Update local book reference with repository value
                    book.setLiked(b.isLiked());
                    break;
                }
            }

            // Force immediate visual update
            updateFavoriteButtonIcon();

            // Animation
            binding.btnFavorite.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(150)
                    .withEndAction(() -> {
                        binding.btnFavorite.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(150)
                                .start();
                    })
                    .start();
        });
    }

    private void updateFavoriteButtonIcon() {
        // Log the current state
        System.out.println("Updating icon, isLiked: " + book.isLiked());

        if (book.isLiked()) {
            binding.btnFavorite.setImageResource(R.drawable.favorite_on);
        } else {
            binding.btnFavorite.setImageResource(R.drawable.favorite_off);
        }

        // Optional: add a color tint based on state
        binding.btnFavorite.setColorFilter(
                book.isLiked() ?
                        getResources().getColor(R.color.favorite_active, getTheme()) :
                        getResources().getColor(R.color.favorite_inactive, getTheme())
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back when the back button is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}