package com.knowledgerealm.models.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.knowledgerealm.models.Token;

/**
 * Interface for the token database
 */
@Dao
public interface TokenDao {
    /**
     * Insert a token into the database
     * @param token the token to insert
     */
    @Insert
    void insert(Token token);

    /**
     * Update a token in the database
     * @param token the token to update
     */
    @Update
    void update(Token token);

    /**
     * Get the token from the database
     * @param id the id of the token. Default being used is 0.
     * @return the Token from the database or null if there is an error
     */
    @Query("SELECT * FROM token WHERE id = :id")
    Token getById(int id);
}

