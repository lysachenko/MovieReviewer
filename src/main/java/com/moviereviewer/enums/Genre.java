package com.moviereviewer.enums;

public enum Genre {
    SCI_FI("Science fiction"), ACT("Action"), CMD("Comedy"), DRM("Drama");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public static Genre from(String text) {
        for (Genre genre : Genre.values()) {
            if (genre.name.equalsIgnoreCase(text)) {
                return genre;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }
}