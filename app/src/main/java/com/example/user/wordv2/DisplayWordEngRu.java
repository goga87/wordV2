package com.example.user.wordv2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class DisplayWordEngRu extends AppCompatActivity {
    String trans;
    long id_sp;
    int count;
    int curentPos;
    ArrayList<String> wordEng;
    ArrayList<String> wordRu;

    TextView tv_eng;
    TextView tv_ru;
    TextView tv_wordEngRu_status;
    TextView tv_other;
    ListView lv_showSp;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Cursor otherCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_word_eng_ru);
        Bundle extras = getIntent().getExtras();
        tv_wordEngRu_status = (TextView)findViewById(R.id.tv_wordEngRu_status);
        tv_wordEngRu_status.setText("id_sp: "+id_sp+", trans: "+trans);
        tv_eng = (TextView)  findViewById(R.id.tV_eng);
        tv_ru = (TextView)  findViewById(R.id.tV_ru);
        tv_other = (TextView) findViewById(R.id.tv_other);

        if (extras != null) {
            trans = extras.getString("trans");
            id_sp = extras.getLong("id_sp");
        }

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        userCursor =  db.rawQuery("select * from word JOIN word_in_sp ON word._id = word_in_sp.id_word WHERE id_sp = "+id_sp, null);
        count = userCursor.getCount();
        wordEng = new ArrayList<String>(count);
        wordRu = new ArrayList<String>(count);
        ArrayList<String> t_wordEng = new ArrayList<>(count);
        ArrayList<String> t_wordRu = new ArrayList<>(count);
        if (userCursor.moveToFirst()) {
            for (int i = 0; i < count; i++) {
                t_wordEng.add(userCursor.getString(1));
                t_wordRu.add(userCursor.getString(2));
                userCursor.moveToNext();
            }
            Random rnd  = new Random(System.currentTimeMillis());
            for (int i = 0; i < count; i++) {
                int r = rnd.nextInt(t_wordEng.size());
                wordEng.add(t_wordEng.remove(r));
                wordRu.add(t_wordRu.remove(r));
            }
            curentPos=-1;
            tv_wordEngRu_status.append(", count: "+count);
        }

        tv_other.setText("");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    public void onClick_b_OkNext(View view){
        if (curentPos<count-1){
            curentPos++;

            String w_eng = wordEng.get(curentPos);
            String w_ru = wordRu.get(curentPos);
            switch(trans) {
                case "eng":{
                    tv_eng.setText(w_eng);
                    tv_ru.setText("");
                    break;
                }
                case "ru":{
                    tv_eng.setText("");
                    tv_ru.setText(w_ru);
                    break;
                }
            }
        }
        else{
            Toast.makeText(this, "Конец списка", Toast.LENGTH_SHORT).show();
        }
        tv_wordEngRu_status.setText("count:"+(curentPos+1)+" / "+count);
        tv_other.setText("");
    }
    public void onClick_b_transfer(View view){
        if ((curentPos<count)&&(curentPos>-1)) {
            String w_eng = wordEng.get(curentPos);
            String w_ru = wordRu.get(curentPos);
            String ot_st = "";
            String temp = "";
            switch(trans) {
                case "eng": {
                    tv_ru.setText(w_ru);
                    break;
                }
                case "ru": {
                    tv_eng.setText(w_eng);
                    otherCursor =  db.rawQuery("select w_ru from word  WHERE w_eng = '"+w_eng+"'", null);
                    if (otherCursor.moveToFirst()){
                        do {
                            temp = otherCursor.getString(0);
                            if (temp.compareToIgnoreCase(w_ru)!=0){
                            ot_st = ot_st + temp;
                            }
                        }
                        while (otherCursor.moveToNext());

                    }
                    tv_other.setText(ot_st);
                    break;
                }
            }

        }
    }
}
