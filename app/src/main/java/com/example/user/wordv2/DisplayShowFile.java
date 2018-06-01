package com.example.user.wordv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class DisplayShowFile extends AppCompatActivity {
    private final static String FILE_NAME = "content.txt";
    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private boolean permissionGranted;

    ListView lv_File;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_show_file);
        lv_File = (ListView) findViewById(R.id.lv_File);
        getFiles(null);
        lv_File.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) lv_File.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DisplayLoadFile.class );
                intent.putExtra("nameFile", s);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Мы кликнули на пункт № " +
                //        position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getFiles(View view){
        if(!permissionGranted){
            permissionGranted = checkPermissions();
            if(!permissionGranted){ return;}
        }
        File dirD = new File("/sdcard/Download/");
        String[] fl = new String[0];
        if (dirD.exists()){
            fl = dirD.list();
        }
        ArrayAdapter<String> StAd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fl);
        lv_File.setAdapter(StAd);
    }

    // проверяем, доступно ли внешнее хранилище для чтения и записи
    public boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }
    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
    private boolean checkPermissions(){
        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){

            case REQUEST_PERMISSION_WRITE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGranted = true;
                    Toast.makeText(this, "Разрешения получены", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Необходимо дать разрешения", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
