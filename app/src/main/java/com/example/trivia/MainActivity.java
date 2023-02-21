package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferencesFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView questionTextView;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int currentQuestionsIndex;
    List<Question>questionList;
    private TextView score_textview;
    private TextView highestScore_textview;
    private int scoreCoenter=0;
    private Score score;
    private Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score=new Score();
        prefs=new Prefs(MainActivity.this);

        //instantiate view
        nextButton=findViewById(R.id.next_btn);
        prevButton=findViewById(R.id.prev_btn);
        trueButton=findViewById(R.id.true_btn);
        falseButton=findViewById(R.id.false_btn);
        questionCounterTextView=findViewById(R.id.counter_textview);
        questionTextView=findViewById(R.id.questions_textview);
        score_textview=findViewById(R.id.score_textview);
        highestScore_textview=findViewById(R.id.highestScore_textview);

      
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);


        questionList=new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionArrayList.get(currentQuestionsIndex).getAnswer());
                questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionsIndex, questionList.size()));
            }
        });

        highestScore_textview.setText(String.valueOf(prefs.getHighScore()));
        currentQuestionsIndex=prefs.getState();

    }






    //METHODS USED IN THE APP

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev_btn:
                if (currentQuestionsIndex>0)
                {
                currentQuestionsIndex=(currentQuestionsIndex-1)%questionList.size();
                updateQuestion();
                }
                break;
            case R.id.next_btn:
                currentQuestionsIndex=(currentQuestionsIndex+1)%questionList.size();
                updateQuestion();
                break;
            case R.id.true_btn:
                checkAnswer(true);
                break;

            case R.id.false_btn:
                checkAnswer(false);
                break;
        }
    }

    private void addPoints()
    {
        scoreCoenter+=100;
        score.setScore(scoreCoenter);
        score_textview.setText(String.valueOf(score.getScore()));
    }

    private void deductPoints()
    {
        if (scoreCoenter>0)
        { scoreCoenter-=100;
        score.setScore(scoreCoenter);
        score_textview.setText(String.valueOf(score.getScore()));
        }


    }

    private void checkAnswer(boolean b) {
        if (questionList.get(currentQuestionsIndex).isAnswerTrue()==b)
        {
            addPoints();
            fadeView();
            Toast.makeText(MainActivity.this,"Correct!",Toast.LENGTH_SHORT).show();
        }
        else {
              deductPoints();
              shakeAnimation();
              Toast.makeText(MainActivity.this, "Wrong!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();

        }
    }

    private void updateQuestion() {
        String question=questionList.get(currentQuestionsIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(currentQuestionsIndex+" / "+questionList.size());

    }

    private void shakeAnimation()
    {
        Animation shake= AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation );
        CardView cardView=findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionsIndex=(currentQuestionsIndex+1)%questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void fadeView()
    {
        CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation =new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
               cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionsIndex=(currentQuestionsIndex+1)%questionList.size();
                updateQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionsIndex);
        super.onPause();
    }
}