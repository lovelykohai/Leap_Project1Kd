package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Start_Session extends AppCompatActivity { //Simple activity to display instructions and move to GIF screen
    ImageView Btn_confirm;
    TextView TheText;
    ImageView Btn_Back;
    View decorView;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //Creates the UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_session);
        Btn_confirm = findViewById(R.id.Btn_confirm);
        Btn_Back = findViewById(R.id.btn_bck_session);
        TheText = findViewById(R.id.textView11);
        decorView = getWindow().getDecorView();
        Btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter-1;
                TextSetter();
            }
        });
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        Btn_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                counter = counter+1;
                TextSetter();
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
    public void TextSetter(){ //Cycles through instructions
        switch (counter){
            case -1:
                counter = 0;
                Intent i = new Intent(getApplicationContext(),UserHome.class);
                startActivity(i);
                break;
            case 0:
                TheText.setText("If the body sensors are connected, disconnect them");
                break;
            case 1:
                TheText.setText("If the cameras you intend to use are connected, disconnect them");
                break;
            case 2:
                TheText.setText("Ensure that both cameras and body sensors show flashing lights");
                break;
            case 3:
                TheText.setText("Please place the cameras and sensors in their correct positions");
                break;
            case 4:
                TheText.setText("Turn on the cameras and body sensors");
                break;
            case 5:
                counter = 0;
                Intent i2 = new Intent(getApplicationContext(),Gif_Screen.class);
                startActivity(i2);
                break;
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