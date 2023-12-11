package com.knowledgerealm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.knowledgerealm.helpers.UiHelpers;

public class MainActivity extends AppCompatActivity {
    RelativeLayout play, leaderBoards, settings;

    // Firebase Auth
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        play = findViewById(R.id.PlayLayout);
        settings = findViewById(R.id.SettingsLayout);
        leaderBoards = findViewById(R.id.LeaderboardsLayout);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set Listeners
        setListeners();
    }

    /**
     * On start check if the user is logged in or not, if not then redirect to login activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    /**
     * Set the background of the buttons to white when the activity resumes
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onResume() {
        super.onResume();
        play.setBackground(getDrawable(R.drawable.round_back_white));
        settings.setBackground(getDrawable(R.drawable.round_back_white));
        leaderBoards.setBackground(getDrawable(R.drawable.round_back_white));
    }

    /**
     * On back pressed, ask the user if he wants to exit the app
     */
    @Override
    public void onBackPressed() {
       UiHelpers.showExitAppDialog(this);
    }

    /**
     * Set the listeners for the "buttons" (RelativeLayouts actually)
     */
    private void setListeners() {
        // Set listener for play button
        play.setOnClickListener(view ->
        {
            play.setBackground(getDrawable(R.drawable.round_back_white_active));
            startActivity(new Intent(MainActivity.this, QuestionActivity.class));
        });

        // Set listener for settings button
        settings.setOnClickListener(view ->
        {
            settings.setBackground(getDrawable(R.drawable.round_back_white_active));
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        // Set listener for leaderboards button
        leaderBoards.setOnClickListener(view ->
        {
            leaderBoards.setBackground(getDrawable(R.drawable.round_back_white_active));
            startActivity(new Intent(MainActivity.this, LeaderBoardActivity.class));
        });
    }
}