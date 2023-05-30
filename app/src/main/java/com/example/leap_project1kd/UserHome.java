package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserHome extends AppCompatActivity { //Just a screen for users to select if they're backing up or doing a sync.
    ImageView backup;
    View decorView;
    ImageView start;
    TextView TimeWarning;
    static String TheTimeWarning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        backup = findViewById(R.id.Btn_backup);
        TimeWarning = findViewById(R.id.TimeWarning);
        start = findViewById(R.id.Btn_start_session);
        if(TheTimeWarning!=null){
            TimeWarning.setText(TheTimeWarning);
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                public void run() {
                    TheTimeWarning = "";
                    TimeWarning.setText(TheTimeWarning);
                }
            }, 3600000); //Displays for an hour
        }
        setupStart();
        setupBackup();
    }
    public void setupStart(){
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),Start_Session.class);
                startActivity(i);
            }
        });
    }
    public void setupBackup(){
        backup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),backup_and_charge_1.class);
                startActivity(i);
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