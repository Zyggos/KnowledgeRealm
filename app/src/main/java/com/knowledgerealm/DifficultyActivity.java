package com.knowledgerealm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knowledgerealm.enums.TriviaApiDifficultyEnum;
import com.knowledgerealm.helpers.InternetConnectivityHelper;
import com.knowledgerealm.helpers.SettingsSaveStatesHelper;


public class DifficultyActivity extends AppCompatActivity {
    ImageView back;
    LinearLayout allDifficultiesLayout;
    TextView stateMessage;
    TextView title;

    // Internet connectivity helper
    InternetConnectivityHelper internetConnectivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        // Initialize views
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        allDifficultiesLayout = findViewById(R.id.difficultyItemsLayout);
        stateMessage = findViewById(R.id.stateMessage);

        // Initialize other
        internetConnectivityHelper = new InternetConnectivityHelper(this);

        // Set all listeners
        setAllListeners();

        // Check internet connectivity and start
        checkInternetConnectivity();
    }

    /**
     * On back pressed go to settings activity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DifficultyActivity.this, SettingsActivity.class));
    }

    /**
     * Check if the device is connected to the internet and get all difficulties
     */
    private void checkInternetConnectivity() {
        if (internetConnectivityHelper.isInternetConnected()) {
            getDifficulties();
            selectSavedDifficultyIfAny();
        } else {
            hideAllUIElements();
            internetConnectivityHelper.showNoInternetConnectionDialog(SettingsActivity.class);
        }
    }

    /**
     * Select the saved difficulty if any
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void selectSavedDifficultyIfAny() {
        String selectedDifficulty = SettingsSaveStatesHelper.getDifficulty(DifficultyActivity.this);
        Log.i("DifficultyActivity", "Selected Difficulty: " + selectedDifficulty);
        if (selectedDifficulty != null) {
            for (int i = 0; i < allDifficultiesLayout.getChildCount(); i++) {
                View difficultyView = allDifficultiesLayout.getChildAt(i);
                TextView difficultyText = difficultyView.findViewById(R.id.itemText);
                if (difficultyText.getText().toString().equals(selectedDifficulty)) {
                    difficultyView.setBackground(getDrawable(R.drawable.round_back_white_active));
                }
            }
        }
    }

    /**
     * Get all difficulties from the API and add them to the layout
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void getDifficulties() {
        TriviaApiDifficultyEnum[] difficulties = TriviaApiDifficultyEnum.values();

        // remove the last difficulty because it is not a difficulty
        difficulties = java.util.Arrays.copyOf(difficulties, difficulties.length - 1);

        for (TriviaApiDifficultyEnum difficulty : difficulties) {
            View difficultyView = LayoutInflater.from(this).inflate(R.layout.settings_item, allDifficultiesLayout, false);
            TextView difficultyText = difficultyView.findViewById(R.id.itemText);

            difficultyText.setText(difficulty.getName());
            allDifficultiesLayout.addView(difficultyView);

            // add onClickListener to the difficulty
            difficultyView.setOnClickListener(v -> {
                // check if the difficulty is already selected
                if (difficultyView.getBackground().getConstantState() == getDrawable(R.drawable.round_back_white_active).getConstantState()) {
                    difficultyView.setBackground(getDrawable(R.drawable.round_back_white));
                    SettingsSaveStatesHelper.saveDifficulty("", DifficultyActivity.this);
                    Toast.makeText(DifficultyActivity.this, "Difficulty " + difficulty.getName() + " deselected!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // else select the difficulty
                deselectAllDifficulties();
                difficultyView.setBackground(getDrawable(R.drawable.round_back_white_active));
                SettingsSaveStatesHelper.saveDifficulty(difficulty.getName(), DifficultyActivity.this);
                Toast.makeText(DifficultyActivity.this, "Difficulty " + difficulty.getName() + " selected!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DifficultyActivity.this, SettingsActivity.class));
            });
        }
    }

    /**
     * Deselect all difficulties
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void deselectAllDifficulties() {
        for (int i = 0; i < allDifficultiesLayout.getChildCount(); i++) {
            View difficultyView = allDifficultiesLayout.getChildAt(i);
            difficultyView.setBackground(getDrawable(R.drawable.round_back_white));
        }
    }

    /**
     * Set all listeners
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setAllListeners() {
        // Set back button listener
        back.setOnClickListener(v -> {
            back.setBackground(getDrawable(R.drawable.round_back_white));
            startActivity(new Intent(DifficultyActivity.this, SettingsActivity.class));
        });
    }

    /**
     * Set to visible all UI elements
     */
    private void showAllUIElements() {
        allDifficultiesLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
    }

    /**
     * Set to gone all UI elements
     */
    private void hideAllUIElements() {
        allDifficultiesLayout.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
    }

}