package com.example.kilogram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.HighlightViewHolder> {
    private List<Highlight> highlights;
    private Context context;


    public HighlightAdapter(List<Highlight> highlights, Context context) {
        this.highlights = highlights;
        this.context = context;
    }

    @NonNull
    @Override
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_highlight, parent, false);
        return new HighlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightViewHolder holder, int position) {
        Highlight highlight = highlights.get(position);
        holder.titleTextView.setText(highlight.getTitle());

        Glide.with(context)
                .load(highlight.getCoverImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.coverImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HighlightDetailActivity.class);
            intent.putExtra("highlight_id", highlight.getId());
            intent.putExtra("highlight_title", highlight.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return highlights.size();
    }

    static class HighlightViewHolder extends RecyclerView.ViewHolder {
        CircleImageView coverImageView;
        TextView titleTextView;

        public HighlightViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.highlight_image);
            titleTextView = itemView.findViewById(R.id.highlight_name);
        }
    }
}