package com.knowledgerealm;

import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.knowledgerealm.handlers.OnResultActivityHandler;

/**
 * Activity for displaying the result and ranking of the user
 */
public class ResultActivity extends AppCompatActivity {

    // UI elements
    private TextView correctAnswersTextView;
    private TextView congratulationText;
    private ImageButton backBtn;

    // Other UI elements
    private View usersRankLayout;
    private TextView usersRankTextView;
    ImageView congratsImage;

    // Other variables
    private OnResultActivityHandler onResultActivityHandler;
    private int correctAnswers;
    private int maxQuestions;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize UI elements
        correctAnswersTextView = findViewById(R.id.congratulate_text_score);
        congratulationText = findViewById(R.id.congratulate_text);
        backBtn = findViewById(R.id.back_btn);
        usersRankLayout = findViewById(R.id.users_rank_layout);
        usersRankTextView = findViewById(R.id.users_rank_text);
        congratsImage = findViewById(R.id.congratulate_image);

        // Get data from intent
        Intent intent = getIntent();
        correctAnswers = intent.getIntExtra("correctAnswers", 0);
        maxQuestions = intent.getIntExtra("maxQuestions", 0);
        points = intent.getIntExtra("points", 0);

        onResultActivityHandler = new OnResultActivityHandler(points, correctAnswers, maxQuestions);

        // Set listeners and update UI
        setButtonListeners();
        setCongratulateTextAndImageBasedOnPerformance();
        setCorrectAnswersTextAndRanking();
    }

    /**
     * Set the congratulation text and image based on the performance
     */
    private void setCongratulateTextAndImageBasedOnPerformance() {
        onResultActivityHandler.setUserScoreOnDatabaseAndGetRank();

        // Set the congratulation text
        congratulationText.setText(onResultActivityHandler.getCongratulateTextBasedOnPerformance());

        // Set the congratulation image
        congratsImage.setImageResource(onResultActivityHandler.getUserImageBasedOnPerformance());
    }

    /**
     * Set the text for correct answers and update user rank
     */
    @SuppressLint("DefaultLocale")
    private void setCorrectAnswersTextAndRanking() {
        correctAnswersTextView.setText(format("You got %d/%d correct!", correctAnswers, maxQuestions));
        setUsersRanking();
    }

    /**
     * Sets the user's rank on the UI
     */
    private void setUsersRanking() {
        final Handler handler = new Handler();
        final int delay = 500;
        final int timeout = 7000;

        final boolean[] rankRetrieved = {false};

        Runnable rankCheckRunnable = new Runnable() {
            private final long startTime = System.currentTimeMillis();

            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (!rankRetrieved[0] && onResultActivityHandler.userRank != 0) {
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(ResultActivity.this, R.anim.fade_in);
                    usersRankLayout.startAnimation(fadeInAnimation);
                    usersRankLayout.setVisibility(View.VISIBLE);
                    usersRankTextView.setText(format("You are ranked on the %s place!", OnResultActivityHandler.formatUserRank(onResultActivityHandler.userRank)));

                    rankRetrieved[0] = true;
                }

                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= timeout || rankRetrieved[0]) handler.removeCallbacks(this);
                else handler.postDelayed(this, delay);
            }
        };

        handler.postDelayed(rankCheckRunnable, delay);
    }

    /**
     * Set button listeners for back button and leaderboards button
     */
    public void setButtonListeners() {
        // Set the back button listener
        backBtn.setOnClickListener(view -> startActivity(new Intent(ResultActivity.this, MainActivity.class)));

        // Set the leaderboards button listener
        FloatingActionButton leaderboardsBtn = findViewById(R.id.leaderboard_btn);
        leaderboardsBtn.setOnClickListener(view -> startActivity(new Intent(ResultActivity.this, LeaderBoardActivity.class)));
    }

    /**
     * Override the back button to go to the main activity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
    }

}
