package com.knowledgerealm.models;


/**
 * A class that represents a user.
 */
public class User implements Comparable<User> {
    private String username;
    private String email;
    private int score;

    public User(){

    }

    /**
     * Constructor for a user object.
     * @param username the username of the user
     * @param email the email of the user
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.score = 0;
    }

    /**
     * Getter for the username of the user.
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username of the user.
     * @param username the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the email of the user.
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the email of the user.
     * @param email the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the score of the user.
     * @return the score of the user
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter for the score of the user.
     * @param score the score of the user
     */
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(User otherUser) {
        return Integer.compare(this.score, otherUser.score);
    }
}
