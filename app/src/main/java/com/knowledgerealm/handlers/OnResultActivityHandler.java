package com.knowledgerealm.handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowledgerealm.R;

import java.util.Objects;

/**
 * Class for handling the result on the ResultActivity
 */
public class OnResultActivityHandler {
    private int points;
    private int correctAnswers;
    private int maxQuestions;
    public int userRank;

    /**
     * Constructor for the OnResultActivityHandler class
     *
     * @param points         The total points earned by the user
     * @param correctAnswers The number of correct answers
     * @param maxQuestions   The total number of questions
     */
    public OnResultActivityHandler(int points, int correctAnswers, int maxQuestions) {
        this.points = points;
        this.correctAnswers = correctAnswers;
        this.maxQuestions = maxQuestions;
    }

    /**
     * Get the congratulation text based on the performance.
     *
     * @return The congratulation text resource id
     */
    public int getCongratulateTextBasedOnPerformance() {
        if (correctAnswers >= maxQuestions * 0.9) {
            return R.string.congratulate_text_perfect;
        } else if (correctAnswers >= maxQuestions * 0.7) {
            return R.string.congratulate_text_very_good;
        } else if (correctAnswers >= maxQuestions * 0.5) {
            return R.string.congratulate_text_great;
        } else if (correctAnswers >= maxQuestions * 0.3) {
            return R.string.congratulate_text_ok;
        } else {
            return R.string.congratulate_text_bad;
        }
    }

    /**
     * Update the user's score on the database and retrieve the user's rank.
     */
    public void setUserScoreOnDatabaseAndGetRank() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://knowledgerealm-38cc9-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        mDatabase.child("Users").child(user_id).child("score").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("Firebase", "Error getting data", task.getException());
            } else {
                int overallPoints = Objects.requireNonNull(task.getResult()).getValue(Integer.class);
                int newPoints = overallPoints + points;
                mDatabase.child("Users").child(user_id).child("score").setValue(newPoints);

                mDatabase.child("Users").orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int rank = (int) dataSnapshot.getChildrenCount();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (Objects.equals(userSnapshot.getKey(), user_id)) {
                                break;
                            }
                            rank--;
                        }
                        userRank = rank;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error retrieving user's rank", databaseError.toException());
                    }
                });
            }
        });
    }

    /**
     * Gets the user's image based on the performance.
     *
     * @return The user's image resource id
     */
    public int getUserImageBasedOnPerformance() {
        if (correctAnswers >= maxQuestions * 0.9) {
            return R.drawable.result_achievement;
        } else if (correctAnswers >= maxQuestions * 0.7) {
            return R.drawable.result_achievement;
        } else if (correctAnswers >= maxQuestions * 0.5) {
            return R.drawable.result_achievement;
        } else if (correctAnswers >= maxQuestions * 0.3) {
            return R.drawable.result_achievement;
        } else {
            return R.drawable.result_achievement;
        }
    }

    /**
     * Format the user rank with the appropriate suffix
     *
     * @param rank the user rank
     * @return the formatted user rank text
     */
    public static String formatUserRank(int rank) {
        if (rank >= 11 && rank <= 13) {
            return rank + "th";
        } else if (rank % 10 == 1) {
            return rank + "st";
        } else if (rank % 10 == 2) {
            return rank + "nd";
        } else if (rank % 10 == 3) {
            return rank + "rd";
        } else {
            return rank + "th";
        }
    }
}
