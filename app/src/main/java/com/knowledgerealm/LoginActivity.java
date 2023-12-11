package com.knowledgerealm;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.knowledgerealm.helpers.UiHelpers;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, passwordET;
    private ImageView emailIcon, passwordIcon;
    private LinearLayout emailLayout, passwordLayout;
    private TextView emailTxt, passwordTxt, signInBtn, signUpBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize all the views
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        emailIcon = findViewById(R.id.emailIcon);
        passwordIcon = findViewById(R.id.passwordIcon);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        checkUsersActivity();

        // Set all the listeners
        setAllListeners();
    }

    /**
     * Get email from the email field
     *
     * @return email string
     */
    private String getEmail() {
        String email = Objects.requireNonNull(emailET.getText()).toString();
        if (TextUtils.isEmpty(email)) {
            emailET.setError("Email cannot be empty");
            emailET.requestFocus();
        }
        return email;
    }

    /**
     * Get password from the password field
     *
     * @return password string
     */
    private String getPassword() {
        String password = Objects.requireNonNull(passwordET.getText()).toString();
        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Password cannot be empty");
            passwordET.requestFocus();
        }
        return password;
    }

    /**
     * Sign in with email and password
     *
     * @param email    email string
     * @param password password string
     */
    private void signInWithEmailAndPassword(String email, String password) {
        final Dialog progressDialog = UiHelpers.showProgressIndicator(this, "Checking what you've entered"); // Show progress indicator

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss(); // Dismiss progress indicator

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    /**
     * Login user with email and password
     */
    private void loginUser() {
        String email = getEmail();
        String password = getPassword();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            signInWithEmailAndPassword(email, password);
        }
    }

    /**
     * Check if user is already logged in
     */
    private void checkUsersActivity() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    /**
     * Sets all the listeners.
     */
    private void setAllListeners() {
        // setting listener on sign up button
        signUpBtn.setOnClickListener(v -> {
            // opening register activity
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        // setting listener on email layout
        emailET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // showing keyboard on email EditText
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(emailET, InputMethodManager.SHOW_IMPLICIT);

                // selecting email
                selectView("email");
            }
        });

        // setting listener on password layout
        passwordET.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                // showing keyboard on password EditText
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(passwordET, InputMethodManager.SHOW_IMPLICIT);

                // selecting password
                selectView("password");
            }
        });

        // setting listener on sign in button
        signInBtn.setOnClickListener(view -> loginUser());

        // setting listener register button
        signUpBtn.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    /**
     * Selects a view based on the given type.
     *
     * @param type The type of the view to be selected ("email" or "password").
     */
    private void selectView(String type) {
        resetFields();

        TextView selectedTxt = null;

        switch (type) {
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
     * Resets the fields to their default values.
     */
    private void resetFields() {
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