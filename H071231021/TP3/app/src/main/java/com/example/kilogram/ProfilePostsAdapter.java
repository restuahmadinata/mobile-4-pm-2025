package com.example.kilogram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ProfilePostViewHolder> {

    private List<Post> posts;

    public ProfilePostsAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_post, parent, false);
        return new ProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Load thumbnail with Glide
        Glide.with(holder.itemView.getContext())
                .load(post.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.postImage);

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();

            // Check if this is the current user's post or a dummy post
            if (post.getUsername().equals(UserProfile.getInstance().getUsername())) {
                // Open regular post detail for user's own posts
                Intent intent = new Intent(context, UserPostDetailActivity.class);
                intent.putExtra("post_id", post.getId());
                context.startActivity(intent);
            } else {
                // Open dummy post detail for other users' posts
                Intent intent = new Intent(context, DummyPostDetailActivity.class);
                intent.putExtra("image_url", post.getImageUrl());
                intent.putExtra("username", post.getUsername());
                intent.putExtra("profile_image_url", post.getUserProfileImageUrl());
                intent.putExtra("like_count", post.getLikeCount());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ProfilePostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        public ProfilePostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.profile_post_image);
        }
    }
}