package com.knowledgerealm;

import static com.knowledgerealm.helpers.UiHelpers.showExitGamePlayDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.knowledgerealm.handlers.OnQuestionActivityHandler;
import com.knowledgerealm.helpers.InternetConnectivityHelper;
import com.knowledgerealm.helpers.UiHelpers;
import com.knowledgerealm.models.TriviaQuestion;

import java.util.ArrayList;

/**
 * Activity for the questions
 */
public class QuestionActivity extends BaseMusicActivity {

    // UI elements
    private TextView questionText;
    private Button answerBtn;
    private LinearLayout answersLayout;
    private ProgressBar questionsProgressBar;
    private ProgressBar timeProgressBarForEachQuestion;
    private TextView pointsText;
    private ImageView coinsImage;
    private ImageButton backBtn;
    TextView difficultyText;
    TextView categoryText;
    Dialog progressDialog;
    LinearLayout gameInfoLayout;
    Toolbar toolbar;

    // Other variables
    private OnQuestionActivityHandler onQuestionActivityHandler;
    private InternetConnectivityHelper internetConnectivityHelper;
    private ArrayList<Button> answerButtons;
    private int currentQuestionNumber;
    private int allPoints;
    private int correctAnswers;
    private CountDownTimer countDownTimer;
    private boolean timeIsUpPlayed = false;

    // Constants
    private final int TIME_DELAY = 1500;
    private final int MAX_QUESTIONS = 10;
    private final int MAX_TIME_EACH_QUESTION = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Initialize UI elements
        questionText = findViewById(R.id.question_text);
        answerBtn = findViewById(R.id.answer_btn);
        answersLayout = findViewById(R.id.answers_layout);
        questionsProgressBar = findViewById(R.id.questions_progress_bar);
        timeProgressBarForEachQuestion = findViewById(R.id.time_progress_bar);
        gameInfoLayout = findViewById(R.id.game_info_layout);
        pointsText = findViewById(R.id.points_text);
        coinsImage = findViewById(R.id.coins_image);
        backBtn = findViewById(R.id.back_btn);
        difficultyText = findViewById(R.id.difficulty_text);
        categoryText = findViewById(R.id.category_text);
        toolbar = findViewById(R.id.toolbar);

        // Initialize other variables
        answerBtn.setVisibility(Button.GONE);
        questionsProgressBar.setMax(MAX_QUESTIONS - 1);
        questionsProgressBar.setEnabled(false);
        questionsProgressBar.setProgress(0);
        pointsText.setText("0");

        internetConnectivityHelper = new InternetConnectivityHelper(this);
        answerButtons = new ArrayList<>();
        currentQuestionNumber = 0;
        allPoints = 0;
        correctAnswers = 0;

        // load the question and play the game
        hideAllUIElements();
        progressDialog = UiHelpers.showProgressIndicator(this, "Loading the game...");
        new Thread(this::loadTheQuestionFromTheApi).start();
    }

    /**
     * On back button pressed, it shows a dialog for exiting the game or not
     */
    @Override
    public void onBackPressed() {
        showExitGamePlayDialog(this, exit -> {
            if (exit) {
                countDownTimer.cancel();
                cancelAll();
                finish();
            }
        });

    }


    /**
     * Loads the question and plays the game
     */
    private void loadTheQuestionFromTheApi() {
        onQuestionActivityHandler = new OnQuestionActivityHandler(this, MAX_QUESTIONS);
        runOnUiThread(() -> {
            showAllUIElements();
            setButtonListeners();
            checkInternetConnectionAndBegin();
            progressDialog.dismiss();
        });
    }

    /**
     * Checks if the is internet connection and if there is, it loads the questions
     * otherwise it shows a dialog for no internet connection
     */
    private void checkInternetConnectionAndBegin() {
        if (internetConnectivityHelper.isInternetConnected()) {
            nextQuestion();
        } else {
            hideAllUIElements();
            internetConnectivityHelper.showNoInternetConnectionDialog(MainActivity.class);
        }
    }

    /**
     * Starts the countdown timer for the question with a mutable time
     */
    public void startCountdownTimeForQuestion() {
        // get the estimated time to read the question and its answer choices
        int waitTimeForEachQuestion = onQuestionActivityHandler.estimateReadTime(currentQuestionNumber, MAX_TIME_EACH_QUESTION);

        // set the progress bar for the time
        timeProgressBarForEachQuestion.setMax(waitTimeForEachQuestion);
        timeProgressBarForEachQuestion.setProgress(waitTimeForEachQuestion);

        // start the countdown timer
        countDownTimer = new CountDownTimer(waitTimeForEachQuestion, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeProgressBarForEachQuestion.setProgress((int) millisUntilFinished);

                if (millisUntilFinished < 3000 && !timeIsUpPlayed) {
                    timeIsUpPlayed = true;
                    playTimeUpSound();
                }
            }

            @Override
            public void onFinish() {
                checkAnswerAndDelayNextQuestion(currentQuestionNumber, false);
            }

        }.start();
    }

    /**
     * Calls the next question
     */
    private void nextQuestion() {
        if (currentQuestionNumber == MAX_QUESTIONS) {
            Intent resultIntent = new Intent(this, ResultActivity.class);
            resultIntent.putExtra("points", allPoints);
            resultIntent.putExtra("correctAnswers", correctAnswers);
            resultIntent.putExtra("maxQuestions", MAX_QUESTIONS);
            startActivity(resultIntent);
            finish();
            return;
        }

        renewGameInfo();
        beginMusic();
        setQuestion(currentQuestionNumber);
        startCountdownTimeForQuestion();
        checkAnswerAndDelayNextQuestion(currentQuestionNumber, true);
    }

    /**
     * Set the question text and answer buttons
     *
     * @param questionNumber the index of the question in the list
     */
    private void setQuestion(Integer questionNumber) {
        TriviaQuestion question = onQuestionActivityHandler.getQuestion(questionNumber);
        if (question != null) {
            questionText.setText(question.getQuestion());

            answersLayout.removeAllViews();
            answerButtons.clear();

            for (String answer : question.getAllAnswersRandomized()) {
                Button answerButton = new Button(new ContextThemeWrapper(this, R.style.AnswerButton), null, 0);
                answerButton.setLayoutParams(answerBtn.getLayoutParams());
                answerButton.setText(answer);

                answerButtons.add(answerButton);
                answersLayout.addView(answerButton);
            }
        } else {
            Log.e("QuestionActivity", "Error displaying answer choices");
        }
    }

    /**
     * Renew the game info (difficulty and category)
     */
    private void renewGameInfo() {
        timeIsUpPlayed = false;
        difficultyText.setText(onQuestionActivityHandler.getDifficulty(currentQuestionNumber));
        categoryText.setText(onQuestionActivityHandler.getCategory(currentQuestionNumber));
    }

    /**
     * Checks the answer (if it was answered) and delays the next question
     *
     * @param questionNumber the index of the question in the list
     * @param wasAnswered    true if the question was answered, false otherwise
     */
    private void checkAnswerAndDelayNextQuestion(Integer questionNumber, boolean wasAnswered) {
        if (!wasAnswered) {
            setAllButtonsToNoClickable();
            highlightAnswerButtons(null);

            delayNextQuestion();
            return;
        }

        // Check if the answer is correct
        for (Button answerButton : answerButtons) {
            answerButton.setOnClickListener(v -> {
                countDownTimer.cancel();

                if (onQuestionActivityHandler.checkAnswer(questionNumber, answerButton.getText().toString())) {
                    setPointsOnCorrectAnswer();
                    playCorrectSound();
                    correctAnswers++;
                } else {
                    playIncorrectSound();
                }

                setAllButtonsToNoClickable();
                highlightAnswerButtons(answerButton);

                delayNextQuestion();
            });
        }
    }

    /**
     * Delays the next question and updates the screen
     */
    private void delayNextQuestion() {
        new Thread(() -> {
            try {
                Thread.sleep(TIME_DELAY);
            } catch (InterruptedException e) {
                Log.e("QuestionActivity", "Error delaying next question");
            }
            updateScreen();
            runOnUiThread(this::nextQuestion);
        }).start();
    }

    /**
     * Sets the points on the screen based on the current question
     */
    private void setPointsOnCorrectAnswer() {
        allPoints += onQuestionActivityHandler.getPoints(currentQuestionNumber);
        pointsText.setText(String.valueOf(allPoints));
    }

    /**
     * Updates the progress bar and the current question number
     */
    private void updateScreen() {
        currentQuestionNumber++;
        questionsProgressBar.setProgress(currentQuestionNumber);
    }

    /**
     * Highlights the correct answer and the incorrect answer
     *
     * @param answerButton the button that was clicked
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void highlightAnswerButtons(Button answerButton) {
        if (answerButton == null) {
            for (Button button : answerButtons) {
                if (button.getText().toString().equals(onQuestionActivityHandler.getCorrectAnswer(currentQuestionNumber)))
                    button.setBackground(getDrawable(R.drawable.correct_answer_button_bg));
                else button.setBackground(getDrawable(R.drawable.disabled_answer_button_bg));
            }
            return;
        }

        for (Button button : answerButtons) {
            if (button.getText().toString().equals(onQuestionActivityHandler.getCorrectAnswer(currentQuestionNumber)))
                button.setBackground(getDrawable(R.drawable.correct_answer_button_bg));
            else if (button.getText().toString().equals(answerButton.getText().toString()))
                button.setBackground(getDrawable(R.drawable.incorrect_answer_button_bg));
            else button.setBackground(getDrawable(R.drawable.disabled_answer_button_bg));
        }
    }

    /**
     * Sets all the answer buttons to not clickable
     */
    private void setAllButtonsToNoClickable() {
        for (Button button : answerButtons)
            button.setClickable(false);
    }

    /**
     * Sets the listeners for the buttons
     */
    private void setButtonListeners() {
        // Set the listener for the back button
        backBtn.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Hide all ui elements
     */
    private void hideAllUIElements() {
        questionText.setVisibility(View.GONE);
        difficultyText.setVisibility(View.GONE);
        categoryText.setVisibility(View.GONE);
        timeProgressBarForEachQuestion.setVisibility(View.GONE);
        pointsText.setVisibility(View.GONE);
        questionsProgressBar.setVisibility(View.GONE);
        answersLayout.setVisibility(View.GONE);
        backBtn.setVisibility(View.GONE);
        gameInfoLayout.setVisibility(View.GONE);
        coinsImage.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
    }

    /**
     * Show all ui elements
     */
    private void showAllUIElements() {
        questionText.setVisibility(View.VISIBLE);
        difficultyText.setVisibility(View.VISIBLE);
        categoryText.setVisibility(View.VISIBLE);
        timeProgressBarForEachQuestion.setVisibility(View.VISIBLE);
        pointsText.setVisibility(View.VISIBLE);
        questionsProgressBar.setVisibility(View.VISIBLE);
        answersLayout.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.VISIBLE);
        gameInfoLayout.setVisibility(View.VISIBLE);
        coinsImage.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
    }
}