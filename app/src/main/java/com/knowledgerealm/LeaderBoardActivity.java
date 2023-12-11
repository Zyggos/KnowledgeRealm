package com.knowledgerealm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.knowledgerealm.handlers.UserAdapterHandler;
import com.knowledgerealm.helpers.UiHelpers;
import com.knowledgerealm.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity to display the leaderboard.
 */
public class LeaderBoardActivity extends AppCompatActivity {
    private List<User> userList;
    private RecyclerView recyclerView;
    private UserAdapterHandler userAdapter;
    private Dialog progressDialog;
    private ImageView backBtn;

    private TextView usernameFirst;
    private TextView usernameSecond;
    private TextView usernameThird;

    private TextView scoreFirst;
    private TextView scoreSecond;
    private TextView scoreThird;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Firebase Database persistence
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_leaderboard);

        // Initialize views and variables
        backBtn = findViewById(R.id.back);
        userList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapterHandler(userList);
        recyclerView.setAdapter(userAdapter);

        usernameFirst = findViewById(R.id.usernameFirstTextView);
        usernameSecond = findViewById(R.id.usernameSecondTextView);
        usernameThird = findViewById(R.id.usernameThirdTextView);

        scoreFirst = findViewById(R.id.scoreFirstTextView);
        scoreSecond = findViewById(R.id.scoreSecondTextView);
        scoreThird = findViewById(R.id.scoreThirdTextView);

        // Show progress indicator
        progressDialog = UiHelpers.showProgressIndicator(this, "Loading scores, keep tight...");

        // Retrieve leaderboard data from Firebase
        Query usernameQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .orderByChild("score")
                .startAfter(0)
                .limitToLast(25);

        addListeners(usernameQuery);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove listeners to prevent memory leaks
        if (databaseReference != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addListeners(Query usernameQuery) {
        // Add listener for username query
        usernameQuery.addValueEventListener(valueEventListener);

        // Add listener for back button
        backBtn.setOnClickListener(v -> {
            backBtn.setBackground(getDrawable(R.drawable.round_back_white_active));
            startActivity(new Intent(LeaderBoardActivity.this, MainActivity.class));
        });
    }

    /**
     * Override the back button behavior to go back to the main activity.
     */
    @Override
    public void onBackPressed() {
        databaseReference.removeEventListener(valueEventListener);
        databaseReference = null;

        startActivity(new Intent(LeaderBoardActivity.this, MainActivity.class));
    }

    /**
     * ValueEventListener to retrieve leaderboard data from Firebase.
     */
    ValueEventListener valueEventListener = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userList.clear();
            if (dataSnapshot.exists()) {
                // Iterate over the leaderboard data and add it to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                Collections.reverse(userList);
                userAdapter.notifyDataSetChanged();

                if (!userList.isEmpty()) {
                    // Update the top three users in the leaderboard
                    addFirstThreeUsersToLeaderboard();
                    scrollToMyScore();
                }
            }

            // Dismiss the progress indicator
            dismissProgressDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    /**
     * Update the top three users in the leaderboard view.
     */
    private void addFirstThreeUsersToLeaderboard() {
        try {
            // Set the first user
            usernameFirst.setText(userList.get(0).getUsername());
            scoreFirst.setText(String.valueOf(userList.get(0).getScore()));
            userList.remove(0);

            // Set the second user
            usernameSecond.setText(userList.get(0).getUsername());
            scoreSecond.setText(String.valueOf(userList.get(0).getScore()));
            userList.remove(0);

            // Set the third user
            usernameThird.setText(userList.get(0).getUsername());
            scoreThird.setText(String.valueOf(userList.get(0).getScore()));
            userList.remove(0);
        } catch (Exception e) {
            Log.e("LeaderboardActivity", "Not enough users to fill the leaderboard");
        }
    }

    /**
     * Scroll to the current user's position in the leaderboard.
     */
    private void scrollToMyScore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentEmail = currentUser.getEmail();

            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);

                if (user.getEmail().equals(currentEmail)) {
                    recyclerView.smoothScrollToPosition(i);
                    break;
                }
            }
        }
    }


    /**
     * Dismiss the progress indicator dialog.
     */
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
