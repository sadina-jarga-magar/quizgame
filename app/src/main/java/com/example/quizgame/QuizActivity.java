package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extrascore";
    private TextView tvquestion,tvscore,tvquestioncount,tvcountdown;
    private RadioGroup rg;
    private RadioButton rb1, rb2, rb3;
    private Button btnconfirm;


    private List<Question> questionList;

    private ColorStateList textColorDefaultRb;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_quiz);

        tvquestion=findViewById (R.id.tvquestion);
        tvscore=findViewById (R.id.tvscore);
        tvquestioncount=findViewById (R.id.tvquestioncount);
        tvcountdown=findViewById (R.id.tvcountdown);
        rg=findViewById (R.id.rg);
        rb1=findViewById (R.id.rb1);
        rb2=findViewById (R.id.rb2);
        rb3=findViewById (R.id.rb3);
        btnconfirm=findViewById (R.id.btnconfirm);


        textColorDefaultRb=rb1.getTextColors ();


        QuizDbHelper dbHelper = new QuizDbHelper (this);
        questionList =dbHelper.getAllQuestions ();
        questionCountTotal= questionList.size ();
        Collections.shuffle (questionList);

        showNextQuestion();
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }


    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rg.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            tvquestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            tvquestioncount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            btnconfirm.setText("Confirm");
        } else {
            finishQuiz();
        }
    }
    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(rg.getCheckedRadioButtonId());
        int answerNr = rg.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswernr ()) {
            score++;
            tvscore.setText("Score: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswernr ()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                tvquestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                tvquestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                tvquestion.setText("Answer 3 is correct");
                break;
        }

        if (questionCounter < questionCountTotal) {
            btnconfirm.setText("Next");
        } else {
            btnconfirm.setText("Finish");
        }
    }

    private void finishQuiz()
    {
        Intent resultIntent = new Intent ();
        resultIntent.putExtra (EXTRA_SCORE,score);
        setResult (RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis ()){
            finishQuiz ();
        }else{
            Toast.makeText (this,"Press back again to finish",Toast.LENGTH_SHORT).show ();
        }
    backPressedTime = System.currentTimeMillis ();
    }
}
