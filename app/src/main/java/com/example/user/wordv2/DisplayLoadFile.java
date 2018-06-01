package com.example.user.wordv2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DisplayLoadFile extends AppCompatActivity {
    String nameFile;
    String text;
    EditText ed_NameSp;
    TextView tv_File;
    TextView tv_status;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_load_file);
        ed_NameSp = (EditText) findViewById(R.id.ed_NameSp);
        tv_File = (TextView) findViewById(R.id.tv_file);
        tv_status = (TextView) findViewById(R.id.tv_status);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameFile = extras.getString("nameFile");
        }
        ed_NameSp.setText(nameFile);

        previewfile();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    private void previewfile(){
        FileInputStream fin = null;
        try {
            File dirD = new File("/sdcard/Download/");
            File fl = new File(dirD, nameFile);
            fin = new FileInputStream(fl);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String (bytes);
            tv_File.setText(text);
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
    private File getExternalPath(String st) {
        return (new File(Environment.getExternalStorageDirectory(), st));
    }

    public void onClick_openfile(View view){
        parsText(text);
    }

    private void parsText(String st){
        int id_NameSP=-1;
        int id_word=-1;
        String NameSp = ed_NameSp.getText().toString();
        String[] arrst =  st.split(".*\\n");//("[a-zA-Z].*");
        // id списка в базе
        userCursor =  db.rawQuery("select * from sp_word where sp_name = '"+NameSp+"'", null);
        if (userCursor.getCount()==0){
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_SPWORD_NAME, NameSp);
            db.insert(DatabaseHelper.TABLE_SPWORD, null, cv);

            userCursor =  db.rawQuery("select * from sp_word where sp_name = '"+NameSp+"'", null);
            userCursor.moveToFirst();
            id_NameSP = userCursor.getInt(0);
        }
        else {
            userCursor =  db.rawQuery("select * from sp_word where sp_name = '"+NameSp+"'", null);
            userCursor.moveToFirst();
            id_NameSP = userCursor.getInt(0);
        }
        tv_status.setText(Integer.toString(id_NameSP));

        int erraddword = 0;
        int erraddinsp = 0;
        if (arrst.length>1){
            for (int i=0; i<arrst.length; i++ ){
                String[] word = arrst[i].split(" - ");

                try{
                    id_word = checkWordinTable(word[0],word[1]);

                }
                catch (Exception ex){
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    erraddword++;
                }
                try{
                    checkWordinSP(id_NameSP, id_word);
                }
                catch (Exception ex){
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    erraddinsp++;
                }

                }



            }


        tv_status.append("ErrWord: "+erraddword+", ErrSp: "+erraddinsp);
    }

    private  int checkWordinSP(int idsp, int idword){
        userCursor =  db.rawQuery("select * from word_in_sp where id_sp = "+ idsp
                +" AND id_word = "+ idword, null);
        if (userCursor.getCount()==0) {
            ContentValues cv = new ContentValues();
            cv.put("id_sp", idsp);
            cv.put("id_word", idword);
            db.insert(DatabaseHelper.TABLE_WORDINSP, null, cv);

        }
        return 0;
    }

    private int checkWordinTable(String eng , String ru){
        userCursor =  db.rawQuery("select * from word where w_eng = '"+eng+"' AND w_ru = '"+ru+"'", null);
        if (userCursor.getCount()==0) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_WORD_ENG, eng);
            cv.put(DatabaseHelper.COLUMN_WORD_RU, ru);
            db.insert(DatabaseHelper.TABLE_WORD, null, cv);

            userCursor =  db.rawQuery("select * from word where w_eng = '"+eng+"' AND w_ru = '"+ru+"'", null);
            userCursor.moveToFirst();
            return  userCursor.getInt(0);
        }
        else{
            userCursor =  db.rawQuery("select * from word where w_eng = '"+eng+"' AND w_ru = '"+ru+"'", null);
            userCursor.moveToFirst();
            return  userCursor.getInt(0);
        }

    }
}
