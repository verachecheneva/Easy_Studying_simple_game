package com.example.easy_studying;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.easy_studying.recycler_view.ModuleAdapter;
import com.example.easy_studying.recycler_view.RecyclerItemClickListener;
import com.example.easy_studying.recycler_view.SwipeToDelete;

import java.util.ArrayList;

public class AllModules extends AppCompatActivity {

    RecyclerView recyclerView;

    ModuleListSQL myDB;
    ArrayList<String> module_id, module_name, module_creation, module_count_words;
    ModuleAdapter moduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_modules);

        recyclerView = findViewById(R.id.my_modules_view);

        myDB = new ModuleListSQL(AllModules.this);
        module_id = new ArrayList<>();
        module_name = new ArrayList<>();
        module_creation = new ArrayList<>();
        module_count_words = new ArrayList<>();

        dataToArray();

        moduleAdapter = new ModuleAdapter(AllModules.this, module_id, module_name, module_count_words);
        recyclerView.setAdapter(moduleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllModules.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDelete(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final String module = moduleAdapter.getData().get(position);
                moduleAdapter.removeModule(position);
                myDB.deleteOneModule(module);
//                TODO:: Snack to restore
//                Snackbar snackbar = Snackbar.make();
//                snackbar.show();
            }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(AllModules.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final String module = moduleAdapter.getData().get(position);
                        Intent intent = new Intent(AllModules.this, ModuleIndex.class);
                        intent.putExtra("module_id", module);
                        startActivity(intent);
                    }
                })
        );

    }

    void dataToArray() {
        Cursor cursor = myDB.readAllModules();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No modules found", Toast.LENGTH_SHORT).show();
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