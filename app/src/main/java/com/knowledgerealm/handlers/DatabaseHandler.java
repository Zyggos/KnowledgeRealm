package com.knowledgerealm.handlers;

import android.content.Context;
import android.util.Log;

import com.knowledgerealm.models.Settings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class is used to handle the database operations
 * It is used to save, retrieve, and update the settings from the database
 */
public class DatabaseHandler {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * This method is used to save or update the settings in the database
     *
     * @param difficulty  the difficulty of the game, it can be easy, medium or hard
     * @param category    the category of the game, it can be any of the categories in the trivia api
     * @param soundVolume the sound volume of the game
     * @param musicVolume the music volume of the game
     * @param context     the context of the activity
     */
    public static void saveOrUpdateSettings(String difficulty, String category, float soundVolume, float musicVolume, Context context) {
        Future<?> future = executor.submit(() -> {
            Settings settings = new Settings(difficulty, category, soundVolume, musicVolume);
            SettingsDatabaseHandler db = SettingsDatabaseHandler.getDatabase(context);
            Settings existingSettings = db.settingsDao().getSettings();
            if (existingSettings == null) {
                db.settingsDao().insertSettings(settings);
            } else {
                existingSettings.setDifficulty(difficulty);
                existingSettings.setCategory(category);
                existingSettings.setSoundVolume(soundVolume);
                existingSettings.setMusicVolume(musicVolume);
                db.settingsDao().updateSettings(existingSettings);
            }
        });

        // Wait for the task to finish
        try {
            future.get();
        } catch (Exception e) {
            Log.e("DatabaseHandler", "Error while waiting for the task to finish", e);
        }
    }

    /**
     * This method is used to retrieve the settings from the database
     *
     * @param context the context of the activity
     * @return the settings object
     */
    public static Settings getSettings(Context context) {
        Future<Settings> future = executor.submit(() -> {
            SettingsDatabaseHandler db = SettingsDatabaseHandler.getDatabase(context);
            return db.settingsDao().getSettings();
        });

        // Wait for the task to finish
        try {
            return future.get();
        } catch (Exception e) {
            Log.e("DatabaseHandler", "Error while waiting for the task to finish", e);
            return null;
        }
    }
}
