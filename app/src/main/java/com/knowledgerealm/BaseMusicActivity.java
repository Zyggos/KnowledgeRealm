package com.knowledgerealm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.knowledgerealm.handlers.BackgroundMusicHandler;

/**
 * BaseMusicActivity is an abstract class that extends AppCompatActivity and is extended by all activities
 */
public class BaseMusicActivity extends AppCompatActivity {
    BackgroundMusicHandler musicHandler;

    /**
     * onCreate initializes the BackgroundMusicHandler
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicHandler = BackgroundMusicHandler.getInstance();
        musicHandler.initialize(this);
    }

    /**
     * onStart starts the BackgroundMusicHandler
     */
    @Override
    protected void onStart() {
        super.onStart();
        musicHandler.start(this);
    }

    /**
     * onStop stops the BackgroundMusicHandler
     */
    @Override
    protected void onResume() {
        super.onResume();
        musicHandler.resume(this);
    }

    /**
     * onPause pauses the BackgroundMusicHandler
     */
    @Override
    protected void onPause() {
        super.onPause();
        musicHandler.pause(this);
    }

    /**
     * onStop stops the BackgroundMusicHandler
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicHandler.release();
        cancelAll();
    }

    /**
     * BeginMusic begins the background music on repeat
     */
    public void beginMusic() {
        musicHandler.beginMusic(this);
    }

    /**
     * Plays once the sound for the correct answer
     */
    public void playCorrectSound() {
        musicHandler.playCorrectSound(this);
    }

    /**
     * Plays once the sound for the incorrect answer
     */
    public void playIncorrectSound() {
        musicHandler.playIncorrectSound(this);
    }

    /**
     * Plays once the sound for the time up
     */
    public void playTimeUpSound() {
        musicHandler.playTimeUpSound(this);
    }

    /**
     * Cancels all the sounds and releases the resources
     */
    public void cancelAll() {
        musicHandler.cancelAll();
    }

}
