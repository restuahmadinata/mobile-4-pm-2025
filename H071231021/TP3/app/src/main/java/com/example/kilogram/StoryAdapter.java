package com.example.kilogram;

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

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private List<Story> stories;

    public StoryAdapter(List<Story> stories) {
        this.stories = stories;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.username.setText(story.getUsername());

        Glide.with(holder.itemView.getContext())
                .load(story.getProfileImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.storyImage);

        if (story.isHasUnseenStory()) {
            holder.storyImage.setBorderColor(holder.itemView.getContext().getResources().getColor(R.color.purple_500));
            holder.storyImage.setBorderWidth(4);
        } else {
            holder.storyImage.setBorderWidth(0);
        }

        holder.itemView.setOnClickListener(v -> {
            // Open StoryViewActivity when story is clicked
            Intent intent = new Intent(v.getContext(), StoryViewActivity.class);
            intent.putExtra("profileImageUrl", story.getProfileImageUrl());
            intent.putExtra("username", story.getUsername());
            v.getContext().startActivity(intent);

            // Mark story as seen
            story.setHasUnseenStory(false);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        CircleImageView storyImage;
        TextView username;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.story_image);
            username = itemView.findViewById(R.id.story_username);
        }
    }
}