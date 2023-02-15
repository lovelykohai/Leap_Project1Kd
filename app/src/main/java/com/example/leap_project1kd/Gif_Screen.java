package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import pl.droidsonroids.gif.GifImageView;

public class Gif_Screen extends AppCompatActivity {
    GifImageView myGif;
    ImageView contd;
    ImageView green;
    String FileName= "GifTracker.txt";
    String CurrentGif;
    View decorView;
    GifImageView ThreeTwoOne;
    int NextGif = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File GifTracker = new File(d,"GifTracker.txt");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_screen);
        myGif = (GifImageView) findViewById(R.id.MyGif);
        myGif.setAlpha((float) 0);
        green = findViewById(R.id.tImageView);
        green.setAlpha((float) 0);
        ThreeTwoOne = findViewById(R.id.ThreeTwoOne);
        ThreeTwoOne.setAdjustViewBounds(true);
        ThreeTwoOne.setAlpha((float) 0);
        ThreeTwoOne.setScaleType(ImageView.ScaleType.FIT_XY);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        ThreeTwoOne.bringToFront();
        contd = findViewById(R.id.contd);
        contd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                        FileInputStream fis = null;
                        try {
                            if(!GifTracker.exists()){
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
                            CurrentGif = sb.toString();
                            Log.i("Pre everything CG ", CurrentGif);
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
                        int i = getResourceId("code"+CurrentGif.trim(), "drawable", getPackageName());
                        Log.i("Current Gif:",CurrentGif);
                        Log.i("Name:" , "code"+CurrentGif+".gif");
                        Log.i("Resource Id:", String.valueOf(i));
                        NextGif = Integer.parseInt(CurrentGif.trim());
                        NextGif = NextGif+1;
                        if(NextGif<10){
                            CurrentGif = "00"+String.valueOf(NextGif);
                        }
                        else if (NextGif<100){
                            CurrentGif = "0"+String.valueOf(NextGif);
                        }
                        else{
                            CurrentGif = String.valueOf(NextGif);
                        }
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(GifTracker);
                            BufferedOutputStream Buff = new BufferedOutputStream(fos);
                            byte[] bs = CurrentGif.getBytes();
                            Buff.write(bs);
                            Buff.flush();
                            Buff.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }   finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("Current Gif:",CurrentGif);
                        Log.i("Name:" , "code"+CurrentGif+".gif");
                        Log.i("Resource Id:", String.valueOf(i));
                        myGif.setImageResource(i);
                        myGif.setBackgroundResource(i);
                        myGif.setAlpha((float)1);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                green.setAlpha((float)1);
                                Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    public void run() {
                                        Intent i = new Intent(getApplicationContext(),UserHome.class);
                                        startActivity(i);
                                    }
                                }, 3000);
                            }
                        }, 10000);
                    }
        });
    }
    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            Log.i("Resource ID in test:", String.valueOf(getResources().getIdentifier(pVariableName, pResourcename, pPackageName)));

            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
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