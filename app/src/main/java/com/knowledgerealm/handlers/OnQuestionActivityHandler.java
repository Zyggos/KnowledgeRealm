package com.knowledgerealm.handlers;

import android.content.Context;
import android.util.Log;

import com.knowledgerealm.enums.TriviaApiCategoriesEnum;
import com.knowledgerealm.enums.TriviaApiDifficultyEnum;
import com.knowledgerealm.helpers.SettingsSaveStatesHelper;
import com.knowledgerealm.helpers.TextReadTimeEstimatorHelper;
import com.knowledgerealm.helpers.TriviaResponseHelper;
import com.knowledgerealm.models.Token;
import com.knowledgerealm.models.TriviaQuestion;
import com.knowledgerealm.models.interfaces.TokenDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Class for handling the questions on the QuestionActivity
 */
public class OnQuestionActivityHandler {

    private TriviaApiHandler triviaApiHandler;
    private List<TriviaQuestion> triviaQuestions;
    private Boolean isTriviaQuestionsLoaded;
    private int maxQuestions;
    private Context context;

    // Settings variables
    private String difficulty;
    private String category;

    /**
     * Constructor for the OnQuestionActivity class
     */
    public OnQuestionActivityHandler(Context context, int maxQuestions) {
        triviaApiHandler = new TriviaApiHandler(null);
        triviaQuestions = new ArrayList<>();
        isTriviaQuestionsLoaded = false;
        this.maxQuestions = maxQuestions;
        this.context = context;

        retrieveSavedSettings();
        handleTokenForUser();
    }

    /**
     * Get the question from the triviaQuestions list
     * @param questionNumber the index of the question in the list
     * @return the trivia question or null if there is an error
     */
    public TriviaQuestion getQuestion(int questionNumber) {
        if (!isTriviaQuestionsLoaded) {
            TriviaResponseHelper triviaResponseHelper = triviaApiHandler.getTriviaQuestions(
                     TriviaApiCategoriesEnum.getEnumByName(category),
                    TriviaApiDifficultyEnum.getEnumByName(difficulty),
                    null,
                    maxQuestions
            );

            if (triviaResponseHelper.isSuccess()) {
                triviaQuestions = triviaResponseHelper.getTriviaQuestions();
                isTriviaQuestionsLoaded = true;
            } else {
                Log.e("OnQuestionActivity", "Error getting trivia questions");
                return null;
            }
        }

        try {
            return triviaQuestions.get(questionNumber);
        } catch (IndexOutOfBoundsException e) {
            Log.e("OnQuestionActivity", "Question number out of bounds");
            return null;
        } catch (Exception e) {
            Log.e("OnQuestionActivity", "Error getting question");
            return null;
        }
    }

    /**
     * Get the category question from the triviaQuestions list
     * @param questionNumber the index of the question in the list
     * @return the string category of the question or "No Category" if there is an error
     */
    public String getCategory(int questionNumber) {
        TriviaQuestion question = getQuestion(questionNumber);
        if (question != null) {
            return shortenString(question.getCategory().getName(), 27);
        } else {
            Log.e("OnQuestionActivity", "Error getting category");
            return TriviaApiCategoriesEnum.NO_CATEGORY.getName();
        }
    }

    /**
     * Get the difficulty of the question from the triviaQuestions list
     * @param questionNumber the index of the question in the list
     * @return the string difficulty of the question or "No Difficulty" if there is an error
     */
    public String getDifficulty(int questionNumber) {
        TriviaQuestion question = getQuestion(questionNumber);
        if (question != null) {
            return question.getDifficulty().getName();
        } else {
            Log.e("OnQuestionActivity", "Error getting difficulty");
            return TriviaApiDifficultyEnum.NO_DIFFICULTY.getName();
        }
    }

    /**
     * Check if the answer is correct
     * @param questionNumber the index of the question in the list
     * @param answer the answer to check
     * @return true if the answer is correct, false otherwise
     */
    public Boolean checkAnswer(int questionNumber, String answer) {
        TriviaQuestion question = getQuestion(questionNumber);
        if (question != null) {
            return question.getCorrectAnswer().equals(answer);
        } else {
            Log.e("OnQuestionActivity", "Error checking answer");
            return false;
        }
    }

    /**
     * Get the correct answer for the question
     * @param questionNumber the index of the question in the list
     * @return the correct answer for the question
     */
    public String getCorrectAnswer(int questionNumber) {
        TriviaQuestion question = getQuestion(questionNumber);
        if (question != null) {
            return question.getCorrectAnswer();
        } else {
            Log.e("OnQuestionActivity", "Error getting correct answer");
            return null;
        }
    }

    /**
     * Get the estimated read time for the question and answer choices, in milliseconds
     * @param questionNumber the index of the question in the list
     * @param defaultTimeValue the default time value to use if there is an error, in milliseconds
     * @return the estimated read time for the question and answer choices, in milliseconds, or the default time value if there is an error
     */
    public int estimateReadTime(int questionNumber, int defaultTimeValue) {
        if (getQuestion(questionNumber) == null) {
            Log.e("OnQuestionActivity", "Error estimating read time, using default value of " + defaultTimeValue + " milliseconds");
            return defaultTimeValue;
        }

        StringBuilder answerChoices = new StringBuilder();
        String question = getQuestion(questionNumber).getQuestion();

        getQuestion(questionNumber).getAllAnswersRandomized().forEach(
                answerChoice -> answerChoices.append(answerChoice).append(" ")
        );

        int estimatedReadTime = TextReadTimeEstimatorHelper.estimateReadTime(question + answerChoices);
        Log.i("OnQuestionActivity", "Estimated read time for question " + questionNumber + " is " + estimatedReadTime + " milliseconds");
        return estimatedReadTime;

    }

    /**
     * Get the points for the question
     * @param currentQuestionNumber the index of the question in the list
     * @return the points for the question
     */
    public int getPoints(int currentQuestionNumber) {
        return getQuestion(currentQuestionNumber).getPoints();
    }

    /**
     * Get the session token
     * @return the session token or null if there is an error
     */
    public String getSessionToken() {
        return triviaApiHandler.getSessionToken();
    }

    /**
     * Handles the user token logic for the trivia questions API
     */
    private void handleTokenForUser() {
        long TOKEN_EXPIRATION_TIME = 6 * 3600000; // 6 hours

        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            Token token = new Token();
            token.setId(0);

            // set a temporary token and the current timestamp
            token.setToken(getSessionToken());
            token.setTimestamp(System.currentTimeMillis());

            // check if there is a token in the database
            TokenDao tokenDao = TokenDatabaseHandler.getInstance(context).tokenDao();
            Token storedToken = tokenDao.getById(0);
            if (storedToken == null || storedToken.getToken() == null) {
                // if there is not, create a new one
                token.setToken(getSessionToken());
                token.setTimestamp(System.currentTimeMillis());
                tokenDao.insert(token);
                Log.i("OnQuestionActivity", "Token " + token + " inserted and being used");
            } else {
                // if there is, check if it is expired
                if (storedToken.getTimestamp() + TOKEN_EXPIRATION_TIME < System.currentTimeMillis()) {
                    // if it is expired, update it
                    token.setToken(getSessionToken());
                    token.setTimestamp(System.currentTimeMillis());
                    tokenDao.update(token);
                    Log.i("OnQuestionActivity", "Token " + token + " updated and being used");
                } else {
                    // if it is not expired, use it
                    Log.i("OnQuestionActivity", "Token " + storedToken.getToken() + " being used");
                    triviaApiHandler.setSessionToken(storedToken.getToken());
                }
            }

            latch.countDown();
        }).start();

        // wait for the thread to finish
        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e("OnQuestionActivity", "Waiting for token thread interrupted. More info: " + e.getMessage());
        }
    }

    // ##### Other Methods #####

    /**
     * Shortens the string to the specified length
     * @param input the string to shorten
     * @param shorteningLength the length to shorten the string to
     * @return the shortened string
     */
    private String shortenString(String input, int shorteningLength) {
        if (input.length() <= shorteningLength) {
            return input;
        } else {
            return (input.substring(0, shorteningLength) + ". . .");
        }
    }

    /**
     * Retrieve the saved settings from the database
     */
    private void retrieveSavedSettings()
    {
        difficulty = SettingsSaveStatesHelper.getDifficulty(context);
        category = SettingsSaveStatesHelper.getCategory(context);
    }
}
