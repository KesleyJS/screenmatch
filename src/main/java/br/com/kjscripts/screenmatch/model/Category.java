package br.com.kjscripts.screenmatch.model;

public enum Category {
    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DRAMA("Drama"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    ANIMATION("Animation"),
    ADVENTURE("Adventure");


    private String omdbCategory;

    Category(String omdbCategory) {
        this.omdbCategory = omdbCategory;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
