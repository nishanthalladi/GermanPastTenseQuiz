package com.example.android.germanpasttensequiz;

/**
 * Created by nishanth on 9/11/18.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue, mDifficultyEasy;

    public Question(int mTextResId, boolean mAnswerTrue, boolean mDifficultyEasy) {
        this.mTextResId = mTextResId;
        this.mAnswerTrue = mAnswerTrue;
        this.mDifficultyEasy = mDifficultyEasy;

    }

    public boolean isDifficultyEasy() {
        return mDifficultyEasy;
    }

    public void setDifficultyEasy(boolean difficultyEasy) {
        mDifficultyEasy = difficultyEasy;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
