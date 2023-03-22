package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.anggrayudi.storage.file.DocumentFileCompat;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Select_Cameras extends AppCompatActivity {
    ImageView camera1;
    ImageView camera2;
    View decorView;
    ImageView camera3;
    ImageView camera4;
    ImageView SaveLocation;
    ImageView btn_back;
    Button btn;
    int cameraTrack;
    private static final int RQS_OPEN_DOCUMENT_TREE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cameras);
        camera1 = findViewById(R.id.Camera_1);
        camera2 = findViewById(R.id.Camera_2);
        btn = findViewById(R.id.button);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        camera3 = findViewById(R.id.Camera_3);
        camera4 = findViewById(R.id.Camera_4);
        SaveLocation = findViewById(R.id.btn_select_location);
        btn_back = findViewById(R.id.btn_back);

        setCameras();
        setBack();
    }
    public void setBack(){
        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File f = new File(d,"File0.txt");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if(f.exists()){
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(f));
                        String line;
                        while ((line = br.readLine()) != null) {
                            Log.i("Line"," "+line);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("", (""+String.valueOf(MainActivity.getUris(1))));
            }
        });
    }
    public void setCameras(){
        camera1.setOnClickListener(imgClk);
        camera2.setOnClickListener(imgClk);
        camera3.setOnClickListener(imgClk);
        camera4.setOnClickListener(imgClk);
        SaveLocation.setOnClickListener(imgClk);
    }

    public View.OnClickListener imgClk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.Camera_1:
                    cameraTrack = 1;
                    break;
                case R.id.Camera_2:
                    cameraTrack = 2;
                    break;
                case R.id.Camera_3:
                    cameraTrack = 3;
                    break;
                case R.id.Camera_4:
                    cameraTrack = 4;
                    break;
                case R.id.btn_select_location:
                    cameraTrack = 5;
                    break;
            }
            Intent openDocumentIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            openDocumentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(openDocumentIntent, RQS_OPEN_DOCUMENT_TREE);
        }};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RQS_OPEN_DOCUMENT_TREE) {
            Uri uriTree = data.getData();
            getContentResolver().takePersistableUriPermission(uriTree, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(uriTree, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            switch(cameraTrack){
                case 1:
                    WriteToFile(0,uriTree); //camera 1
                    MainActivity.setUris(uriTree, 1);
                    break;
                case 2:
                    WriteToFile(1,uriTree); //camera 2
                    MainActivity.setUris(uriTree, 2);
                    break;
                case 3:
                    WriteToFile(2,uriTree); //camera 3
                    MainActivity.setUris(uriTree, 3);
                    break;
                case 4:
                    WriteToFile(3,uriTree); //camera 4
                    MainActivity.setUris(uriTree, 4);
                    break;
                case 5:
                    WriteToFile(4,uriTree); //Save Location
                    MainActivity.setUris(uriTree, 5);
            }
        }
    }
    public void WriteToFile(int i, Uri URIT){
        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File f = new File(d,"File"+i+".txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            BufferedOutputStream Buff = new BufferedOutputStream(fos);
            byte[] bs = String.valueOf(URIT).getBytes();
            Buff.write(bs);
            Buff.flush();
            Buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }
    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }
}