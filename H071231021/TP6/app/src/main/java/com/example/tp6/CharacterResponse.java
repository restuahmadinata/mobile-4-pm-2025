package com.example.tp6;

import java.util.List;

public class CharacterResponse {
    private Info info;
    private List<Character> results;

    public Info getInfo() {
        return info;
    }

    public List<Character> getResults() {
        return results;
    }

    public static class Info {
        private int pages;
        public int getPages() {
            return pages;
        }

    }
}