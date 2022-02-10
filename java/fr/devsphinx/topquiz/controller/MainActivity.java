package fr.devsphinx.topquiz.controller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.devsphinx.topquiz.DatabaseManager;
import fr.devsphinx.topquiz.R;
import fr.devsphinx.topquiz.model.*;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private boolean mPlay = false;
    private Button mOwnKitButton;
    private Button mDeleteButton;
    private User mUser;
    private KitBank mKitBank;
    private List<Button> listofdeletebutton;
    private List<Kit> mKitList;

    private DatabaseManager db;

    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private static final int MAKE_KIT_ACTIVITY_REQUEST_CODE = 69;

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";
    private static final String SHARED_PREF_USER_INFO_LASTSCORE = "SHARED_PREF_USER_INFO_LASTSCORE";

    public static final String KEY_CUSTOMS_KITS = "KEY_CUSTOMS_KEYS";
    public static final String KEY_CUSTOMS_QUESTION = "KEY_CUSTOMS_QUESTION";
    public static final String PREFERENCE_NAME_CUSTOMS_KITS = "PREFERENCE_NAME_CUSTOMS_KITS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText = findViewById(R.id.main_edittext_name);
        mOwnKitButton = findViewById(R.id.main_button_make_own_kit);
        mDeleteButton = findViewById(R.id.main_button_delete_kit);
        mUser = new User();
        LinearLayout kitlist = findViewById(R.id.main_list_kit);


        db = new DatabaseManager(this);
        mKitList = db.getAllKit();
        db.close();

        listofdeletebutton = new ArrayList<Button>();

        for (Kit item : mKitList) {
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(32,16,32,16);
            l.setLayoutParams(params);


            TextView tv = new TextView(this);
            tv.setText(item.getTitle());
            tv.setTextSize(18);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.rgb(211, 211, 211));
            tv.setPadding(16, 16, 16, 28);
            l.addView(tv);
            ViewGroup.LayoutParams lp = tv.getLayoutParams();
            lp.width = 600;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tv.setLayoutParams(lp);

            Button button = new Button(this);
            button.setId(item.getId());
            button.setText("Play");
            button.setBackgroundColor(Color.rgb(62, 79, 92));
            button.setTextColor(Color.WHITE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mPlay){
                        UpdateFirstname(mNameEditText.getText().toString());

                        Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("idkit", button.getId());
                        gameActivityIntent.putExtra("idkit", b);
                        startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);

                        mUser.setFirstName(mNameEditText.getText().toString());
                    }else{
                        mNameEditText.setError("Required");
                    }
                }
            });
            l.addView(button);

            Button delete_but = new Button(this);
            delete_but.setBackgroundResource(R.drawable.ic_action_delete);
            delete_but.setVisibility(View.INVISIBLE);
            delete_but.setId(item.getId());
            listofdeletebutton.add(delete_but);
            delete_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    db.deletekitbyID(delete_but.getId());
                                    Intent MakeKitActivityIntent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(MakeKitActivityIntent);
                                    finish();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    // no ...
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Are you sure to delete '"+ item.getTitle()+"' ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
            l.addView(delete_but);

            kitlist.addView(l);
        }


        UpdateView();

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    mPlay = true;
                }
            }
        });

        mOwnKitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MakeKitActivityIntent = new Intent(MainActivity.this, MakeKit_title_Activity.class);
                startActivity(MakeKitActivityIntent);
                finish();
            }
        });
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Button but : listofdeletebutton) {
                    if(but.getVisibility() == View.INVISIBLE)
                        but.setVisibility(View.VISIBLE);
                    else
                        but.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            UpdateLastscore(score);
            UpdateView();
        }
    }

    public void UpdateFirstname(String pfirstname){
        getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .edit()
                .putString(SHARED_PREF_USER_INFO_NAME, pfirstname)
                .apply();
    }
    public void UpdateLastscore(int plastscore){
        getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .edit()
                .putString(SHARED_PREF_USER_INFO_LASTSCORE, Integer.toString(plastscore))
                .apply();
    }
    public void UpdateView(){
        String lastscore = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_LASTSCORE, null);
        String firstname = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);

        if(firstname != null && lastscore != null){
            mGreetingTextView.setText("Welcome back " + firstname + " !\nYour last score was " + lastscore + ", will you do better this time ?");
            mNameEditText.setText(firstname);
            if(!firstname.isEmpty())
                mPlay = true;
        }else{
            mGreetingTextView.setText("Welcome ! What's your name ?");
        }

    }
}