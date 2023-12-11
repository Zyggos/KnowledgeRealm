package com.knowledgerealm.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Settings model class
 * This class is used to store the settings of the game
 * It is used to store the difficulty, category, sound volume and music volume
 */
@Entity(tableName = "settings")
public class Settings {
    @PrimaryKey
    private int id;

    @NonNull
    private String difficulty;

    @NonNull
    private String category;

    private float soundVolume;

    private float musicVolume;

    /**
     * Constructor for Settings class
     * @param difficulty The difficulty of the game
     * @param category The category of the game
     * @param soundVolume The sound volume of the game
     * @param musicVolume The music volume of the game
     */
    public Settings(@NonNull String difficulty, @NonNull String category, float soundVolume, float musicVolume) {
        this.id = 1;
        this.difficulty = difficulty;
        this.category = category;
        this.soundVolume = soundVolume;
        this.musicVolume = musicVolume;
    }

    /**
     * Get the id of the settings
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the settings
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the difficulty of the game
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Set the difficulty of the game
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Get the category of the game
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category of the game
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the sound volume of the game
     */
    public float getSoundVolume() {
        return soundVolume;
    }

    /**
     * Set the sound volume of the game
     */
    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    /**
     * Get the music volume of the game
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Set the music volume of the game
     */
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }
}
