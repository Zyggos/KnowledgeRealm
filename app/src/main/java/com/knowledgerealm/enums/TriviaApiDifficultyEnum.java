package com.knowledgerealm.enums;

/**
 * Enum for the difficulty of the trivia questions
 */
public enum TriviaApiDifficultyEnum {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard"),
    NO_DIFFICULTY("no Difficulty");

    private final String difficulty;

    /**
     * Constructor for the TriviaApiDifficultyEnum class
     * @param difficulty the difficulty of the trivia question
     */
    TriviaApiDifficultyEnum(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Get the difficulty of the trivia question
     * @return the difficulty of the trivia question
     */
    public String getName() {
        return difficulty.substring(0, 1).toUpperCase() + difficulty.substring(1);
    }

    /**
     * Get the TriviaApiDifficultyEnum by the name
     * @param name the name of the TriviaApiDifficultyEnum
     * @return the TriviaApiDifficultyEnum
     */
    public static TriviaApiDifficultyEnum getEnumByName(String name) {
        for (TriviaApiDifficultyEnum triviaApiDifficultyEnum : TriviaApiDifficultyEnum.values()) {
            if (triviaApiDifficultyEnum.getName().equalsIgnoreCase(name)) {
                return triviaApiDifficultyEnum;
            }
        }
        return null;
    }
}
