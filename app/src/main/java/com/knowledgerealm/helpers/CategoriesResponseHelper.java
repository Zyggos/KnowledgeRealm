package com.knowledgerealm.helpers;

import java.util.List;

/**
 * CategoriesResponseHelper is a helper class that is used to parse the response from the Trivia API.
 */
public class CategoriesResponseHelper {
    public final List<String> categories;
    public final boolean success;

    /**
     * Constructor for the CategoriesResponseHelper class.
     * @param categories The categories.
     * @param success Whether the request was successful.
     */
    public CategoriesResponseHelper(List<String> categories, boolean success) {
        this.categories = categories;
        this.success = success;
    }
}