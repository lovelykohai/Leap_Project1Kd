package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class backup_and_charge_1 extends AppCompatActivity { //B&C 1 acts as a 'buffer room' with the purpose of just displaying text, it exists so the text scroller in B&C2 can always be correct
    ImageView CameraPlugged; //While B&C1 can be deprecated, it allows for a potential bug relating to constantly shifting between text boxes in B&C2, so it was decided to keep B&C1.
   View decorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_and_charge1);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        CameraPlugged = findViewById(R.id.CameraPlugged);
        CameraPlugged.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),backup_and_charge_2.class);
                startActivity(i);
            }
        });


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){ //Every class has this 'On window focus' and 'Hide sys bars' their purpose is to hide the home bar, preventing the user from easily exiting the app

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