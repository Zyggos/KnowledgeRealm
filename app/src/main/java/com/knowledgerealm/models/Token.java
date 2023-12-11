package com.knowledgerealm.models;

import androidx.room.*;

/**
 * Class for the token model
 */
@Entity
public class Token {
    @PrimaryKey
    private int id;
    private String token;
    private long timestamp;

    /**
     * Get the id of the token. This is the primary key of the token
     * and the default value is 0, so there will only be one token.
     * @return the id of the token
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the token. This is the primary key of the token
     * and the default value should be 0, so there will only be one token.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the token
     * @return the token string or null if there is an error
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the token string for the token object
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get the timestamp of the token
     * @return the timestamp of the token or 0 if there is an error
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp of the token
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

