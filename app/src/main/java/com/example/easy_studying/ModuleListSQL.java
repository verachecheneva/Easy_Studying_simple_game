package com.example.easy_studying;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

class ModuleListSQL extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "EasyStudying.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Modules
     */

    private static final String TABLE_NAME_MODULES = "Modules";
    private static final String COLUMN_MODULE_ID = "module_id";
    private static final String COLUMN_MODULE_NAME = "module_name";
    private static final String COLUMN_DATE_CREATION = "module_creation";
    private static final String COLUMN_COUNT_WORDS = "module_count_words";

    /**
     * Table Words
     */
    private static final String TABLE_NAME_WORDS = "ModuleWords";
    private static final String COLUMN_WORD_ID = "word_id";
    private static final String WORD = "module_word";
    private static final String TRANSLATION = "module_translation";
    private static final String LEARNED_TEST = "module_test";
    private static final String LEARNED_WRITING = "module_writing";


    public ModuleListSQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME_MODULES +
                        " (" + COLUMN_MODULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MODULE_NAME + " TEXT NOT NULL, " +
                        COLUMN_DATE_CREATION + " DATETIME NOT NULL, " +
                        COLUMN_COUNT_WORDS + " INTEGER DEFAULT 0 );";

        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_NAME_WORDS +
                " (" + COLUMN_WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WORD + " TEXT NOT NULL, " +
                TRANSLATION + " TEXT NOT NULL, " +
                LEARNED_TEST + " BIT DEFAULT 0, " +
                LEARNED_WRITING + " BIT DEFAULT 0, " +
                COLUMN_MODULE_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY ( " + COLUMN_MODULE_ID + " ) REFERENCES Modules( module_id ) ON DELETE CASCADE);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MODULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WORDS);
        onCreate(db);
    }

    boolean addModule(String module_name, ArrayList<String[]> wordsPair) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MODULE_NAME, module_name);
            contentValues.put(COLUMN_DATE_CREATION, System.currentTimeMillis());
            contentValues.put(COLUMN_COUNT_WORDS, wordsPair.size());
            long module_id = db.insert(TABLE_NAME_MODULES, null, contentValues);

            for (String[] pair : wordsPair) {
                ContentValues cv = new ContentValues();
                cv.put(WORD, pair[0]);
                cv.put(TRANSLATION, pair[1]);
                cv.put(COLUMN_MODULE_ID, module_id);
                db.insert(TABLE_NAME_WORDS, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            db.endTransaction();
            System.out.println(e.getMessage());
            return false;
        }
        db.endTransaction();
        return true;
    }

    Cursor readAllModules() {
        String query = "SELECT * FROM " + TABLE_NAME_MODULES + " ORDER BY " + COLUMN_DATE_CREATION
                + " DESC;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteOneModule(String module_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_MODULES, COLUMN_MODULE_ID + "=?", new String[]{module_id});
        if (result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneWord(String word_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_WORDS, COLUMN_WORD_ID + "=?", new String[]{word_id});
        if (result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor getModuleInfo(String module_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_MODULES,
                null,
                COLUMN_MODULE_ID + "=?",
                new String[]{module_id},
                null,
                null,
                null);
        return cursor;
    }

    Cursor getModuleWords(String module_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_WORDS,
                new String[] {COLUMN_WORD_ID, WORD, TRANSLATION},
                COLUMN_MODULE_ID + "=?",
                new String[]{module_id},
                null,
                null,
                null);
        return cursor;
    }
}
