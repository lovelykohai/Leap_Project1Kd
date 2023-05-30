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

public class AdminRoom extends AppCompatActivity { //Admin room has mostly been deprecated, home screen now moves to select cameras, however some messy legacy code points to the admin room for UI scalings
    ImageView CameraBtn;
    ImageView BackBtn;
    View decorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //Set up the UI
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
    @Override
    public void onWindowFocusChanged(boolean hasFocus){//Every class has this 'On window focus' and 'Hide sys bars' their purpose is to hide the home bar, preventing the user from easily exiting the app
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