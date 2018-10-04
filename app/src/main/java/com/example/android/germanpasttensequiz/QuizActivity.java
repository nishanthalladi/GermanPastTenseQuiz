package com.example.android.germanpasttensequiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton, mFalseButton, mNextButton, mHintButton;
    private TextView mQuestionTextView, mDifficultyTextView, mScoreTextView;
    private int mCurrentIndex = 0, mScore = 0;
    private long mFirebaseHighscore;
    private boolean mScoreLock;
    private ProgressBar mProgressBar;
    private ArrayList<Question> mQuestionBank = new ArrayList<>(Arrays.asList(
            new Question(R.string.q1, true, true),
            new Question(R.string.q2, false, false),
            new Question(R.string.q3, false, true),
            new Question(R.string.q4, false, true),
            new Question(R.string.q5, true, false),
            new Question(R.string.q6, false, true),
            new Question(R.string.q7, false, false),
            new Question(R.string.q8, false, false),
            new Question(R.string.q9, true, true),
            new Question(R.string.q10, false, true)));
    private Toast t;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private DatabaseReference mDatabase;

    private void checkAnswer(boolean userPressed) {
        if (t!=null)
            t.cancel();
        boolean bool = userPressed == mQuestionBank.get(mCurrentIndex).isAnswerTrue();
        if (!mScoreLock && bool) {
            mScore+=1;
            mEditor.putInt("highscore", mScore > mSharedPreferences.getInt("highscore", -1)? mScore: mSharedPreferences.getInt("highscore", -1));
            mEditor.commit();
            mDatabase.setValue(mScore > mFirebaseHighscore ? mScore : mFirebaseHighscore);
            mScoreTextView.setText("Score: "+ mScore + " Highscore: " + mFirebaseHighscore);

        }
        mScoreLock = true;
        int f = (bool? R.string.correct_toast: R.string.wrong_toast);
        t = Toast.makeText(getApplicationContext(), f, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference("highscore");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    long x = (Long) (dataSnapshot.getValue());
                    mFirebaseHighscore = mScore > x ? mScore : x;
                }
                catch (NullPointerException e) {
                    mDatabase.setValue(mScore);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error:", databaseError +"");
                Toast.makeText(getApplicationContext(), "no work", Toast.LENGTH_SHORT).show();
            }
        });

        mSharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mEditor.putInt("highscore", 0);
        mEditor.commit();

        mScoreTextView = findViewById(R.id.score);
        mScoreTextView.setText("Score: " + mScore + " Highscore: " + mSharedPreferences.getInt("highscore", 0));

        mDifficultyTextView = findViewById(R.id.difficulty);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(mQuestionBank.size());

        mHintButton = findViewById(R.id.hint_button);

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setText(mQuestionBank.get(mCurrentIndex).getTextResId());

        mDifficultyTextView.setText((mQuestionBank.get(mCurrentIndex).isDifficultyEasy())? "difficulty: easy" : "difficulty: hard");

        mNextButton = findViewById(R.id.next_button);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScoreLock = false;
                mCurrentIndex++;
                if (mCurrentIndex >= mQuestionBank.size()) {
                    mCurrentIndex = 0;
                    mScore = 0;
                }
                mDatabase.setValue(mScore > mFirebaseHighscore ? mScore : mFirebaseHighscore);
                mScoreTextView.setText("Score: "+ mScore + " Highscore: " + mFirebaseHighscore);
                mProgressBar.setProgress(mCurrentIndex);
                mQuestionTextView.setText(mQuestionBank.get(mCurrentIndex).getTextResId());
                mDifficultyTextView.setText((mQuestionBank.get(mCurrentIndex).isDifficultyEasy())? "difficulty: easy" : "difficulty: hard");
            }
        });

        mHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HintActivity.class);
                i.putExtra("answer", mQuestionBank.get(mCurrentIndex).isAnswerTrue());
                startActivity(i);
                mScoreLock = true;
            }
        });
    }
}
