package com.example.kilogram;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Set user details
        holder.username.setText(post.getUsername());

        // Load profile image
        Glide.with(holder.itemView.getContext())
                .load(post.getUserProfileImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.profileImage);

        // Load post image
        Glide.with(holder.itemView.getContext())
                .load(post.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.postImage);

        // Set post details
        holder.likes.setText(String.valueOf(post.getLikeCount()));
        holder.comments.setText(String.valueOf(post.getCommentCount()));
        holder.shares.setText(String.valueOf(post.getShareCount()));
        holder.caption.setText(post.getUsername() + " " + post.getCaption());
        holder.timePosted.setText(post.getTimePosted());

        // Set like button state
        updateLikeButton(holder, post);

        // Set click listeners
        holder.likeButton.setOnClickListener(v -> {
            post.toggleLike();
            updateLikeButton(holder, post);
            holder.likes.setText(String.valueOf(post.getLikeCount()));
        });

        // Navigate to profile when username or profile image is clicked
        View.OnClickListener profileClickListener = v -> {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            intent.putExtra("username", post.getUsername());
            intent.putExtra("profileImageUrl", post.getUserProfileImageUrl());
            intent.putExtra("postId", post.getId());
            v.getContext().startActivity(intent);
        };

        holder.profileImage.setOnClickListener(profileClickListener);
        holder.username.setOnClickListener(profileClickListener);
    }

    private void updateLikeButton(PostViewHolder holder, Post post) {
        if (post.isLiked()) {
            holder.likeButton.setImageResource(R.drawable.notifications); // Replace with liked image
        } else {
            holder.likeButton.setImageResource(R.drawable.notifications); // Replace with not liked image
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView username;
        ImageView postImage;
        ImageButton likeButton;
        ImageButton commentButton;
        ImageButton shareButton;
        ImageButton saveButton;
        TextView likes;
        TextView comments;
        TextView shares;
        TextView caption;
        TextView timePosted;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.post_profile_image);
            username = itemView.findViewById(R.id.post_username);
            postImage = itemView.findViewById(R.id.post_image);
            likeButton = itemView.findViewById(R.id.post_like_button);
            commentButton = itemView.findViewById(R.id.post_comment_button);
            shareButton = itemView.findViewById(R.id.post_share_button);
            saveButton = itemView.findViewById(R.id.post_save_button);
            likes = itemView.findViewById(R.id.post_likes);
            comments = itemView.findViewById(R.id.post_comment);
            shares = itemView.findViewById(R.id.post_share);
            caption = itemView.findViewById(R.id.post_caption);
            timePosted = itemView.findViewById(R.id.post_time);
        }
    }
}