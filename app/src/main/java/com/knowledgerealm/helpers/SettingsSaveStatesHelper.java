package com.knowledgerealm.helpers;

import android.content.Context;

import com.knowledgerealm.handlers.DatabaseHandler;
import com.knowledgerealm.models.Settings;

public class SettingsSaveStatesHelper {
    public static final String defaultDifficulty = "";
    public static final String defaultCategory = "";
    public static final float defaultSoundVolume = 10.0f;
    public static final float defaultMusicVolume = 10.0f;

    /**
     * Save settings in the database
     * @param difficulty the difficulty of the questions, can be easy, medium or hard. If null, will be set to default empty string or the saved difficulty, if any
     * @param category the category of the questions, can be any category from the API. If null, will be set to default empty string or the saved category, if any
     * @param soundVolume the sound volume, can be any float between 0.0f and 10.0f. If -1.0f, will be set to default 10.0f or the saved sound volume, if any
     * @param musicVolume the music volume, can be any float between 0.0f and 10.0f. If -1.0f, will be set to default 10.0f, or the saved music volume, if any
     * @param context the context of the application (CategoriesActivity)
     * @return true if the settings were saved successfully, false otherwise
     */
    public static void saveSettings(String difficulty, String category,  float soundVolume, float musicVolume, Context context) {
        // If difficulty is not set, set it to default
        if (difficulty == null) {
            if (hasDifficulty(context)) {
                Settings settings = DatabaseHandler.getSettings(context);
                difficulty = settings.getDifficulty();
            } else {
                difficulty = defaultDifficulty;
            }
        }

        // If category is not set, set it to default
        if (category == null) {
            if (hasCategory(context)) {
                Settings settings = DatabaseHandler.getSettings(context);
                category = settings.getCategory();
            } else {
                category = defaultCategory;
            }
        }

        // If sound volume is not set, set it to default
        if (soundVolume == -1.0f) {
            if (hasSoundVolume(context)) {
                Settings settings = DatabaseHandler.getSettings(context);
                soundVolume = settings.getSoundVolume();
            } else {
                soundVolume = defaultSoundVolume;
            }
        }

        // If music volume is not set, set it to default
        if (musicVolume == -1.0f) {
            if (hasMusicVolume(context)) {
                Settings settings = DatabaseHandler.getSettings(context);
                musicVolume = settings.getMusicVolume();
            } else {
                musicVolume = defaultMusicVolume;
            }
        }

        // Save/update settings
        DatabaseHandler.saveOrUpdateSettings(difficulty, category, soundVolume, musicVolume, context);
    }

    /**
     * Save category in the database
     * @param category the category of the questions, can be any category from the API. If null, will be set to default null, or the saved category, if any
     * @param context the context of the application (CategoriesActivity)
     */
    public static void saveCategory(String category, Context context)
    {
        saveSettings(null, category, -1.0f, -1.0f, context);
    }

    /**
     * Save difficulty in the database
     * @param difficulty the difficulty of the questions, can be easy, medium or hard. If null, will be set to default null, or the saved difficulty, if any
     * @param context the context of the application (CategoriesActivity)
     */
    public static void saveDifficulty(String difficulty, Context context)
    {
        saveSettings(difficulty, null, -1.0f, -1.0f, context);
    }

    /**
     * Save sound volume in the database
     * @param soundVolume the sound volume, can be any float between 0.0f and 10.0f. If -1.0f, will be set to default 10.0f, or the saved sound volume, if any
     * @param context the context of the application (CategoriesActivity)
     */
    public static void saveSoundVolume(float soundVolume, Context context)
    {
        saveSettings(null, null, soundVolume, -1.0f, context);
    }

    /**
     * Save music volume in the database
     * @param musicVolume the music volume, can be any float between 0.0f and 10.0f. If -1.0f, will be set to default 10.0f, or the saved music volume, if any
     * @param context the context of the application (CategoriesActivity)
     */
    public static void saveMusicVolume(float musicVolume, Context context)
    {
        saveSettings(null, null, -1.0f, musicVolume, context);
    }

    /**
     * Get difficulty from the database
     * @param context the context of the application (CategoriesActivity)
     * @return the difficulty of the questions, can be easy, medium or hard. If null, will be set to default empty string or the saved difficulty, if any
     */
    public static String getDifficulty(Context context) {
        if (!hasDifficulty(context)) {
            return defaultDifficulty;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getDifficulty();
    }

    /**
     * Get category from the database
     * @param context the context of the application (CategoriesActivity)
     * @return the category of the questions, can be any category from the API. If null, will be set to default empty string or the saved category, if any
     */
    public static String getCategory(Context context) {
        if (!hasCategory(context)) {
            return defaultCategory;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getCategory();
    }

    /**
     * Get sound volume from the database
     * @param context the context of the application (CategoriesActivity)
     * @return the sound volume, can be any float between 0.0f and 10.0f. If -1.0f, will be set to default 10.0f or the saved sound volume, if any
     */
    public static float getSoundVolume(Context context) {
        if (!hasSoundVolume(context)) {
            return defaultSoundVolume;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getSoundVolume();
    }

    /**
     * Get music volume from the database
     * @param context the context of the application (CategoriesActivity)
     * @return the music volume, can be any float between 0.0f and 10.0f. If -1.0f, will be set to default 10.0f or the saved music volume, if any
     */
    public static float getMusicVolume(Context context) {
        if (!hasMusicVolume(context)) {
            return defaultMusicVolume;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getMusicVolume();
    }

    /**
     * Check if the database has settings
     * @param context the context of the application (CategoriesActivity)
     * @return true if the database has settings, false otherwise
     */
    private static boolean hasSettings(Context context) {
        Settings settings = DatabaseHandler.getSettings(context);
        return settings != null;
    }

    /**
     * Check if the database has difficulty
     * @param context the context of the application (CategoriesActivity)
     * @return true if the database has difficulty, false otherwise
     */
    private static boolean hasDifficulty(Context context) {
        if (!hasSettings(context)) {
            return false;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getDifficulty() != null;
    }

    /**
     * Check if the database has category
     * @param context the context of the application (CategoriesActivity)
     * @return true if the database has category, false otherwise
     */
    private static boolean hasCategory(Context context) {
        if (!hasSettings(context)) {
            return false;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getCategory() != null;
    }

    /**
     * Check if the database has sound volume
     * @param context the context of the application (CategoriesActivity)
     * @return true if the database has sound volume, false otherwise
     */
    private static boolean hasSoundVolume(Context context) {
        if (!hasSettings(context)) {
            return false;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getSoundVolume() != -1.0f;
    }

    /**
     * Check if the database has music volume
     * @param context the context of the application (CategoriesActivity)
     * @return true if the database has music volume, false otherwise
     */
    private static boolean hasMusicVolume(Context context) {
        if (!hasSettings(context)) {
            return false;
        }

        Settings settings = DatabaseHandler.getSettings(context);
        return settings.getMusicVolume() != -1.0f;
    }
}
