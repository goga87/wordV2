package com.example.user.wordv2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayDb extends AppCompatActivity {
    TextView tv_viewdb;
    ListView lv_ViewDb;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db_word;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_db);
        lv_ViewDb = (ListView)findViewById(R.id.lv_ViewDb);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // открываем подключение
        db_word = databaseHelper.getReadableDatabase();
        //получаем данные из бд в виде курсора
        userCursor =  db_word.rawQuery("select name from sqlite_master where type = 'table'", null);
        ArrayList<String> table = new ArrayList();
        tv_viewdb = findViewById(R.id.tv_viewdb);
        if(userCursor.moveToFirst()){
            do{
                if(userCursor.getString(0).compareToIgnoreCase("android_metadata")==0) {continue;}
                if(userCursor.getString(0).compareToIgnoreCase("sqlite_sequence")==0) {continue;}
                table.add(userCursor.getString(0));
                tv_viewdb.append(userCursor.getString(0)+"\n");
            }
            while (userCursor.moveToNext());
        }
        ArrayAdapter<String > adapterTable = new ArrayAdapter(this,android.R.layout.simple_list_item_1,table);
        lv_ViewDb.setAdapter(adapterTable);

        lv_ViewDb.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                String tablename = (String) lv_ViewDb.getItemAtPosition(position);
                switch(tablename){
                    case "word":{
                        Intent intent = new Intent (getApplicationContext(), DisplayViewTableWordDb.class);
                        intent.putExtra("name", "value");
                        startActivity(intent);
                        break;
                    }
                    case "sp_word":{
                        Intent intent = new Intent (getApplicationContext(), DisplayViewTablesp_word.class);
                        intent.putExtra("name", "value");
                        startActivity(intent);
                        break;

                    }
                    default:{
                        Toast.makeText(getApplicationContext(), "Мы кликнули на пункт " +
                                tablename, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db_word.close();
        userCursor.close();
    }

    public void onClick_b_CreateDb (View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("word.db",MODE_PRIVATE,null);
        db.execSQL("DROP TABLE IF EXISTS word");
        db.execSQL("DROP TABLE IF EXISTS sp_word");
        db.execSQL("CREATE TABLE IF NOT EXISTS word (_id INTEGER  PRIMARY KEY AUTOINCREMENT ,w_eng TEXT UNIQUE, w_ru TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS sp_word (_id INTEGER  PRIMARY KEY AUTOINCREMENT ,sp_name TEXT UNIQUE)");
        db.execSQL("INSERT INTO word VALUES (null, 'word' , 'слово');");
        db.execSQL("INSERT INTO word VALUES (null, 'low' , 'низкий');");
        db.execSQL("INSERT INTO word VALUES (null, 'narrow' , 'узкий');");
        db.execSQL("INSERT INTO sp_word VALUES (null, 'список 1');");
        db.close();
    }

    public void onClick_b_DeleteDb (View view){
        getApplicationContext().deleteDatabase("word.db");
    }

    public void onClick_b_ViewDb (View view){
        /*
        Intent intent = new Intent(this,DisplayViewDb.class );
        intent.putExtra("name", "value");
        startActivity(intent);
        */
        tv_viewdb = findViewById(R.id.tv_viewdb);
        tv_viewdb.setText("");
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("word.db",MODE_PRIVATE,null);
        Cursor query = db.rawQuery("SELECT * FROM word;", null);
        tv_viewdb.append("Table word, count: "+query.getCount()+"\n");
        if (query.moveToFirst()){
            do{
                int i_ind = query.getInt(0);
                String w_eng = query.getString(1);
                String w_ru = query.getString(2);
                tv_viewdb.append("id: "+i_ind+ " eng: "+ w_eng + " ru: "+w_ru+" \n");
            }
            while (query.moveToNext());
        }
        query.close();
        query = db.rawQuery("SELECT * FROM sp_word;", null);

        tv_viewdb.append("Table sp_word, count: "+query.getCount()+"\n");

        if (query.moveToFirst()){
            do{
                int i_ind = query.getInt(0);
                String sp_word = query.getString(1);

                tv_viewdb.append("id: "+i_ind+ " sp_word: "+ sp_word + " \n");
            }
            while (query.moveToNext());
        }
        query.close();
        db.close();
    }
}
