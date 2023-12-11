package com.knowledgerealm.handlers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.knowledgerealm.models.Token;
import com.knowledgerealm.models.interfaces.TokenDao;

/**
 * Class for handling the token database
 */
@Database(entities = {Token.class}, version = 1)
public abstract class TokenDatabaseHandler extends RoomDatabase {
    private static final String DATABASE_NAME = "token_database";
    private static TokenDatabaseHandler INSTANCE;
    public abstract TokenDao tokenDao();

    /**
     * Get the instance of the database
     * @param context the context of the application
     * @return the instance of the database
     */
    public static synchronized TokenDatabaseHandler getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TokenDatabaseHandler.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
