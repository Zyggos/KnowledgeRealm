package com.knowledgerealm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.knowledgerealm.helpers.SettingsSaveStatesHelper;

public class SettingsActivity extends AppCompatActivity {
    ImageView back;
    RelativeLayout setDifficulty;
    RelativeLayout chooseCategory;
    RelativeLayout logout;
    RangeSlider setMusicVolume;
    RangeSlider setSoundVolume;

    // Firebase authentication
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize the views
        back = findViewById(R.id.back);
        setDifficulty = findViewById(R.id.setDifficulty);
        chooseCategory = findViewById(R.id.chooseCategory);
        logout = findViewById(R.id.logout);
        setMusicVolume = findViewById(R.id.setMusicVolume);
        setSoundVolume = findViewById(R.id.setSoundVolume);

        // Initialize the firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // Set the saved settings
        setSavedSettings();

        // Set the onClickListeners
        setOnClickListeners();
    }

    /**
     * Set the saved settings
     */
    private void setSavedSettings() {
        setMusicVolume.setValues(SettingsSaveStatesHelper.getMusicVolume(SettingsActivity.this));
        setSoundVolume.setValues(SettingsSaveStatesHelper.getSoundVolume(SettingsActivity.this));
    }

    /**
     * Set the background of the buttons to null when the activity is resumed
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onResume() {
        super.onResume();
        back.setBackground(null);
        setDifficulty.setBackground(null);
        chooseCategory.setBackground(null);
        logout.setBackground(null);
    }

    /**
     * On back pressed, go to main activity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }

    /**
     * Set the onClickListeners
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOnClickListeners() {
        // Set onClick listener for back button
        back.setOnClickListener(v -> {
            back.setBackground(getDrawable(R.drawable.round_back_white));
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        });

        // Set onClick listener for choosing difficulty
        setDifficulty.setOnClickListener(v -> {
            setDifficulty.setBackground(getDrawable(R.drawable.round_back_white));
            startActivity(new Intent(SettingsActivity.this, DifficultyActivity.class));
        });

        // Set onClick listener for choosing category
        chooseCategory.setOnClickListener(v -> {
            chooseCategory.setBackground(getDrawable(R.drawable.round_back_white));
            startActivity(new Intent(SettingsActivity.this, CategoriesActivity.class));
        });

        // Set onClick listener for logging out
        logout.setOnClickListener(v -> {
            logout.setBackground(getDrawable(R.drawable.round_back_logout_active));
            mAuth.signOut();
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        });

        // Set sound volume change listener
        setSoundVolume.addOnChangeListener((slider, value, fromUser) -> SettingsSaveStatesHelper.saveSoundVolume(value, SettingsActivity.this));

        // Set music volume change listener
        setMusicVolume.addOnChangeListener((slider, value, fromUser) -> SettingsSaveStatesHelper.saveMusicVolume(value, SettingsActivity.this));
    }
}