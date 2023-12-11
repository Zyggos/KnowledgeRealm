package com.knowledgerealm.enums;

/**
 * Enum for the different points for each question difficulty.
 */
public enum QuestionPointsEnum {
    EASY(130),
    MEDIUM(260),
    HARD(390);

    private int points;

    /**
     * Constructor for the QuestionPointsEnum class.
     * @param points the points for the question.
     */
    QuestionPointsEnum(int points) {
        this.points = points;
    }

    /**
     * Returns the points for the question.
     * @return the points for the question.
     */
    public int getPoints() {
        return points;
    }
}
