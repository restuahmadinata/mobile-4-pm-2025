package com.example.kilogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighlightDataProvider {
    public static List<Highlight> getUserHighlights(String userId) {
        // In a real app, you would fetch this data from a database or API
        List<Highlight> highlights = new ArrayList<>();

        highlights.add(new Highlight(
                "h1",
                "Travel",
                "https://picsum.photos/id/1/200",
                Arrays.asList(
                        "https://picsum.photos/id/10/500",
                        "https://picsum.photos/id/11/500",
                        "https://picsum.photos/id/12/500"
                )
        ));

        highlights.add(new Highlight(
                "h2",
                "Food",
                "https://picsum.photos/id/20/200",
                Arrays.asList(
                        "https://picsum.photos/id/21/500",
                        "https://picsum.photos/id/22/500"
                )
        ));

        highlights.add(new Highlight(
                "h3",
                "Music",
                "https://picsum.photos/id/30/200",
                Arrays.asList(
                        "https://picsum.photos/id/31/500",
                        "https://picsum.photos/id/32/500",
                        "https://picsum.photos/id/33/500"
                )
        ));

        highlights.add(new Highlight(
                "h4",
                "Friends",
                "https://picsum.photos/id/40/200",
                Arrays.asList(
                        "https://picsum.photos/id/41/500",
                        "https://picsum.photos/id/42/500"
                )
        ));

        highlights.add(new Highlight(
                "h5",
                "Pets",
                "https://picsum.photos/id/50/200",
                Arrays.asList(
                        "https://picsum.photos/id/51/500",
                        "https://picsum.photos/id/52/500"
                )
        ));

        highlights.add(new Highlight(
                "h6",
                "Nature",
                "https://picsum.photos/id/60/200",
                Arrays.asList(
                        "https://picsum.photos/id/61/500",
                        "https://picsum.photos/id/62/500"
                )
        ));

        highlights.add(new Highlight(
                "h7",
                "Sports",
                "https://picsum.photos/id/70/200",
                Arrays.asList(
                        "https://picsum.photos/id/71/500",
                        "https://picsum.photos/id/72/500"
                )
        ));

        return highlights;
    }
}