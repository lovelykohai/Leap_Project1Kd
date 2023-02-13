package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AdminRoom extends AppCompatActivity {
    ImageView ResetBtn;
    ImageView CameraBtn;
    ImageView BackBtn;
    View decorView;
    File GifTracker;
    String FileName= "GifTracker.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        CameraBtn = (ImageView) findViewById(R.id.Admin_btn_Camera);
        BackBtn = findViewById(R.id.Admin_back);
        ResetBtn = (ImageView) findViewById(R.id.Admin_btn_reset);
        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        GifTracker = new File(d,"GifTracker.txt");
        SetResetBtn();
        setCameraBtn();
        SetBtnBack();
    }
    public void SetBtnBack(){
        BackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }
    public void setCameraBtn(){
        CameraBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),Select_Cameras.class);
                startActivity(i);
            }
        });
    }
    public void SetResetBtn(){
        ResetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Toast.makeText(getApplicationContext(),"Contador Reinicializar", Toast.LENGTH_LONG).show();
                FileInputStream fis = null;
                try {
                    if(!GifTracker.exists()){
                        Log.i("T","He file did not exist");
                        GifTracker.createNewFile();
                    }
                     fis = new FileInputStream(GifTracker);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String text;
                    while((text = br.readLine())!=null){
                        sb.append(text).append("\n");
                    }
                    Log.i("Text ", sb.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(GifTracker);
                    BufferedOutputStream Buff = new BufferedOutputStream(fos);
                    byte[] bs = "001".getBytes();
                    Buff.write(bs);
                    Buff.flush();
                    Buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }

        });
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