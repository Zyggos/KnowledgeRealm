package com.knowledgerealm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knowledgerealm.handlers.TriviaApiHandler;
import com.knowledgerealm.helpers.CategoriesResponseHelper;
import com.knowledgerealm.helpers.InternetConnectivityHelper;
import com.knowledgerealm.helpers.SettingsSaveStatesHelper;
import com.knowledgerealm.helpers.UiHelpers;


public class CategoriesActivity extends AppCompatActivity {
    ImageView back;
    LinearLayout allCategoriesLayout;
    TextView stateMessage;
    Dialog progressDialog;
    TextView title;

    // Internet connectivity helper
    InternetConnectivityHelper internetConnectivityHelper;

    // Trivia api data
    CategoriesResponseHelper categoriesResponseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // Initialize views
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        allCategoriesLayout = findViewById(R.id.allCategoriesLayout);
        stateMessage = findViewById(R.id.stateMessage);

        // Initialize other
        internetConnectivityHelper = new InternetConnectivityHelper(this);

        // Set all listeners
        setAllListeners();

        // load the question and play the game
        hideAllUIElements();
        progressDialog = UiHelpers.showProgressIndicator(this, "Loading categories...");
        new Thread(this::loadTheCategoriesAndContinue).start();
    }

    /**
     * Load the categories and continue
     */
    private void loadTheCategoriesAndContinue() {
        categoriesResponseHelper = TriviaApiHandler.getAllCategories();
        runOnUiThread(() -> {
            showAllUIElements();
            checkInternetConnectivity();
            progressDialog.dismiss();
        });
    }

    /**
     * Check if the device is connected to the internet and get all categories
     */
    private void checkInternetConnectivity() {
        if (internetConnectivityHelper.isInternetConnected()) {
            getCategories();
            selectSavedCategoryIfAny();
        } else {
            hideAllUIElements();
            internetConnectivityHelper.showNoInternetConnectionDialog(SettingsActivity.class);
        }
    }

    /**
     * Select the saved category if any
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void selectSavedCategoryIfAny() {
        String selectedCategory = SettingsSaveStatesHelper.getCategory(CategoriesActivity.this);
        if (selectedCategory != null) {
            for (int i = 0; i < allCategoriesLayout.getChildCount(); i++) {
                View categoryView = allCategoriesLayout.getChildAt(i);
                TextView categoryText = categoryView.findViewById(R.id.itemText);
                if (categoryText.getText().toString().equals(selectedCategory)) {
                    categoryView.setBackground(getDrawable(R.drawable.round_back_white_active));
                }
            }
        }
    }

    /**
     * Deselect all categories
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void deselectAllCategories() {
        for (int i = 0; i < allCategoriesLayout.getChildCount(); i++) {
            View categoryView = allCategoriesLayout.getChildAt(i);
            categoryView.setBackground(getDrawable(R.drawable.round_back_white));
        }
    }

    /**
     * Get all categories from the API and add them to the layout
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void getCategories() {
        if (categoriesResponseHelper.success) {
            LayoutInflater inflater = LayoutInflater.from(this);

            for (String category : categoriesResponseHelper.categories) {
                View categoryView = inflater.inflate(R.layout.settings_item, allCategoriesLayout, false);
                TextView categoryText = categoryView.findViewById(R.id.itemText);

                categoryText.setText(category);
                allCategoriesLayout.addView(categoryView);

                // add onClickListener to the category
                categoryView.setOnClickListener(v -> {
                    // check if the category is already selected
                    if (categoryView.getBackground().getConstantState() == getDrawable(R.drawable.round_back_white_active).getConstantState()) {
                        categoryView.setBackground(getDrawable(R.drawable.round_back_white));
                        SettingsSaveStatesHelper.saveCategory("", CategoriesActivity.this);
                        Toast.makeText(CategoriesActivity.this, "Category " + category + " deselected!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // else select the category
                    deselectAllCategories();
                    categoryView.setBackground(getDrawable(R.drawable.round_back_white_active));
                    SettingsSaveStatesHelper.saveCategory(category, CategoriesActivity.this);
                    Toast.makeText(CategoriesActivity.this, "Category " + category + " selected!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CategoriesActivity.this, SettingsActivity.class));
                });
            }
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
            startActivity(new Intent(CategoriesActivity.this, SettingsActivity.class));
        });
    }

    /**
     * Set to visible all UI elements
     */
    private void showAllUIElements() {
        allCategoriesLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
    }

    /**
     * Set to gone all UI elements
     */
    private void hideAllUIElements() {
        allCategoriesLayout.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
    }

}