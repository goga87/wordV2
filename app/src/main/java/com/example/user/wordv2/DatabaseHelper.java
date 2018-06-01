package com.example.user.wordv2;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "word.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE_WORD = "word"; // название таблицы в бд
    static final String TABLE_SPWORD = "sp_word"; // название таблицы в бд
    static final String TABLE_WORDINSP = "word_in_sp";
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WORD_ENG = "w_eng";
    public static final String COLUMN_WORD_RU = "w_ru";

    public static final String COLUMN_SPWORD_NAME = "sp_name";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE word (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_WORD_ENG
                + " TEXT, " + COLUMN_WORD_RU + " TEXT);");
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE_WORD +" (" + COLUMN_WORD_ENG
                + ", " + COLUMN_WORD_RU  + ") VALUES ('word', 'слово');");
        db.execSQL("INSERT INTO "+ TABLE_WORD +" (" + COLUMN_WORD_ENG
                + ", " + COLUMN_WORD_RU  + ") VALUES ('low', 'низкий');");
        db.execSQL("INSERT INTO "+ TABLE_WORD +" (" + COLUMN_WORD_ENG
                + ", " + COLUMN_WORD_RU  + ") VALUES ('narrow', 'узкий');");


        db.execSQL("CREATE TABLE sp_word (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SPWORD_NAME
                + " TEXT UNIQUE);");

        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE_SPWORD +" (" + COLUMN_SPWORD_NAME
                + ") VALUES ('список 1');");

        //слова в списке
        db.execSQL("CREATE TABLE word_in_sp ( id_sp INTEGER , id_word INTEGER ,PRIMARY KEY (id_sp, id_word) )" );

        db.execSQL("INSERT INTO "+ TABLE_WORDINSP +" ( id_sp , id_word ) VALUES (1, 1);");

        //db.execSQL("CREATE TABLE IF NOT EXISTS word (w_id INTEGER  PRIMARY KEY AUTOINCREMENT ,w_eng TEXT UNIQUE, w_ru TEXT)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS sp_word (sp_id INTEGER  PRIMARY KEY AUTOINCREMENT ,sp_name TEXT UNIQUE)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WORD);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SPWORD);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WORDINSP);
        onCreate(db);
    }
}
