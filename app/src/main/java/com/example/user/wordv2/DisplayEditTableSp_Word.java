package com.example.user.wordv2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DisplayEditTableSp_Word extends AppCompatActivity {
    EditText sp_name;
    Button delButton;
    Button saveButton;
    ListView lv_WordinSp;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    long itemId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_edit_table_sp__word);
        sp_name = (EditText) findViewById(R.id.ed_sp_name);
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        lv_WordinSp = (ListView)findViewById(R.id.lv_WordinSp);

        sqlHelper = new DatabaseHelper(this);

        lv_WordinSp.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                String tablename = ((Cursor) lv_WordinSp.getItemAtPosition(position)).getString(1);

                Toast.makeText(getApplicationContext(), "Мы кликнули на пункт " +
                        tablename+", pos: "+ position+", id: "+ id, Toast.LENGTH_SHORT).show();

                }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getLong("id");
        }
        // если 0, то добавление
        if (itemId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_SPWORD + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(itemId)});
            userCursor.moveToFirst();
            sp_name.setText(userCursor.getString(1));
            userCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
        showWordInSp();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_SPWORD_NAME, sp_name.getText().toString());

        if (itemId > 0) {
            db.update(DatabaseHelper.TABLE_SPWORD, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(itemId), null);
        } else {
            db.insert(DatabaseHelper.TABLE_SPWORD, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE_SPWORD, "_id = ?", new String[]{String.valueOf(itemId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, DisplayViewTablesp_word.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private  void showWordInSp(){
        //получаем данные из бд в виде курсора
        userCursor = db.rawQuery("select _id, w_eng ||' - '|| w_ru as 'w_eng' from " + DatabaseHelper.TABLE_WORD, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{DatabaseHelper.COLUMN_WORD_ENG};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                userCursor, headers, new int[]{android.R.id.text1}, 0);
        //header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        lv_WordinSp.setAdapter(userAdapter);
        //lv_WordinSp.setItemChecked(1,true);
    }
}
