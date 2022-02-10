package fr.devsphinx.topquiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.regex.Pattern;

import fr.devsphinx.topquiz.DatabaseManager;
import fr.devsphinx.topquiz.R;
import fr.devsphinx.topquiz.model.Kit;
import fr.devsphinx.topquiz.model.Question;

public class MakeKit_NumberOfQuestion_Activity extends AppCompatActivity {

    private TextView mTest;
    private TextView mNumberQuestion;
    private Kit mKit;
    private EditText mDisplayedQuestions;
    private Button mFinsih;

    private DatabaseManager db;

    public static final String KEY_CUSTOMS_KITS = "KEY_CUSTOMS_KEYS";
    public static final String KEY_CUSTOMS_QUESTION = "KEY_CUSTOMS_QUESTION";
    public static final String PREFERENCE_NAME_CUSTOMS_KITS = "PREFERENCE_NAME_CUSTOMS_KITS";

    private Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_kit_number_of_question);

        Intent intent = getIntent();
        mKit = intent.getParcelableExtra("kit");
        mNumberQuestion = findViewById(R.id.make_kit_number_question2);
        mDisplayedQuestions = findViewById(R.id.make_kit_number_question_edittext);
        mFinsih = findViewById(R.id.make_kit_finish_it);

        UpdateNumberQuestion();

        mFinsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isNumeric.matcher(mDisplayedQuestions.getText().toString()).matches()){
                    mDisplayedQuestions.setError("Must be a number");
                }else if(Integer.parseInt(mDisplayedQuestions.getText().toString()) <= 0){
                    mDisplayedQuestions.setError("Must be greater than 0");
                }else if(Integer.parseInt(mDisplayedQuestions.getText().toString()) > mKit.count()){
                    mDisplayedQuestions.setError("Cannot be than the number of question in the kit");
                }else{
                    mKit.setDisplayedQuestions(Integer.parseInt(mDisplayedQuestions.getText().toString()));

                    db = new DatabaseManager(MakeKit_NumberOfQuestion_Activity.this);
                    saveKitInDB(db, mKit);
                    db.close();

                    Intent MakeKitActivityIntent = new Intent(MakeKit_NumberOfQuestion_Activity.this, MainActivity.class);
                    startActivity(MakeKitActivityIntent);
                    finish();
                }
            }
        });
    }
    @SuppressLint("SetTextI18n")
    public void UpdateNumberQuestion(){
        mNumberQuestion.setText("Number of question : " + mKit.count());
    }

    // Nous sommes à la fin du processus de création d'un Kit par l'utilisateur
    // Nous voulons donc le sauvegarder dans la base de donnée
    public void saveKitInDB(DatabaseManager db, Kit kit){

        db.insertKit(kit.getTitle(), kit. getDisplayedQuestions()); // On insere le Kit sans les questions
        Kit newKit = db.getKitbyName(kit.getTitle()); // Vu que l'id est incrémenté automatiquement par la bd, il faut le recupérer
        for (Question q: kit.getAllQuestions()) { // On parcourt les questions de Kit à sauvegarder
            List<String> reponses = q.getChoiceList();
            db.insertQuestion(newKit.getId(), q.getQuestion(), reponses.get(0), reponses.get(1), reponses.get(2),
                    reponses.get(3), q.getAnswerIndex()); // On insere la question dans le bon Kit avec à l'id récupéré précedement
        }

    }

}