package fr.devsphinx.topquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import fr.devsphinx.topquiz.DatabaseManager;
import fr.devsphinx.topquiz.R;
import fr.devsphinx.topquiz.model.KitBank;
import fr.devsphinx.topquiz.model.Question;
import fr.devsphinx.topquiz.model.Kit;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseManager db;

    private TextView mQuestion;
    private TextView mLiveScore;
    private Button mAnswer1;
    private Button mAnswer2;
    private Button mAnswer3;
    private Button mAnswer4;

    private Kit mQuestionBank;

    private int mRemainingQuestionCount;
    private int mCountQuestion;
    private int mScore = -1;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    private Boolean mEnableTouchEvents;

    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";

    private int kitid;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mRemainingQuestionCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("idkit");
        int id = b.getInt("idkit");
        db = new DatabaseManager(this);
        mQuestionBank = db.getKitbyID(id);
        db.close();

        mQuestion = findViewById(R.id.game_activity_textview_question);
        mLiveScore = findViewById(R.id.livescore);
        mAnswer1 = findViewById(R.id.game_activity_button_1);
        mAnswer2 = findViewById(R.id.game_activity_button_2);
        mAnswer3 = findViewById(R.id.game_activity_button_3);
        mAnswer4 = findViewById(R.id.game_activity_button_4);

        // Use the same listener for the four buttons.
        // The view id value will be used to distinguish the button triggered
        mAnswer1.setOnClickListener(this);
        mAnswer2.setOnClickListener(this);
        mAnswer3.setOnClickListener(this);
        mAnswer4.setOnClickListener(this);

        mCountQuestion = 0;

        mQuestionBank.shuffle();
        displayQuestion(mQuestionBank.getCurrentQuestion());

        mEnableTouchEvents = true;

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mRemainingQuestionCount = mQuestionBank.getDisplayedQuestions();
        }
    }

   public Kit generateQuestionBank(){
       KitBank kb = new KitBank(KitBank.generateKits());
       return kb.getSpecificKit(kitid);
    }

    private void displayQuestion(final Question question) {
        // Set the text for the question text view and the four buttons
        mQuestion.setText(question.getQuestion());
        mAnswer1.setText(question.getChoiceList().get(0));
        mAnswer2.setText(question.getChoiceList().get(1));
        mAnswer3.setText(question.getChoiceList().get(2));
        mAnswer4.setText(question.getChoiceList().get(3));
        UpdateLiveScore();

    }
    public void UpdateLiveScore(){
        mCountQuestion++;
        mLiveScore.setText(mCountQuestion + " / " + mQuestionBank.getDisplayedQuestions());
    }
    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .create()
                .show();
    }
    @Override
    public void onClick(View v) {
        int index;

        if (v == mAnswer1) {
            index = 0;
        } else if (v == mAnswer2) {
            index = 1;
        } else if (v == mAnswer3) {
            index = 2;
        } else if (v == mAnswer4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }

       Question q = this.mQuestionBank.getCurrentQuestion();
        if(q.getAnswerIndex() == index){
            mScore++;
            v.setBackgroundColor(Color.GREEN);
        }else {
            v.setBackgroundColor(Color.RED);
            switch (q.getAnswerIndex()){
                case 0:
                    mAnswer1.setBackgroundColor(Color.GREEN);
                    break;
                case 1:
                    mAnswer2.setBackgroundColor(Color.GREEN);
                    break;
                case 2:
                    mAnswer3.setBackgroundColor(Color.GREEN);
                    break;
                case 3:
                    mAnswer4.setBackgroundColor(Color.GREEN);
                    break;
                default:
                    throw new IllegalStateException("Unknown clicked view : " + v);
            }
        }

        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mAnswer1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                mAnswer2.setBackgroundColor(getResources().getColor(R.color.purple_500));
                mAnswer3.setBackgroundColor(getResources().getColor(R.color.purple_500));
                mAnswer4.setBackgroundColor(getResources().getColor(R.color.purple_500));
                mRemainingQuestionCount--;
                if (mRemainingQuestionCount > 0) {
                    Question mCurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mCurrentQuestion);
                } else {
                    endGame();
                }
                mEnableTouchEvents = true;

            }

        }, 2_000); // LENGTH_SHORT is usually 2 second long
    }
}