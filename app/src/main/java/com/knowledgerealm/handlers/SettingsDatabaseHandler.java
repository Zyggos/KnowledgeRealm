package com.knowledgerealm.handlers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.knowledgerealm.models.Settings;
import com.knowledgerealm.models.interfaces.SettingsDao;

/**
 * This class is used to handle the database operations
 */
@Database(entities = {Settings.class}, version = 1)
public abstract class SettingsDatabaseHandler extends RoomDatabase {
    public abstract SettingsDao settingsDao();

    private static volatile SettingsDatabaseHandler INSTANCE;

    /**
     * This method is used to get the database instance
     * @param context the context of the activity
     * @return the database instance
     */
    public static SettingsDatabaseHandler getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SettingsDatabaseHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    SettingsDatabaseHandler.class, "settings")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

