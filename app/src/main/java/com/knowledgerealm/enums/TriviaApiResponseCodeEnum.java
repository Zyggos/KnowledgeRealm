package com.knowledgerealm.enums;

/**
 * TriviaApiResponseCodeEnum is an enum that contains all the possible response codes from the Trivia API.
 */
public enum TriviaApiResponseCodeEnum {
    SUCCESS(0),
    NO_RESULTS(1),
    INVALID_PARAMETER(2),
    TOKEN_NOT_FOUND(3),
    TOKEN_EMPTY(4);

    private final int responseCode;

    /**
     * Constructor for the TriviaApiResponseCodeEnum.
     * @param responseCode the code of the response.
     */
    TriviaApiResponseCodeEnum(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Getter for the code of the response.
     * @return the code of the response.
     */
    public int getResponseCode() {
        return responseCode;
    }
}
