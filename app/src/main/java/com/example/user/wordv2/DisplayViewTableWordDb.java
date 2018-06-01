package com.example.user.wordv2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DisplayViewTableWordDb extends AppCompatActivity {

    ListView lv_ViewTableWordDb;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db_word;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_view_table_word_db);
        lv_ViewTableWordDb = (ListView)findViewById(R.id.lv_tableWordDb);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        lv_ViewTableWordDb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DisplayEditTableWord.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db_word = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db_word.rawQuery("select * from "+ DatabaseHelper.TABLE_WORD, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DatabaseHelper.COLUMN_WORD_ENG, DatabaseHelper.COLUMN_WORD_RU};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        //header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        lv_ViewTableWordDb.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db_word.close();
        userCursor.close();
    }

    public void b_add (View view){
        Intent intent = new Intent(getApplicationContext(), DisplayEditTableWord.class);
        startActivity(intent);
    }
}
