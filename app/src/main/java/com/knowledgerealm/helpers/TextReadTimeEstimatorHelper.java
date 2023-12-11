package com.knowledgerealm.helpers;

/**
 * A helper class that estimates the time needed to read a text paragraph based on the average reading speed of 200 words per minute.
 */
public class TextReadTimeEstimatorHelper {
    private static final int AVERAGE_READING_SPEED = 100; // words per minute

    private TextReadTimeEstimatorHelper() {
        // private constructor to prevent
        // instantiation of this helper class
    }

    /**
     * Estimates the time needed to read the given text paragraph.
     * @param text the text paragraph to be read
     * @return the estimated time needed to read the paragraph, in milliseconds
     */
    public static int estimateReadTime(String text) {
        int numWords = text.split("\\s+").length;
       return (int) Math.ceil(numWords / (double) AVERAGE_READING_SPEED * 60) * 1000;
    }
}
