package com.example.kilogram;

import java.util.ArrayList;
import java.util.List;

public class StoryDataProvider {

    /**
     * Creates a list of dummy story data for testing/demo purposes
     * @return List of Story objects with dummy data
     */
    public static List<Story> getDummyStories() {
        List<Story> stories = new ArrayList<>();

        // Add dummy stories (10 items)
        stories.add(new Story("1", "Your Story", "https://i.pinimg.com/originals/bf/e9/d9/bfe9d9a8ecea7b01ac207dfa6e4ee52e.jpg", true));
        stories.add(new Story("2", "John_Smith", "https://randomuser.me/api/portraits/men/2.jpg", true));
        stories.add(new Story("3", "Emma_Watson", "https://randomuser.me/api/portraits/women/3.jpg", false));
        stories.add(new Story("4", "Michael_B", "https://randomuser.me/api/portraits/men/4.jpg", true));
        stories.add(new Story("5", "Sophia_J", "https://randomuser.me/api/portraits/women/5.jpg", false));
        stories.add(new Story("6", "James_K", "https://randomuser.me/api/portraits/men/6.jpg", true));
        stories.add(new Story("7", "Olivia_P", "https://randomuser.me/api/portraits/women/7.jpg", false));
        stories.add(new Story("8", "William_H", "https://randomuser.me/api/portraits/men/8.jpg", true));
        stories.add(new Story("9", "Ava_Wilson", "https://randomuser.me/api/portraits/women/9.jpg", false));
        stories.add(new Story("10", "Daniel_T", "https://randomuser.me/api/portraits/men/10.jpg", true));

        return stories;
    }

}