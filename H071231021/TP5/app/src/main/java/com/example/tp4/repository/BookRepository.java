package com.example.tp4.repository;

import com.example.tp4.data.Book;
import com.example.tp4.utils.DummyData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository {
    private static BookRepository instance;
    private List<Book> books;

    private BookRepository() {
        books = new ArrayList<>();
        // Load dummy data
        books.addAll(DummyData.getBooks());
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public List<Book> getAllBooks() {
        // Sort by newest (year) first
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return b2.getYear().compareTo(b1.getYear());
            }
        });
        return new ArrayList<>(books);
    }

    public List<Book> getBooksByGenre(String genre) {
        return books.stream()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void toggleLike(String bookId) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                book.setLiked(!book.isLiked());
                break;
            }
        }
    }

    public List<String> getAllGenres() {
        return books.stream()
                .map(Book::getGenre)
                .distinct()
                .collect(Collectors.toList());
    }
}