package com.example.easy_studying;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class add_module extends AppCompatActivity {

    LinearLayout formsLayout;
    EditText moduleName;
    AppCompatButton saveButton;
    FloatingActionButton addFormButton;
    private ArrayList<String> formIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        formsLayout = findViewById(R.id.formsLayout);
        formIds = new ArrayList<>(Arrays.asList("wordsForm0"));

        addFormButton = findViewById(R.id.floatingActionButton);
        addFormButton.setOnClickListener(onClick());

        moduleName = findViewById(R.id.moduleNameText);

        saveButton = findViewById(R.id.saveModuleButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModuleListSQL moduleDB = new ModuleListSQL(add_module.this);
                moduleDB.addModule(moduleName.getText().toString().trim(), formIds.size());
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
        int layout_id = formIds.size();
        newFormLayout.setId(layout_id);
        formIds.add(Integer.toString(layout_id));

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lparams.weight = 1.0f;

        EditText eng_word = new EditText(this);
        eng_word.setLayoutParams(lparams);
        eng_word.setHint("Word in english");
        eng_word.setId(layout_id);

        EditText rus_word = new EditText(this);
        rus_word.setLayoutParams(lparams);
        rus_word.setHint("Word in russian");
        eng_word.setId(layout_id + 1);

        newFormLayout.addView(eng_word);
        newFormLayout.addView(rus_word);
        return newFormLayout;
    }
}
