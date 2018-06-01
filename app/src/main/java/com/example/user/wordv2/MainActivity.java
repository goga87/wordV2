package com.example.user.wordv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick_Show_DisplayDb (View view){
        Intent intent = new Intent(this,DisplayDb.class );
        intent.putExtra("name", "value");
        startActivity(intent);
    }
    public void onClick_Show_file (View view){
        Intent intent = new Intent(this,DisplayShowFile.class );
        intent.putExtra("name", "value");
        startActivity(intent);
    }
    public void onClick_Show_Eng_ru (View view){
        Intent intent = new Intent(this,DisplayShowSP.class );
        intent.putExtra("trans", "eng");
        startActivity(intent);
    }
    public void onClick_Show_Ru_eng (View view){
        Intent intent = new Intent(this,DisplayShowSP.class );
        intent.putExtra("trans", "ru");
        startActivity(intent);
    }
    //temp
}
