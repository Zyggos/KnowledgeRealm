package com.knowledgerealm.models;

import com.knowledgerealm.enums.QuestionPointsEnum;
import com.knowledgerealm.enums.TriviaApiCategoriesEnum;
import com.knowledgerealm.enums.TriviaApiDifficultyEnum;

import java.util.Collections;
import java.util.List;

/**
 * TriviaQuestion is a model that represents a question from the Trivia API.
 */
public class TriviaQuestion {
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;
    private TriviaApiCategoriesEnum category;
    private TriviaApiDifficultyEnum difficulty;

    /**
     * Constructor for the TriviaQuestion class.
     * @param question the question.
     * @param correctAnswer the correct answer.
     * @param incorrectAnswers the incorrect answers.
     * @param category the category.
     * @param difficulty the difficulty.
     */
    public TriviaQuestion(String question, String correctAnswer, List<String> incorrectAnswers, TriviaApiCategoriesEnum category, TriviaApiDifficultyEnum difficulty) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.category = category;
        this.difficulty = difficulty;
    }

    /**
     * Constructor for the TriviaQuestion class.
     * @param question the question.
     * @param correctAnswer the correct answer.
     * @param incorrectAnswers the incorrect answers.
     * @param category the category.
     */
    public TriviaQuestion(String question, String correctAnswer, List<String> incorrectAnswers, TriviaApiCategoriesEnum category) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.category = category;
    }

    /**
     * Constructor for the TriviaQuestion class.
     * @param question the question.
     * @param correctAnswer the correct answer.
     * @param incorrectAnswers the incorrect answers.
     */
    public TriviaQuestion(String question, String correctAnswer, List<String> incorrectAnswers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    /**
     * Constructor for the TriviaQuestion class.
     * @param question the question.
     * @param correctAnswer the correct answer.
     */
    public TriviaQuestion(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the question.
     * @return the question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the question.
     * @param question the question.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Returns the correct answer.
     * @return the correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct answer.
     * @param correctAnswer the correct answer.
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the incorrect answers.
     * @return the incorrect answers.
     */
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    /**
     * Sets the incorrect answers.
     * @param incorrectAnswers the incorrect answers.
     */
    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    /**
     * Returns all the answers randomized.
     * @return all the answers randomized.
     */
    public List<String> getAllAnswersRandomized() {
        List<String> allAnswers = incorrectAnswers;
        allAnswers.add(correctAnswer);
        Collections.shuffle(allAnswers);
        return allAnswers;
    }

    /**
     * Returns the category.
     * @return the category.
     */
    public TriviaApiCategoriesEnum getCategory() {
        return category;
    }

    /**
     * Sets the category.
     * @param category the category.
     */
    public void setCategory(TriviaApiCategoriesEnum category) {
        this.category = category;
    }

    /**
     * Returns the difficulty.
     * @return the difficulty.
     */
    public TriviaApiDifficultyEnum getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty.
     * @param difficulty the difficulty.
     */
    public void setDifficulty(TriviaApiDifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns the points for the question.
     * @return the points for the question or 0 if the difficulty is not recognized.
     */
    public int getPoints() {
        switch (difficulty) {
            case EASY:
                return QuestionPointsEnum.EASY.getPoints();
            case MEDIUM:
                return QuestionPointsEnum.MEDIUM.getPoints();
            case HARD:
                return QuestionPointsEnum.HARD.getPoints();
            default:
                return 0;
        }
    }
}
