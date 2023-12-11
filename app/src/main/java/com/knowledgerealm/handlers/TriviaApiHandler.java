package com.knowledgerealm.handlers;

import static com.knowledgerealm.enums.TriviaApiResponseCodeEnum.SUCCESS;
import static com.knowledgerealm.enums.TriviaApiResponseCodeEnum.TOKEN_EMPTY;
import static com.knowledgerealm.enums.TriviaApiResponseCodeEnum.TOKEN_NOT_FOUND;
import static com.knowledgerealm.helpers.HtmlHelper.decodeHtml;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgerealm.enums.TriviaApiCategoriesEnum;
import com.knowledgerealm.enums.TriviaApiDifficultyEnum;
import com.knowledgerealm.helpers.CategoriesResponseHelper;
import com.knowledgerealm.helpers.HttpRequestAsyncHelper;
import com.knowledgerealm.helpers.TriviaResponseHelper;
import com.knowledgerealm.models.TriviaQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * TriviaApiHandler is a class that handles all the API calls to the Trivia API.
 */
public class TriviaApiHandler {
    private final String baseURL = "https://opentdb.com/api.php";
    private final String tokenURL = "https://opentdb.com/api_token.php";
    private String sessionToken;

    /**
     * Constructor for the TriviaApiHandler.
     * Retrieves a new session token from the Trivia API.
     */
    public TriviaApiHandler() {
        checkAndCorrectSessionToken();
    }

    /**
     * Constructor for the TriviaApiHandler.
     * @param sessionToken the session token to use for the API calls.
     */
    public TriviaApiHandler(String sessionToken) {
        this.sessionToken = sessionToken;
        checkAndCorrectSessionToken();
    }

    /**
     * Makes a request to the Open Trivia API to retrieve a list of trivia categories.
     * @return a CategoriesResponseHelper object containing a list of category names and a success flag.
     */
    public static CategoriesResponseHelper getAllCategories() {
        String urlRequest = "https://opentdb.com/api_category.php";
        List<String> categories = new ArrayList<>();
        boolean success = false;

        try {
            // Use the getStringAsync method to make the HTTP request asynchronously.
            CompletableFuture<String> responseFuture = HttpRequestAsyncHelper.getResponseAsync(urlRequest);
            String content = responseFuture.get();

            // Parse the JSON response into a list of category names.
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(content);
            JsonNode categoriesNode = rootNode.get("trivia_categories");
            for (JsonNode categoryNode : categoriesNode) {
                String categoryName = categoryNode.get("name").asText();
                categories.add(categoryName);
            }
            success = true;
        } catch (Exception e) {
            Log.e("TriviaApiHandler", "Error retrieving categories: " + e.getMessage());
        }

        return new CategoriesResponseHelper(categories, success);
    }

    /**
     * Sets the given session token to the TriviaApiHandler object.
     * @param sessionToken the string session token to use for the API calls.
     * @return true if the token is set successfully, false otherwise.
     */
    public boolean setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        return checkAndCorrectSessionToken();
    }

    /**
     * Retrieves a new session token from the Trivia API or sets the session token to null in the case of an error.
     * @return true if the token is retrieved successfully, false otherwise.
     */
    private Boolean retrieveSessionToken() {
        String urlRequest = tokenURL + "?command=request";
        boolean isTokenRetrieved = false;

        try {
            // Use the getStringAsync method to make the HTTP request asynchronously.
            CompletableFuture<String> responseFuture = HttpRequestAsyncHelper.getResponseAsync(urlRequest);
            String content = responseFuture.get();

            // Parse the JSON response.
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(content);

            // Get the session token from the JSON response.
            if (jsonNode.get("response_code").asInt() == SUCCESS.getResponseCode()) {
                sessionToken = jsonNode.get("token").asText();
                isTokenRetrieved = true;
            } else {
                sessionToken = null;
            }
        } catch (Exception e) {
            Log.e("TriviaApiHandler", "Error retrieving session token: " + e.getMessage());
        }

        return isTokenRetrieved;
    }

    /**
     * Resets the session token or sets it to null if the token is invalid.
     * @return true if the token is reset successfully, false otherwise.
     */
    private Boolean resetSessionToken() {
        String urlRequest = tokenURL + "?command=reset&token=" + sessionToken;
        boolean isTokenReset = false;

        try {
            // Use the getStringAsync method to make the HTTP request asynchronously.
            CompletableFuture<String> responseFuture = HttpRequestAsyncHelper.getResponseAsync(urlRequest);
            String content = responseFuture.get();

            // Parse the JSON response.
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(content);

            // Get the session token from the JSON response.
            if (jsonNode.get("response_code").asInt() == SUCCESS.getResponseCode()) {
                sessionToken = jsonNode.get("token").asText();
                isTokenReset = true;
            } else {
                sessionToken = null;
            }
        } catch (Exception e) {
            Log.e("TriviaApiHandler", "Error resetting session token: " + e.getMessage());
        }

        return isTokenReset;
    }

    /**
     * Checks if the session token is valid, or resets if
     * token is empty, or retrieves a new token if token is invalid.
     * @return true if the token is valid, false otherwise.
     */
    private boolean checkAndCorrectSessionToken() {
        if (sessionToken == null) {
            Log.i("TriviaApiHandler", "Session token is null. Retrieving a new token.");
            return retrieveSessionToken();
        }

        String urlRequest = baseURL + "?amount=1&token=" + sessionToken;

        try {
            // Use the getStringAsync method to make the HTTP request asynchronously.
            CompletableFuture<String> responseFuture = HttpRequestAsyncHelper.getResponseAsync(urlRequest);
            String content = responseFuture.get();

            // Parse the JSON response.
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(content);

            // Get the session token from the JSON response.
            if (jsonNode.get("response_code").asInt() == SUCCESS.getResponseCode()) {
                Log.d("TriviaApiHandler", "Session token is valid. Continuing with the API call.");
                return true;
            } else if (jsonNode.get("response_code").asInt() == TOKEN_EMPTY.getResponseCode()) {
                Log.i("TriviaApiHandler", "Session token has returned all possible questions for the specified query. Resetting the token.");

                if (resetSessionToken()) {
                    return checkAndCorrectSessionToken();
                }

                return false;
            } else if (jsonNode.get("response_code").asInt() == TOKEN_NOT_FOUND.getResponseCode()) {
                Log.i("TriviaApiHandler", "Session token not found. Retrieving a new token.");

                if (retrieveSessionToken()) {
                    return checkAndCorrectSessionToken();
                }
                return false;
            }
        } catch (Exception e) {
            Log.e("TriviaApiHandler", "Error checking session token: " + e.getMessage());
        }

        return false;
    }

    /**
     * Gets the session token.
     * @return the session token.
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Makes a request to the Open Trivia API to retrieve a list of trivia questions.
     * @param category   the category of questions to retrieve.
     * @param difficulty the difficulty of questions to retrieve.
     * @param type       the type of questions to retrieve.
     * @param amount     the number of questions to retrieve.
     * @return a TriviaResponseHelper object containing a list of trivia questions and a success flag.
     */
    public TriviaResponseHelper getTriviaQuestions(TriviaApiCategoriesEnum category, TriviaApiDifficultyEnum difficulty, String type, int amount) {
        String urlRequest = baseURL + "?amount=" + amount;

        if (category != null) {
            urlRequest += "&category=" + category.getId();
        }

        if (difficulty != null) {
            urlRequest += "&difficulty=" + difficulty.getName().toLowerCase();
        }

        if (type != null) {
            urlRequest += "&type=" + type;
        }

        if (!checkAndCorrectSessionToken()) {
            return new TriviaResponseHelper(null, false);
        }

        List<TriviaQuestion> triviaQuestions = new ArrayList<>();
        boolean success = false;

        try {
            // Use the getStringAsync method to make the HTTP request asynchronously.
            CompletableFuture<String> responseFuture = HttpRequestAsyncHelper.getResponseAsync(urlRequest);
            String content = responseFuture.get();

            // Parse the JSON response into a list of trivia questions.
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(content);
            if (rootNode.get("response_code").asInt() == SUCCESS.getResponseCode()) {
                JsonNode resultsNode = rootNode.get("results");
                for (JsonNode resultNode : resultsNode) {
                    String question = decodeHtml(resultNode.get("question").asText());
                    String correctAnswer = decodeHtml(resultNode.get("correct_answer").asText());
                    String categoryString = decodeHtml(resultNode.get("category").asText());
                    String difficultyString = decodeHtml(resultNode.get("difficulty").asText());
                    List<String> incorrectAnswers = new ArrayList<>();
                    JsonNode incorrectAnswersNode = resultNode.get("incorrect_answers");
                    for (JsonNode incorrectAnswerNode : incorrectAnswersNode) {
                        incorrectAnswers.add(decodeHtml(incorrectAnswerNode.asText()));
                    }
                    triviaQuestions.add(new TriviaQuestion(
                            question,
                            correctAnswer,
                            incorrectAnswers,
                            TriviaApiCategoriesEnum.getEnumByName(categoryString),
                            TriviaApiDifficultyEnum.valueOf(difficultyString.toUpperCase()))
                    );
                }
                success = true;
            } else if (rootNode.get("response_code").asInt() == TOKEN_EMPTY.getResponseCode() ||
                    rootNode.get("response_code").asInt() == TOKEN_NOT_FOUND.getResponseCode()) {
                // If the session token is invalid, retrieve a new one.
                if (!checkAndCorrectSessionToken()) {
                    return getTriviaQuestions(category, difficulty, type, amount);
                }
            }
        } catch (Exception e) {
            Log.e("TriviaApiHandler", "Error retrieving trivia questions: " + e.getMessage());
        }

        return new TriviaResponseHelper(triviaQuestions, success);
    }

}
