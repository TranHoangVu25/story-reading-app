package com.example.finalassigment;

import android.content.ClipData;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Thiết lập RecyclerView cũ
        setupTrending();
        setupPopular();

        // Thiết lập Bottom Navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        // Hiển thị ForYouFragment khi mở app
        if (savedInstanceState == null) {
            loadFragment(new ForYouFragment());
        }
        // Sử dụng listener kiểu cũ để tránh lỗi "constant expression required"
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();
                if (id == R.id.nav_for_you) {
                    selectedFragment = new ForYouFragment();
                } else if (id == R.id.nav_my) {
                    selectedFragment = new MyFragment();
                } else if (id == R.id.nav_more) {
                    selectedFragment = new MoreFragment();
                }
                return loadFragment(selectedFragment);
            }
        });
    }

    private void setupTrending() {
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
    }

    private void setupPopular() {
        RecyclerView rvPopular = findViewById(R.id.rvPopular);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvPopular.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        rvPopular.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true));

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

    private boolean loadFragment(Fragment fragment) {
        if (fragment == null) return false;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)  // ← dùng container mới
                .commit();
        return true;
    }
}
