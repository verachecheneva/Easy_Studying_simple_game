package com.example.easy_studying;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Date;

class ModuleListSQL extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "EasyStudying.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Modules";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MODULE_NAME = "module_name";
    private static final String COLUMN_DATE_CREATION = "module_creation";
    private static final String COLUMN_COUNT_WORDS = "module_count_words";


    public ModuleListSQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MODULE_NAME + " TEXT, " +
                        COLUMN_DATE_CREATION + " DATE, " +
                        COLUMN_COUNT_WORDS + " INTEGER DEFAULT 0);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addModule(String module_name, int count_words) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        dateFormat.format(date);  // в этом я совссем не уверенна
        cv.put(COLUMN_MODULE_NAME, module_name);
        cv.put(COLUMN_DATE_CREATION, dateFormat.format(date));
        cv.put(COLUMN_COUNT_WORDS, count_words);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllModules() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
