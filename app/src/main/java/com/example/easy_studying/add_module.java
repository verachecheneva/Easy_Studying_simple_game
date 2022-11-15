package com.example.easy_studying;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class add_module extends AppCompatActivity {

    LinearLayout formsLayout;
    EditText moduleName;
    AppCompatButton saveButton;
    FloatingActionButton addFormButton;
    private ArrayList<int[]> formIds;
    private ArrayList<String[]> wordsPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        formsLayout = findViewById(R.id.formsLayout);
        formIds = new ArrayList<int[]>();

        addFormButton = findViewById(R.id.floatingActionButton);
        addFormButton.setOnClickListener(onClick());

        moduleName = findViewById(R.id.moduleNameText);

        wordsPair = new ArrayList<String[ ] >();

        formsLayout.addView(addNewForm());

        saveButton = findViewById(R.id.saveModuleButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModuleListSQL moduleDB = new ModuleListSQL(add_module.this);
                if (!correct_data()) {
                    return;
                }
                boolean result = moduleDB.addModule(moduleName.getText().toString().trim(),
                        wordsPair);

                if (!result) {
                    Toast.makeText(add_module.this, "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(add_module.this, "Success", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(add_module.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsLayout.addView(addNewForm());
            }
        };
    }

    private LinearLayout addNewForm() {
        LinearLayout newFormLayout = new LinearLayout(this);
        newFormLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        newFormLayout.setOrientation(LinearLayout.HORIZONTAL);
        int first_word_id = View.generateViewId();
        int second_word_id = View.generateViewId();
        formIds.add(new int[]{first_word_id, second_word_id});

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lparams.weight = 1.0f;

        EditText first_word = new EditText(this);
        first_word.setLayoutParams(lparams);
        first_word.setHint("Word");
        first_word.setId(first_word_id);

        EditText second_word = new EditText(this);
        second_word.setLayoutParams(lparams);
        second_word.setHint("Meaning");
        second_word.setId(second_word_id);

        newFormLayout.addView(first_word);
        newFormLayout.addView(second_word);
        return newFormLayout;
    }

    /**
     * If data is correct function return 1, else 0
     * 1) Group should should have two or no one words
     */
    public boolean correct_data() {
        for (int i = 0; i < formIds.size(); i++) {
            EditText first_word = (EditText) findViewById(formIds.get(i)[0]);
            EditText second_word = (EditText) findViewById(formIds.get(i)[1]);

            String first_word_text = first_word.getText().toString().trim()
                    .replaceAll(" +", " ");
            String second_word_text = second_word.getText().toString().trim()
                    .replaceAll(" +", " ");

            if (!TextUtils.isEmpty(first_word_text) && !TextUtils.isEmpty(second_word_text)){
                wordsPair.add(new String[] { first_word_text, second_word_text });
            } else if (!TextUtils.isEmpty(first_word_text) || !TextUtils.isEmpty(second_word_text)) {
                if (TextUtils.isEmpty(first_word_text)) {
                    first_word.setError("Field cannot be empty");
                } else {
                    second_word.setError("Field cannot be empty");
                }
                return false;
            }
        }
        if (wordsPair.size() == 0) {
            Toast.makeText(this, "Module cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
