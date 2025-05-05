package com.example.kilogram;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class HighlightDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TextView titleTextView;
    private String highlightId;
    private String highlightTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.highlight_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        viewPager = findViewById(R.id.highlight_view_pager);
        titleTextView = findViewById(R.id.highlight_title_text);
        ImageButton closeButton = findViewById(R.id.close_button);

        // Get highlight info from intent
        highlightId = getIntent().getStringExtra("highlight_id");
        highlightTitle = getIntent().getStringExtra("highlight_title");

        titleTextView.setText(highlightTitle);

        // Set up close button
        closeButton.setOnClickListener(v -> finish());

        // Load highlight content
        loadHighlightContent();
    }

    private void loadHighlightContent() {
        // Get the highlight media from HighlightDataProvider
        List<String> mediaUrls = new ArrayList<>();

        for (Highlight highlight : HighlightDataProvider.getUserHighlights(UserProfile.getInstance().getUserId())) {
            if (highlight.getId().equals(highlightId)) {
                mediaUrls = highlight.getMediaUrls();
                break;
            }
        }

        // Set up ViewPager with images
        HighlightMediaAdapter adapter = new HighlightMediaAdapter(this, mediaUrls);
        viewPager.setAdapter(adapter);
    }
}