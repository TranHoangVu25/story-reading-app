package com.example.finalassigment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Setup Trending RecyclerView
        RecyclerView rvTrending = findViewById(R.id.rvTrending);
        rvTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Story> trendingStories = new ArrayList<>();
        trendingStories.add(new Story(R.drawable.fb, "Lookism", 32400000));
        trendingStories.add(new Story(R.drawable.fb, "Winter Moon", 28800000));
        trendingStories.add(new Story(R.drawable.fb, "The Price Is Your", 2500000));
        trendingStories.add(new Story(R.drawable.fb, "No Longer Co...", 1500000));
        trendingStories.add(new Story(R.drawable.fb, "Everything", 4500000));
        trendingStories.add(new Story(R.drawable.fb, "My Adoption", 3200000));

        StoryAdapter trendingAdapter = new StoryAdapter(trendingStories);
        rvTrending.setAdapter(trendingAdapter);

        // Setup Popular RecyclerView
        RecyclerView rvPopular = findViewById(R.id.rvPopular);
        rvPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Story> popularStories = new ArrayList<>();
        popularStories.add(new Story(R.drawable.fb, "To Whom It", 5000000));
        popularStories.add(new Story(R.drawable.fb, "This Wasn't in", 4800000));
        popularStories.add(new Story(R.drawable.fb, "Slice", 3000000));
        popularStories.add(new Story(R.drawable.fb, "Action", 2700000));
        popularStories.add(new Story(R.drawable.fb, "Comedy", 2200000));
        popularStories.add(new Story(R.drawable.fb, "Drama", 1900000));

        StoryAdapter popularAdapter = new StoryAdapter(popularStories);
        rvPopular.setAdapter(popularAdapter);
    }
}