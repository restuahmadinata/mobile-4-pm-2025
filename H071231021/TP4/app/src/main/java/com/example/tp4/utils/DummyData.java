package com.example.tp4.utils;

import com.example.tp4.data.Book;

import java.util.ArrayList;
import java.util.List;

public class DummyData {
    public static List<Book> getBooks() {
        List<Book> books = new ArrayList<>();

        // Default cover for all books
        String defaultCoverUri = "android.resource://com.example.tp4/drawable/sample_cover";
        String baseUri = "android.resource://com.example.tp4/drawable/";


        books.add(new Book(
                "To Kill a Mockingbird",
                "Harper Lee",
                "1960",
                "The story of racial injustice and the loss of innocence in the American South through the eyes of a young girl.",
                baseUri + "book1",
                "Classic",
                4.8f,
                "A timeless exploration of human behavior and moral growth."
        ));

        books.add(new Book(
                "1984",
                "George Orwell",
                "1949",
                "A dystopian novel set in a totalitarian society where Big Brother is always watching.",
                baseUri + "book2",
                "Dystopian",
                4.7f,
                "A chilling portrayal of government surveillance and manipulation."
        ));

        books.add(new Book(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "1925",
                "The story of eccentric millionaire Jay Gatsby and his obsession with the beautiful Daisy Buchanan.",
                baseUri + "book3",
                "Classic",
                4.5f,
                "A vivid portrayal of the Roaring Twenties and the American Dream."
        ));

        books.add(new Book(
                "Dune",
                "Frank Herbert",
                "1965",
                "Set in the distant future, the novel tells the story of young Paul Atreides, whose family accepts control of the desert planet Arrakis.",
                baseUri + "book4",
                "Science Fiction",
                4.9f,
                "A masterpiece of worldbuilding and political intrigue."
        ));

        books.add(new Book(
                "The Hobbit",
                "J.R.R. Tolkien",
                "1937",
                "The tale of Bilbo Baggins, who is convinced by the wizard Gandalf to accompany thirteen dwarves on a quest to reclaim the Lonely Mountain.",
                baseUri + "book5",
                "Fantasy",
                4.6f,
                "An enchanting prelude to The Lord of the Rings."
        ));

        books.add(new Book(
                "Pride and Prejudice",
                "Jane Austen",
                "1813",
                "The story follows Elizabeth Bennet as she deals with issues of manners, upbringing, morality, and marriage in the society of the landed gentry of early 19th-century England.",
                baseUri + "book6",
                "Romance",
                4.7f,
                "A witty exploration of social class and personal growth."
        ));

        books.add(new Book(
                "The Catcher in the Rye",
                "J.D. Salinger",
                "1951",
                "The story of Holden Caulfield, a teenage boy dealing with alienation and loss in New York City.",
                baseUri + "book7",
                "Coming of Age",
                4.3f,
                "A classic portrayal of teenage angst and rebellion."
        ));

        books.add(new Book(
                "Sapiens: A Brief History of Humankind",
                "Yuval Noah Harari",
                "2011",
                "A survey of the history of humankind from the evolution of archaic human species to the 21st century.",
                baseUri + "book8",
                "Non-fiction",
                4.7f,
                "A thought-provoking exploration of human history and potential future."
        ));

        books.add(new Book(
                "The Hunger Games",
                "Suzanne Collins",
                "2008",
                "In a dystopian future, teenagers are selected to fight to the death in an annual televised event.",
                baseUri + "book9",
                "Young Adult",
                4.5f,
                "A gripping tale of survival and rebellion."
        ));

        books.add(new Book(
                "The Alchemist",
                "Paulo Coelho",
                "1988",
                "A philosophical novel about a young Andalusian shepherd named Santiago who dreams of finding a worldly treasure.",
                baseUri + "book10",
                "Philosophy",
                4.6f,
                "A spiritual journey that inspires readers to follow their dreams."
        ));

        books.add(new Book(
                "Harry Potter and the Philosopher's Stone",
                "J.K. Rowling",
                "1997",
                "The first novel in the Harry Potter series, following a young wizard's adventures at Hogwarts School of Witchcraft and Wizardry.",
                baseUri + "book11",
                "Fantasy",
                4.8f,
                "The beginning of a magical journey that captivated a generation."
        ));

        books.add(new Book(
                "The Da Vinci Code",
                "Dan Brown",
                "2003",
                "A mystery thriller novel that follows symbologist Robert Langdon as he investigates a murder in the Louvre Museum.",
                baseUri + "book12",
                "Mystery",
                4.2f,
                "A fast-paced thriller that combines art, history, and conspiracy."
        ));

        books.add(new Book(
                "Atomic Habits",
                "James Clear",
                "2018",
                "An easy and proven way to build good habits and break bad ones.",
                baseUri + "book13",
                "Self-help",
                4.9f,
                "A practical guide to making small changes for remarkable results."
        ));

        books.add(new Book(
                "The Silent Patient",
                "Alex Michaelides",
                "2019",
                "A psychological thriller about a woman who shoots her husband and then refuses to speak.",
                baseUri + "book14",
                "Thriller",
                4.5f,
                "A shocking psychological thriller with a twist you won't see coming."
        ));

        books.add(new Book(
                "Educated",
                "Tara Westover",
                "2018",
                "A memoir about a woman who was kept out of school as a child but went on to earn a PhD from Cambridge University.",
                baseUri + "book15",
                "Biography",
                4.7f,
                "A powerful testament to self-invention and the power of education."
        ));

        return books;
    }
}