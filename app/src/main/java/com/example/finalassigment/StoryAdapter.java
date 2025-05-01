package com.example.finalassigment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private final List<Story> stories;

    public StoryAdapter(List<Story> stories) {
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.title.setText(story.getTitle());
        holder.views.setText(String.format("%dM Views", story.getViews() / 1000000));

        // For image loading (using Glide)
        holder.image.setImageResource(story.getImageRes());
        // Glide.with(holder.itemView.getContext()).load(story.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return Math.min(stories.size(), 6); // Limit to max 6 items
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView views;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivStory);
            title = itemView.findViewById(R.id.tvTitle);
            views = itemView.findViewById(R.id.tvViews);
        }
    }
}