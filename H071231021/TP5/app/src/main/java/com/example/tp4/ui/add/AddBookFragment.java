// app/src/main/java/com/example/tp4/ui/add/AddBookFragment.java
package com.example.tp4.ui.add;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tp4.data.Book;
import com.example.tp4.databinding.FragmentAddBookBinding;
import com.example.tp4.repository.BookRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddBookFragment extends Fragment {

    private FragmentAddBookBinding binding;
    private BookRepository bookRepository;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookRepository = BookRepository.getInstance();
        setupGenreSpinner();
        setupImagePicker();
        setupSaveButton();
    }

    private void setupGenreSpinner() {
        // Get existing genres + add some common ones
        List<String> existingGenres = bookRepository.getAllGenres();
        List<String> genres = new ArrayList<>(Arrays.asList(
                "Fantasy", "Science Fiction", "Mystery", "Thriller", "Romance",
                "Non-fiction", "Biography", "History", "Self-help", "Horror",
                "Adventure", "Classic", "Young Adult", "Children", "Poetry",
                "Dystopian", "Comic", "Manga", "Philosophy"
        ));

        // Add existing genres that aren't in our predefined list
        for (String genre : existingGenres) {
            if (!genres.contains(genre)) {
                genres.add(genre);
            }
        }

        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenre.setAdapter(genreAdapter);
    }

    private void setupImagePicker() {
        binding.btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Book Cover"), PICK_IMAGE_REQUEST);
        });
    }

    private void setupSaveButton() {
        binding.btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                saveBook();
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Check title
        String title = binding.etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            binding.tilTitle.setError("Title is required");
            isValid = false;
        } else {
            binding.tilTitle.setError(null);
        }

        // Check author
        String author = binding.etAuthor.getText().toString().trim();
        if (author.isEmpty()) {
            binding.tilAuthor.setError("Author is required");
            isValid = false;
        } else {
            binding.tilAuthor.setError(null);
        }

        // Check year
        String year = binding.etYear.getText().toString().trim();
        if (year.isEmpty()) {
            binding.tilYear.setError("Year is required");
            isValid = false;
        } else {
            binding.tilYear.setError(null);
        }

        // Check description
        String blurb = binding.etBlurb.getText().toString().trim();
        if (blurb.isEmpty()) {
            binding.tilBlurb.setError("Description is required");
            isValid = false;
        } else {
            binding.tilBlurb.setError(null);
        }

        // Review and image are optional

        return isValid;
    }

    private void saveBook() {
        // Get data from form
        String title = binding.etTitle.getText().toString().trim();
        String author = binding.etAuthor.getText().toString().trim();
        String year = binding.etYear.getText().toString().trim();
        String blurb = binding.etBlurb.getText().toString().trim();
        String genre = binding.spinnerGenre.getSelectedItem().toString();
        float rating = binding.ratingBar.getRating();
        String review = binding.etReview.getText().toString().trim();

        // Use selected image or default
        String coverUri = (selectedImageUri != null)
                ? selectedImageUri.toString()
                : "android.resource://com.example.tp4/drawable/sample_cover";

        // Create new book
        Book newBook = new Book(title, author, year, blurb, coverUri, genre, rating, review);

        // Add to repository
        bookRepository.addBook(newBook);

        // Show success message
        Toast.makeText(getContext(), "Book added successfully", Toast.LENGTH_SHORT).show();

        // Clear form
        clearForm();

        // Switch to home fragment to see the new book
        if (getActivity() != null) {
            getActivity().findViewById(com.example.tp4.R.id.navigation_home).performClick();
        }
    }

    private void clearForm() {
        binding.etTitle.setText("");
        binding.etAuthor.setText("");
        binding.etYear.setText("");
        binding.etBlurb.setText("");
        binding.etReview.setText("");
        binding.ratingBar.setRating(0);
        binding.spinnerGenre.setSelection(0);
        binding.ivBookCover.setImageResource(android.R.drawable.ic_menu_gallery);
        selectedImageUri = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.ivBookCover.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}