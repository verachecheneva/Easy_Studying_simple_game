package com.example.easy_studying;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.easy_studying.recycler_view.CustomAdapter;
import com.example.easy_studying.recycler_view.SwipeToDelete;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class my_modules extends AppCompatActivity {

    RecyclerView recyclerView;

    ModuleListSQL myDB;
    ArrayList<String> module_id, module_name, module_creation, module_count_words;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_modules);

        recyclerView = findViewById(R.id.my_modules_view);

        myDB = new ModuleListSQL(my_modules.this);
        module_id = new ArrayList<>();
        module_name = new ArrayList<>();
        module_creation = new ArrayList<>();
        module_count_words = new ArrayList<>();

        dataToArray();

        customAdapter = new CustomAdapter(my_modules.this, module_id, module_name, module_count_words);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(my_modules.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDelete(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final String module = customAdapter.getData().get(position);
                customAdapter.removeModule(position);
                myDB.deleteOneRow(module);
//                TODO:: Snack to restore
//                Snackbar snackbar = Snackbar.make();
//                snackbar.show();
            }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    void dataToArray() {
        Cursor cursor = myDB.readAllModules();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                module_id.add(cursor.getString(0));
                module_name.add(cursor.getString(1));
                module_creation.add(cursor.getString(2));
                module_count_words.add(cursor.getString(3));
            }
        }
    }
}