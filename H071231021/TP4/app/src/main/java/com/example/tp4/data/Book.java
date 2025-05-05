package com.example.tp4.data;
import java.util.UUID;

public class Book {
    private String id;
    private String title;
    private String author;
    private String year;
    private String blurb;
    private String coverUri;
    private boolean isLiked;

    // Tambahan Nilai tentu saja
    private String genre;
    private float rating;
    private String review;

    public Book(String title, String author, String year, String blurb,
                String coverUri, String genre, float rating, String review) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.year = year;
        this.blurb = blurb;
        this.coverUri = coverUri;
        this.genre = genre;
        this.rating = rating;
        this.review = review;
        this.isLiked = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }


    public String getYear() {
        return year;
    }


    public String getBlurb() {
        return blurb;
    }


    public String getCoverUri() {
        return coverUri;
    }


    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getGenre() {
        return genre;
    }


    public float getRating() {
        return rating;
    }


    public String getReview() {
        return review;
    }

}
