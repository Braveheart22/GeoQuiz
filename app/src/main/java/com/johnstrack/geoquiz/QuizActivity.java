package com.johnstrack.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWERED = "answered";
    private static final String KEY_CORRECT = "correct";
    private static final String KEY_BUTTON_STATE = "button_state";
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private int questionsAnswered = 0;
    private int questionsCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState !=   null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            questionsAnswered = savedInstanceState.getInt(KEY_ANSWERED, 0);
            questionsCorrect = savedInstanceState.getInt(KEY_CORRECT, 0);
       }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        if (savedInstanceState !=   null) {
            setButtonState(savedInstanceState.getBoolean(KEY_BUTTON_STATE, true));
        }

        ImageButton nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                setButtonState(true);
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                startActivity(intent);
            }
        });

        ImageButton prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length;
                }
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
                setButtonState(true);
            }
        });

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: CALLED");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_ANSWERED, questionsAnswered);
        savedInstanceState.putInt(KEY_CORRECT, questionsCorrect);
        savedInstanceState.putBoolean(KEY_BUTTON_STATE, mFalseButton.isEnabled());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (questionsAnswered == mQuestionBank.length) {
            double score = ((double)questionsCorrect / (double)questionsAnswered) * 100;
            String message = "You scored " + String.format(Locale.getDefault(),"%.0f", score) + "%";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            questionsAnswered = 0;
            questionsCorrect = 0;
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            questionsAnswered ++;
            questionsCorrect ++;
        } else {
            messageResId = R.string.incorrect_toast;
            questionsAnswered ++;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        setButtonState(false);
    }

    private void setButtonState (boolean buttonState) {
        mFalseButton.setEnabled(buttonState);
        mTrueButton.setEnabled(buttonState);
    }
}