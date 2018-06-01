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

public class DisplayViewTablesp_word extends AppCompatActivity {

    ListView lv_ViewTablesp_word;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db_word;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_view_tablesp_word);
        lv_ViewTablesp_word = (ListView)findViewById(R.id.lv_tablesp_word);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        lv_ViewTablesp_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DisplayEditTableSp_Word.class);
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
        userCursor =  db_word.rawQuery("select * from "+ DatabaseHelper.TABLE_SPWORD, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DatabaseHelper.COLUMN_SPWORD_NAME};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                userCursor, headers, new int[]{android.R.id.text1}, 0);
        //header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        lv_ViewTablesp_word.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db_word.close();
        userCursor.close();
    }

    public void b_add (View view){
        Intent intent = new Intent(getApplicationContext(), DisplayEditTableSp_Word.class);
        startActivity(intent);
    }
}
