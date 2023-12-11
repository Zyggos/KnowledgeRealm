package com.knowledgerealm.enums;

/**
 * TriviaApiCategoriesEnum is an enum that represents the categories that the Trivia API has.
 */
public enum TriviaApiCategoriesEnum {
    GENERAL_KNOWLEDGE(9, "General Knowledge"),
    BOOKS(10, "Entertainment: Books"),
    FILM(11, "Entertainment: Film"),
    MUSIC(12, "Entertainment: Music"),
    MUSICALS_THEATRES(13, "Entertainment: Musicals & Theatres"),
    TELEVISION(14, "Entertainment: Television"),
    VIDEO_GAMES(15, "Entertainment: Video Games"),
    BOARD_GAMES(16, "Entertainment: Board Games"),
    SCIENCE_NATURE(17, "Science & Nature"),
    COMPUTERS(18, "Science: Computers"),
    MATHEMATICS(19, "Science: Mathematics"),
    MYTHOLOGY(20, "Mythology"),
    SPORTS(21, "Sports"),
    GEOGRAPHY(22, "Geography"),
    HISTORY(23, "History"),
    POLITICS(24, "Politics"),
    ART(25, "Art"),
    CELEBRITIES(26, "Celebrities"),
    ANIMALS(27, "Animals"),
    VEHICLES(28, "Vehicles"),
    COMICS(29, "Entertainment: Comics"),
    GADGETS(30, "Science: Gadgets"),
    ANIME_MANGA(31, "Entertainment: Japanese Anime & Manga"),
    CARTOON_ANIMATIONS(32, "Entertainment: Cartoon & Animations"),
    NO_CATEGORY(0, "No Category");

    private final int id;
    private final String name;

    /**
     * Constructor for the TriviaApiCategoriesEnum class.
     * @param id the id of the category.
     * @param name the name of the category.
     */
    TriviaApiCategoriesEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the enum based on the id.
     * @param id the id of the category.
     * @return the enum based on the id.
     */
    public static TriviaApiCategoriesEnum getEnum(int id) {
        for (TriviaApiCategoriesEnum category : TriviaApiCategoriesEnum.values()) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }

    /**
     * Get the enum based on the name.
     * @param name the name of the category.
     * @return the enum based on the name.
     */
    public static TriviaApiCategoriesEnum getEnumByName(String name) {
        for (TriviaApiCategoriesEnum category : TriviaApiCategoriesEnum.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    /**
     * Gets the id of the category.
     * @return the id of the category.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the category.
     * @return the name of the category.
     */
    public String getName() {
        return name;
    }
}
