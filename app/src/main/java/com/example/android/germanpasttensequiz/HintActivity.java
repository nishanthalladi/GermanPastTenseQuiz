package com.example.android.germanpasttensequiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class HintActivity extends AppCompatActivity {
    private Button mShowButton;
    private TextView mAnswer;
    private boolean answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        Intent i = getIntent();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) (displayMetrics.heightPixels * 0.3);
        int width = (int) ( displayMetrics.widthPixels * 0.6);

        getWindow().setLayout(width, height);
        answer = i.getBooleanExtra("answer", false);
        mShowButton = findViewById(R.id.show_answer_button);
        mAnswer = findViewById(R.id.answer_text_view);

        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = answer+"".toUpperCase();
                mAnswer.setText(x);
            }
        });
    }
}
