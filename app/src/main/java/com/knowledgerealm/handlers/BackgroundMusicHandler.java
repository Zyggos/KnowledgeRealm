package com.knowledgerealm.handlers;

import android.content.Context;
import android.media.MediaPlayer;

import com.knowledgerealm.*;
import com.knowledgerealm.helpers.SettingsSaveStatesHelper;

/**
 * BackgroundMusicHandler class is a singleton class that handles the background music and sound effects of the app.
 */
public class BackgroundMusicHandler {
    private static BackgroundMusicHandler instance;

    // Media Players
    private MediaPlayer mediaPlayer;
    private MediaPlayer correctSoundPlayer;
    private MediaPlayer incorrectSoundPlayer;
    private MediaPlayer timeUpSoundPlayer;

    // Variables
    private float musicVolume;
    private float soundVolume;
    private int musicId;

    // Flags
    private boolean musicStarted = false;
    private boolean canceled = false;

    /**
     * Private constructor to prevent instantiation
     */
    private BackgroundMusicHandler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the instance of the BackgroundMusicHandler class
     *
     * @return instance
     */
    public static BackgroundMusicHandler getInstance() {
        if (instance == null) {
            instance = new BackgroundMusicHandler();
        }
        return instance;
    }

    /**
     * Initializes the BackgroundMusicHandler class
     *
     * @param context Context of the activity
     */
    public void initialize(Context context) {
        getMusicBasedOnActivity(context.getClass());
        musicVolume = SettingsSaveStatesHelper.getMusicVolume(context) / 10;
    }

    /**
     * Starts the background music
     *
     * @param context Context of the activity
     */
    public void start(Context context) {
        musicVolume = SettingsSaveStatesHelper.getMusicVolume(context) / 10;
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && musicStarted && !canceled) {
            mediaPlayer.start();
        }
    }

    /**
     * Pauses the background music
     *
     * @param context Context of the activity
     */
    public void pause(Context context) {
        musicVolume = SettingsSaveStatesHelper.getMusicVolume(context) / 10;
        if (mediaPlayer != null && mediaPlayer.isPlaying() && musicStarted && !canceled) {
            mediaPlayer.pause();
        }
    }

    /**
     * Resumes the background music
     *
     * @param context Context of the activity
     */
    public void resume(Context context) {
        musicVolume = SettingsSaveStatesHelper.getMusicVolume(context) / 10;
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && musicStarted && !canceled) {
            mediaPlayer.start();
        }
    }

    /**
     * Releases the media player and sets it to null
     */
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Starts the background music based on the activity
     *
     * @param context Context of the activity
     */
    public void beginMusic(Context context) {
        if (!musicStarted && !canceled) {
            musicVolume = SettingsSaveStatesHelper.getMusicVolume(context) / 10;
            mediaPlayer = MediaPlayer.create(context, musicId);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(musicVolume, musicVolume);
            mediaPlayer.start();
            musicStarted = true;
        }
    }

    /**
     * Plays the sound effect for correct answer
     *
     * @param context Context of the activity
     */
    public void playCorrectSound(Context context) {
        if (timeUpSoundPlayer != null && timeUpSoundPlayer.isPlaying()) {
            timeUpSoundPlayer.pause();
        }

        soundVolume = SettingsSaveStatesHelper.getSoundVolume(context) / 10;
        int correctSoundId = R.raw.correct_sound;
        correctSoundPlayer = MediaPlayer.create(context, correctSoundId);
        if (correctSoundPlayer != null) {
            correctSoundPlayer.setVolume(soundVolume, soundVolume);
            correctSoundPlayer.start();
            correctSoundPlayer.setOnCompletionListener(mediaPlayer -> {
                correctSoundPlayer.release();
                correctSoundPlayer = null;
            });
        }
    }

    /**
     * Plays the sound effect for incorrect answer
     *
     * @param context Context of the activity
     */
    public void playIncorrectSound(Context context) {
        if (timeUpSoundPlayer != null && timeUpSoundPlayer.isPlaying()) {
            timeUpSoundPlayer.pause();
        }

        soundVolume = SettingsSaveStatesHelper.getSoundVolume(context) / 10;
        int incorrectSoundId = R.raw.incorrect_sound;
        incorrectSoundPlayer = MediaPlayer.create(context, incorrectSoundId);
        if (incorrectSoundPlayer != null) {
            incorrectSoundPlayer.setVolume(soundVolume, soundVolume);
            incorrectSoundPlayer.start();
            incorrectSoundPlayer.setOnCompletionListener(mediaPlayer -> {
                incorrectSoundPlayer.release();
                incorrectSoundPlayer = null;
            });
        }
    }

    /**
     * Plays the sound effect for times up
     *
     * @param context Context of the activity
     */
    public void playTimeUpSound(Context context) {
        soundVolume = SettingsSaveStatesHelper.getSoundVolume(context) / 10;
        int timeUpSoundId = R.raw.times_up_sound;
        timeUpSoundPlayer = MediaPlayer.create(context, timeUpSoundId);
        if (timeUpSoundPlayer != null) {
            timeUpSoundPlayer.setVolume(soundVolume, soundVolume);
            timeUpSoundPlayer.start();
            timeUpSoundPlayer.setOnCompletionListener(mediaPlayer -> {
                timeUpSoundPlayer.release();
                timeUpSoundPlayer = null;
            });
        }
    }

    /**
     * Releases all the media players and sets them to null
     */
    public void cancelAll() {
        canceled = true;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
                mediaPlayer = null;
            });
            mediaPlayer.stop();
        }
        if (correctSoundPlayer != null && correctSoundPlayer.isPlaying()) {
            correctSoundPlayer.setOnCompletionListener(mp -> {
                correctSoundPlayer.release();
                correctSoundPlayer = null;
            });
            correctSoundPlayer.stop();
        }
        if (incorrectSoundPlayer != null && incorrectSoundPlayer.isPlaying()) {
            incorrectSoundPlayer.setOnCompletionListener(mp -> {
                incorrectSoundPlayer.release();
                incorrectSoundPlayer = null;
            });
            incorrectSoundPlayer.stop();
        }
        if (timeUpSoundPlayer != null && timeUpSoundPlayer.isPlaying()) {
            timeUpSoundPlayer.setOnCompletionListener(mp -> {
                timeUpSoundPlayer.release();
                timeUpSoundPlayer = null;
            });
            timeUpSoundPlayer.stop();
        }
    }

    /**
     * Gets the music based on the activity
     *
     * @param activity Activity
     */
    public void getMusicBasedOnActivity(Class<?> activity) {
        if (activity == QuestionActivity.class) {
            musicId = R.raw.main_music;
        }
    }

}