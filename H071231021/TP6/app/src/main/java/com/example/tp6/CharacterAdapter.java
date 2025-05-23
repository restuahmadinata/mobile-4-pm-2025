package com.example.tp6;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private List<Character> characterList;

    public CharacterAdapter(List<Character> characterList) {
        this.characterList = characterList;
    }

    public void addCharacters(List<Character> newCharacters) {
        int startPosition = characterList.size();
        characterList.addAll(newCharacters);
        notifyItemRangeInserted(startPosition, newCharacters.size());
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character character = characterList.get(position);
        holder.bind(character);
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarImageView;
        private TextView nameTextView;
        private TextView speciesTextView;
        private TextView statusView;
        private Character character;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            speciesTextView = itemView.findViewById(R.id.speciesTextView);
            statusView = itemView.findViewById(R.id.statusView);


            itemView.setOnClickListener(v -> {
                if (character != null) {
                    openDetailActivity(v.getContext());
                }
            });

            avatarImageView.setOnClickListener(v -> {
                if (character != null) {
                    openDetailActivity(v.getContext());
                }
            });
        }

        private void openDetailActivity(Context context) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("character_id", character.getId());
            context.startActivity(intent);
        }

        public void bind(Character character) {
            this.character = character;
            Picasso.get().load(character.getImage()).into(avatarImageView);
            nameTextView.setText(character.getName());
            speciesTextView.setText(character.getSpecies());

            statusView.setText(character.getStatus());

            if ("Alive".equalsIgnoreCase(character.getStatus())) {
                statusView.getBackground().setTint(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else if ("Dead".equalsIgnoreCase(character.getStatus())) {
                statusView.getBackground().setTint(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            } else {
                statusView.getBackground().setTint(itemView.getContext().getResources().getColor(android.R.color.darker_gray));
            }
        }
    }
}