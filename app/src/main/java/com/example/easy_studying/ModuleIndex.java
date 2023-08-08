package com.example.easy_studying;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_studying.recycler_view.SwipeToDelete;
import com.example.easy_studying.recycler_view.WordAdapter;

import java.util.ArrayList;

public class ModuleIndex extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView moduleNameTitle;
    ModuleListSQL myDB;
    String module_id, module_name, module_creation, module_count_words;
    ArrayList<String> word_id, word, translation;
    WordAdapter wordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_index);

        recyclerView = findViewById(R.id.my_words_view);

        myDB = new ModuleListSQL(ModuleIndex.this);
        word_id = new ArrayList<String>();
        word = new ArrayList<String>();
        translation = new ArrayList<String>();

        Intent prevIntent = getIntent();
        module_id = prevIntent.getStringExtra("module_id");
        moduleData();

        wordAdapter = new WordAdapter(ModuleIndex.this, word_id, word, translation, module_name, module_creation);
        recyclerView.setAdapter(wordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModuleIndex.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDelete(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                if (position == 0 ) {
                    return;   // the first line is module info
                }
                final String word = wordAdapter.getData().get(position);
                wordAdapter.removeWord(position - 1);
                myDB.deleteOneWord(word);
//                TODO:: Snack to restore
//                Snackbar snackbar = Snackbar.make();
//                snackbar.show();
            }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    void moduleData() {
        Cursor moduleCursor = myDB.getModuleInfo(module_id);
        if (moduleCursor.getCount() != 1) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show();
            return;
        }

        moduleCursor.moveToFirst();
        module_name = moduleCursor.getString(1);
        module_creation = moduleCursor.getString(2);
        module_count_words = moduleCursor.getString(3);

        Cursor wordsCursor = myDB.getModuleWords(module_id);
        if (moduleCursor.getCount() == 0) {
            Toast.makeText(this, "Module words not found", Toast.LENGTH_SHORT).show();
            return;
        }
        while (wordsCursor.moveToNext()) {
            word_id.add(wordsCursor.getString(0));
            word.add(wordsCursor.getString(1));
            translation.add(wordsCursor.getString(2));
        }
    }
}