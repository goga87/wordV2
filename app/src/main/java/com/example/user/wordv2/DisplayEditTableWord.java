package com.example.user.wordv2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DisplayEditTableWord extends AppCompatActivity {
    EditText word_eng;
    EditText word_ru;
    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long itemId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_edit_table_word);

        word_eng = (EditText) findViewById(R.id.ed_eng);
        word_ru = (EditText) findViewById(R.id.ed_ru);
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getLong("id");
        }
        // если 0, то добавление
        if (itemId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_WORD + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(itemId)});
            userCursor.moveToFirst();
            word_eng.setText(userCursor.getString(1));
            word_ru.setText(String.valueOf(userCursor.getString(2)));
            userCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_WORD_ENG, word_eng.getText().toString());
        cv.put(DatabaseHelper.COLUMN_WORD_RU, word_ru.getText().toString());

        if (itemId > 0) {
            db.update(DatabaseHelper.TABLE_WORD, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(itemId), null);
        } else {
            db.insert(DatabaseHelper.TABLE_WORD, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE_WORD, "_id = ?", new String[]{String.valueOf(itemId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, DisplayViewTableWordDb.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
