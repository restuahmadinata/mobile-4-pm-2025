package com.example.tp4.ui.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tp4.R;
import com.example.tp4.data.Book;
import com.example.tp4.databinding.ItemBookBinding;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private final List<Book> books;
    private final OnBookClickListener listener;

    public BookAdapter(Context context, List<Book> books, OnBookClickListener listener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);

        // Use Glide for efficient image loading and performance boost
        Glide.with(context)
                .load(Uri.parse(book.getCoverUri()))
                .placeholder(R.drawable.sample_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivBookCover);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public final ItemBookBinding binding;

        public BookViewHolder(ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(Book book) {
            binding.tvBookTitle.setText(book.getTitle());
            binding.tvBookAuthor.setText(book.getAuthor());
            binding.tvBookGenre.setText(book.getGenre());
            binding.ratingBar.setRating(book.getRating());

            // Update to use custom favorite icons
            binding.ivFavorite.setImageResource(
                    book.isLiked() ? R.drawable.favorite_on : R.drawable.favorite_off
            );

            binding.ivFavorite.setColorFilter(
                    book.isLiked() ?
                            binding.getRoot().getContext().getColor(R.color.item_favorite_active) :
                            binding.getRoot().getContext().getColor(R.color.item_favorite_inactive)
            );

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });
        }
    }

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }
}