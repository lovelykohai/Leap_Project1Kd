package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Start_Session extends AppCompatActivity {
    ImageView Btn_confirm;
    TextView TheText;
    ImageView Btn_Back;
    View decorView;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public void TextSetter(){
        switch (counter){
            case -1:
                counter = 0;
                Intent i = new Intent(getApplicationContext(),UserHome.class);
                startActivity(i);
                break;
            case 0:
                TheText.setText("Se os sensores corporais estiverem conectados, desconectá-los");
                break;
            case 1:
                TheText.setText("Se as câmeras que pretende utilizar estiverem conectados, desconectá-los");
                break;
            case 2:
                TheText.setText("Assegurar que tanto as câmaras como os sensores corporais mostram luzes a piscar");
                break;
            case 3:
                TheText.setText("Por favor, coloque as câmaras e os sensores nas suas posições à direita");
                break;
            case 4:
                TheText.setText("Ligar as câmaras e os sensores corporais");
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