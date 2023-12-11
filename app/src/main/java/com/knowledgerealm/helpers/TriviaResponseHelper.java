package com.knowledgerealm.helpers;

import com.knowledgerealm.models.TriviaQuestion;

import java.util.List;

/**
 * TriviaResponseHelper is a helper class that is used to parse the response from the Trivia API.
 */
public class TriviaResponseHelper {
    private List<TriviaQuestion> triviaQuestions;
    private boolean success;

    /**
     * Constructor for the TriviaResponseHelper class.
     * @param triviaQuestions the trivia questions.
     * @param success whether the request was successful.
     */
    public TriviaResponseHelper(List<TriviaQuestion> triviaQuestions, boolean success) {
        this.triviaQuestions = triviaQuestions;
        this.success = success;
    }

    /**
     * Getter for the trivia questions.
     * @return The trivia questions.
     */
    public List<TriviaQuestion> getTriviaQuestions() {
        return triviaQuestions;
    }

    /**
     * Getter for whether the request was successful.
     * @return whether the request was successful.
     */
    public boolean isSuccess() {
        return success;
    }
}
