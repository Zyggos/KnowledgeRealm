package com.knowledgerealm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.knowledgerealm.helpers.UiHelpers;
import com.knowledgerealm.models.User;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText userNameET, emailET, passwordET;
    private LinearLayout userNameLayout, emailLayout, passwordLayout;
    private ImageView userNameIcon, emailIcon, passwordIcon;
    private TextView userNameTxt, emailTxt, passwordTxt, signInBtn, signUpBtn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameET = findViewById(R.id.userNameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        userNameLayout = findViewById(R.id.userNameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        userNameIcon = findViewById(R.id.userNameIcon);
        emailIcon = findViewById(R.id.emailIcon);
        passwordIcon = findViewById(R.id.passwordIcon);
        userNameTxt = findViewById(R.id.usernameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set the listeners
        setListeners();
    }

    /**
     * Checks if the password is valid.
     *
     * @param password the password string to check.
     */
    private void checkUsernameAvailability(String email, String username, String password) {
        Query usernameQuery = FirebaseDatabase.getInstance("https://knowledgerealm-38cc9-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").orderByChild("username").equalTo(username);
        Dialog progressDialog = UiHelpers.showProgressIndicator(this, "Checking username availability...");

        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    userNameET.setError("Username is taken");
                    userNameET.requestFocus();
                } else {
                    createUserWithEmailAndPassword(email, password, username, progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss(); // Dismiss progress dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage(error.getMessage()).setTitle("An error occurred").setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    /**
     * Creates a new user with email and password.
     *
     * @param email    the email of the user.
     * @param password the password of the user.
     * @param username the username of the user.
     */
    private void createUserWithEmailAndPassword(String email, String password, String username, Dialog progressDialog) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                User user = new User(username, email);
                current_user_db.setValue(user);
                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss(); // Dismiss progress dialog
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            } else {
                progressDialog.dismiss(); // Dismiss progress dialog
                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Adds a new user to the database.
     *
     * @param email    the email of the user.
     * @param username the username of the user.
     * @param password the password of the user.
     */
    private void addToDatabase(String email, String username, String password) {
        checkUsernameAvailability(email, username, password);
    }

    /**
     * Creates a new user.
     */
    private void createUser() {
        String email = Objects.requireNonNull(emailET.getText()).toString();
        String username = Objects.requireNonNull(userNameET.getText()).toString();
        String password = Objects.requireNonNull(passwordET.getText()).toString();

        if (isEmailEmpty(email)) {
            showEmailEmptyError();
        } else if (isPasswordEmpty(password)) {
            showPasswordEmptyError();
        } else if (!isPasswordValid(password)) {
            showInvalidPasswordError();
        } else {
            addToDatabase(email, username, password);
        }
    }

    /**
     * Checks if the email is empty.
     *
     * @param email the email string to check.
     * @return true if the email is empty, false otherwise.
     */
    private boolean isEmailEmpty(String email) {
        return TextUtils.isEmpty(email);
    }

    /**
     * Shows an error message if the email is empty.
     */
    private void showEmailEmptyError() {

        emailET.setError("Email cannot be empty");
        emailET.requestFocus();
    }

    /**
     * Checks if the password is empty.
     *
     * @param password the password string to check.
     * @return true if the password is empty, false otherwise.
     */
    private boolean isPasswordEmpty(String password) {
        return TextUtils.isEmpty(password);
    }

    /**
     * Shows an error message if the password is empty.
     */
    private void showPasswordEmptyError() {
        passwordET.setError("Password cannot be empty");
        passwordET.requestFocus();
    }

    /**
     * Shows an error message if the password is invalid.
     */
    private void showInvalidPasswordError() {
        passwordET.setError("Password must be at least 8 characters, have at least a letter, a number and a symbol");
        passwordET.requestFocus();
    }

    /**
     * Validates a given password string.
     *
     * @param password τhe password string to validate.
     * @return true if the password is valid, false otherwise.
     */
    private boolean isPasswordValid(String password) {
        final int MIN_PASSWORD_LENGTH = 8;

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        Pattern letterPattern = Pattern.compile("[a-zA-z]");
        Pattern digitPattern = Pattern.compile("[0-9]");
        Pattern specialPattern = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        Matcher letterMatcher = letterPattern.matcher(password);
        Matcher digitMatcher = digitPattern.matcher(password);
        Matcher specialMatcher = specialPattern.matcher(password);

        return letterMatcher.find() && digitMatcher.find() && specialMatcher.find();
    }

    /**
     * Sets the listeners.
     */
    private void setListeners() {
        // setting click listener on register button
        signUpBtn.setOnClickListener(v -> createUser());

        // setting click listener on sign in button
        signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        // setting click listener on register button
        userNameET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // showing keyboard on userName EditText
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(userNameET, InputMethodManager.SHOW_IMPLICIT);

                // selecting username
                selectView("userName");
            }
        });

        // setting click listener on register button
        emailET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // showing keyboard on email EditText
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(emailET, InputMethodManager.SHOW_IMPLICIT);

                // selecting email
                selectView("email");
            }
        });

        // setting click listener on register button
        passwordET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // showing keyboard on password EditText
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(passwordET, InputMethodManager.SHOW_IMPLICIT);

                // selecting password
                selectView("password");
            }
        });
    }

    /**
     * This method is called when user click on register button
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Selects the view based on the given type.
     *
     * @param type The type of the view to be selected.
     */
    private void selectView(String type) {
        resetFields();
        TextView selectedTxt = null;

        switch (type) {
            case "userName":
                selectedTxt = userNameTxt;
                userNameET.setHint("");
                userNameET.setTypeface(null, Typeface.BOLD);
                userNameTxt.setVisibility(View.VISIBLE);
                userNameET.setTextSize(16);
                userNameLayout.setBackgroundResource(R.drawable.round_back_white);
                userNameIcon.setImageResource(R.drawable.selected_user_icon);
                break;
            case "email":
                selectedTxt = emailTxt;
                emailET.setHint("");
                emailET.setTypeface(null, Typeface.BOLD);
                emailTxt.setVisibility(View.VISIBLE);
                emailET.setTextSize(16);
                emailLayout.setBackgroundResource(R.drawable.round_back_white);
                emailIcon.setImageResource(R.drawable.selected_email_icon);
                break;
            case "password":
                selectedTxt = passwordTxt;
                passwordET.setHint("");
                passwordET.setTypeface(null, Typeface.BOLD);
                passwordTxt.setVisibility(View.VISIBLE);
                passwordET.setTextSize(16);
                passwordLayout.setBackgroundResource(R.drawable.round_back_white);
                passwordIcon.setImageResource(R.drawable.selected_password_icon);
                break;
        }

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 50, 0);
        translateAnimation.setDuration(200);

        assert selectedTxt != null;

        selectedTxt.startAnimation(translateAnimation);
    }

    /**
     * Resets the fields to their initial state.
     */
    private void resetFields() {
        userNameET.setHint("USERNAME");
        userNameET.setTextSize(12);
        if (userNameET.getText().toString().isEmpty()) {
            userNameTxt.setVisibility(View.GONE);
            userNameET.setTypeface(null, Typeface.NORMAL);
        }
        userNameIcon.setImageResource(R.drawable.user_icon);
        userNameLayout.setBackgroundColor(getColor(android.R.color.transparent));

        passwordET.setHint("PASSWORD");
        passwordET.setTextSize(12);
        if (passwordET.getText().toString().isEmpty()) {
            passwordTxt.setVisibility(View.GONE);
            passwordET.setTypeface(null, Typeface.NORMAL);
        }
        passwordIcon.setImageResource(R.drawable.password_icon);
        passwordLayout.setBackgroundColor(getColor(android.R.color.transparent));

        emailET.setHint("EMAIL");
        emailET.setTextSize(12);
        if (emailET.getText().toString().isEmpty()) {
            emailTxt.setVisibility(View.GONE);
            emailET.setTypeface(null, Typeface.NORMAL);
        }
        emailIcon.setImageResource(R.drawable.email_icon);
        emailLayout.setBackgroundColor(getColor(android.R.color.transparent));
    }

}