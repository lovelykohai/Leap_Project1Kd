package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalTime;
import java.time.LocalDate;
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
    int day;
    int hour;
    int counter;
    int dec;
    String FileName= "GifTracker.txt";
    String CurrentGif;
    View decorView;
    GifImageView ThreeTwoOne;
    int NextGif = 1;
    FileInputStream fis = null;
    File d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        CreateFile();
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        day = date.getDayOfMonth();
        hour = time.getHour();
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
                        String dec1 =  SetTime();
                        int Count = ReadFile();
                        Log.i("Counter ", String.valueOf(Count));
                        String counterbin = String.format("%2s", Integer.toBinaryString(Count)).replaceAll(" ", "0");
                        String fullbin = dec1+counterbin;
                        int dec = Integer.parseInt(fullbin, 2);
                        IncrementText(Count);
                        Log.i("Dec: ", String.valueOf(dec));
                        Log.i("Current Gif:",String.valueOf(dec));
                        Log.i("Name:" , "code"+String.valueOf(dec)+".gif");;
                        NextGif = Integer.parseInt(String.valueOf(dec).trim());
                        if(NextGif<10){
                            CurrentGif = "00"+NextGif;
                        }
                        else if (NextGif<100){
                            CurrentGif = "0"+NextGif;
                        }
                        else{
                            CurrentGif = String.valueOf(NextGif);
                        }
                        int i = getResourceId("code"+String.valueOf(CurrentGif), "drawable", getPackageName());
                        Log.i("Current Gif:", String.valueOf(CurrentGif));
                        Log.i("Name:" , "code"+String.valueOf(CurrentGif)+".gif");
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
                        }, 11000);
                    }
        });
    }
    public String SetTime(){
        int dec1;
        int day3bits = day % 8;
        String daybin = String.format("%3s", Integer.toBinaryString(day3bits)).replaceAll(" ", "0");
        Log.i("Daybin: ", daybin);
        // Convert the hour of the day to binary - will be 5 bits
        String hourbin = String.format("%5s", Integer.toBinaryString(hour)).replaceAll(" ", "0");
        Log.i("hourbin: ", hourbin);
        // Create 8 bit code by concatenating
        String bin = daybin + hourbin;
        Log.i("bin: ", bin);
        // Convert back to decimal
        //dec1 = Integer.parseInt(bin, 2); // Use this value to choose GIF
        //Log.i("Dec1: ", String.valueOf(dec1));
        return bin;
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
    private void CreateFile(){

        File GifTracker = new File(d,"Counter.txt");
        try {
            if(!GifTracker.exists()){
                Log.i("T","He file did not exist");
                GifTracker.createNewFile();
                WriteFile();
            }
            else{
                Log.i("T","he file already exists");
            }
            fis = new FileInputStream(GifTracker);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine())!=null){
                sb.append(text);
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
    }
    private void WriteFile(){
        File GifTracker = new File(d,"Counter.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(GifTracker);
            BufferedOutputStream Buff = new BufferedOutputStream(fos);
            byte[] bs = "000".getBytes();
            Buff.write(bs);
            Buff.flush();
            Buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int ReadFile(){
        int Counter;
        File GifTracker = new File(d,"Counter.txt");
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
                sb.append(text);
            }
            Log.i("Text: ", sb.toString());
            Counter = Integer.parseInt(sb.toString());
            return Counter;
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
        return 0;
    }
    private void IncrementText(int currentVal){
        FileInputStream fis = null;
        File GifTracker = new File(d,"Counter.txt");
        int nextVal = 0;
        switch (currentVal){
            case 0:
                nextVal = 1;
                break;
            case 1:
                nextVal = 2;
                break;
            case 2:
                nextVal = 3;
                break;
            case 3:
                nextVal = 0;
                break;
            default:
                break;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(GifTracker);
            BufferedOutputStream Buff = new BufferedOutputStream(fos);
            byte[] bs = String.valueOf(nextVal).getBytes();
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
    }
}