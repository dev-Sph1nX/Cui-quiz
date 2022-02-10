package fr.devsphinx.topquiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fr.devsphinx.topquiz.R;
import fr.devsphinx.topquiz.model.Kit;

public class MakeKit_title_Activity extends AppCompatActivity {

    private EditText mKitTitleEditText;
    private Button mNextButton;
    public static final String EXTRA_KIT_NAME = "EXTRA_KIT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_kit_title);

        mKitTitleEditText = findViewById(R.id.make_kit_title_edittext);
        mNextButton = findViewById(R.id.make_kit_title_next_button);

        mNextButton.setEnabled(false);
        mKitTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mNextButton.setEnabled(!s.toString().isEmpty());
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent MakeKitActivityIntent = new Intent(MakeKit_title_Activity.this, MakeKit_questions_Activity.class);
                Bundle b = new Bundle();
                b.putString("kit name", mKitTitleEditText.getText().toString());
                MakeKitActivityIntent.putExtras(b);
                startActivity(MakeKitActivityIntent);
                finish();
            }
        });
    }
}