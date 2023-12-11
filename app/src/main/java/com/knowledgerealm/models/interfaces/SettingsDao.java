package com.knowledgerealm.models.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.knowledgerealm.models.Settings;

/**
 * Interface for SettingsDao
 */
@Dao
public interface SettingsDao {
    /**
     * Insert settings
     * @param settings Settings object
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSettings(Settings settings);

    /**
     * Update settings
     * @param settings Settings object
     */
    @Update
    void updateSettings(Settings settings);

    /**
     * Get settings
     * @return Settings object
     */
    @Query("SELECT * FROM settings LIMIT 1")
    Settings getSettings();
}

