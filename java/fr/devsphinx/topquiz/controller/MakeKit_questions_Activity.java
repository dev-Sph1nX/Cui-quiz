package fr.devsphinx.topquiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.devsphinx.topquiz.R;
import fr.devsphinx.topquiz.model.Kit;
import fr.devsphinx.topquiz.model.KitBank;
import fr.devsphinx.topquiz.model.Question;

public class MakeKit_questions_Activity extends AppCompatActivity {
    private TextView mTest;
    private TextView mNumberQuestion;
    private EditText mQuestion;
    private EditText mAnswer1;
    private EditText mAnswer2;
    private EditText mAnswer3;
    private EditText mAnswer4;
    private Spinner mSpinner;
    private Button mNextButton;
    private Button mFinishButton;
    private List<Question> mQuestionList;
    private Kit mKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_kit_questions);

        mQuestionList = new ArrayList<Question>();

        Bundle b = getIntent().getExtras();
        String name = b.getString("kit name");

        mTest = findViewById(R.id.test);
        mTest.setText("Kit's name : " + name);

        mQuestion = findViewById(R.id.make_kit_questions_question);
        mAnswer1 = findViewById(R.id.make_kit_questions_answer1);
        mAnswer2 = findViewById(R.id.make_kit_questions_answer2);
        mAnswer3 = findViewById(R.id.make_kit_questions_answer3);
        mAnswer4 = findViewById(R.id.make_kit_questions_answer4);
        mNextButton = findViewById(R.id.make_kit_questions_nextbutton);
        mFinishButton = findViewById(R.id.make_kit_questions_finishbutton);
        mSpinner = findViewById(R.id.make_kit_questions_spinner);
        mNumberQuestion = findViewById(R.id.make_kit_number_question);
        UpdateNumberQuestion();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.right_answer_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate()){
                    Question question = KitBank.createQuestion(mQuestion.getText().toString(), mAnswer1.getText().toString(),
                            mAnswer2.getText().toString(), mAnswer3.getText().toString(), mAnswer4.getText().toString(),
                            Integer.parseInt(mSpinner.getSelectedItem().toString())-1);
                    mQuestionList.add(question);
                    Toast.makeText(MakeKit_questions_Activity.this, "Question inserted !", Toast.LENGTH_SHORT).show();

                    mQuestion.getText().clear();
                    mAnswer1.getText().clear();
                    mAnswer2.getText().clear();
                    mAnswer3.getText().clear();
                    mAnswer4.getText().clear();
                    mSpinner.setSelection(0);

                    UpdateNumberQuestion();
                }else{
                    Toast.makeText(MakeKit_questions_Activity.this, "Some empty fields has to be filled!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate()){
                   Question question = KitBank.createQuestion(mQuestion.getText().toString(), mAnswer1.getText().toString(),
                            mAnswer2.getText().toString(), mAnswer3.getText().toString(), mAnswer4.getText().toString(),
                            Integer.parseInt(mSpinner.getSelectedItem().toString())-1);
                    mQuestionList.add(question);
                    Toast.makeText(MakeKit_questions_Activity.this, "Question inserted !", Toast.LENGTH_SHORT).show();

                    Kit k = new Kit(name, mQuestionList);
                    Intent MakeKitActivityIntent = new Intent(MakeKit_questions_Activity.this, MakeKit_NumberOfQuestion_Activity.class);
                    MakeKitActivityIntent.putExtra("kit", k);
                    startActivity(MakeKitActivityIntent);
                    finish();
                }else{
                    Toast.makeText(MakeKit_questions_Activity.this, "Some empty fields has to be filled!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static boolean hasText(EditText editText) {

        return hasText(editText,"Required");
    }
    public static boolean hasText(EditText editText,String error_message) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0) {
            editText.setError(error_message);
            return false;
        }

        return true;
    }

    private boolean Validate() {
        boolean check = true;
        if (!hasText(mQuestion)) check = false;
        if (!hasText(mAnswer1)) check = false;
        if (!hasText(mAnswer2)) check = false;
        if (!hasText(mAnswer3)) check = false;
        if (!hasText(mAnswer4)) check = false;

        return check;
    }
    public void UpdateNumberQuestion(){
        mNumberQuestion.setText("Number of question : " + mQuestionList.size());
    }
}