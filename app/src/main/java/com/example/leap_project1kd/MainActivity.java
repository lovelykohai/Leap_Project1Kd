package com.example.leap_project1kd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    ImageView userButton;
    ImageView AdminBtn;
    TextView Password;
    public static Uri camera1URI;
    public static Uri camera2URI;
    View decorView;
    public static Uri camera3URI;
    public static Uri camera4URI;
    public static Uri SaveFolderURI;
    TextView Credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userButton = (ImageView) findViewById(R.id.btn_user_home);
        setUserBtn();
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        Password = findViewById(R.id.Password);
        AdminBtn = (ImageView) findViewById(R.id.btn_admin);
        Credit = findViewById(R.id.textView3);
        Credit.setAlpha((float)0.05);
        setAdminBtn();
        CreateFiles();
    }
    public void setUserBtn(){
        userButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),UserHome.class);
                startActivity(i);
            }
        });
    }
    public void setAdminBtn(){
        AdminBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                if(Password.getText().toString().equals("C234")){ //Password is hard coded, the intention is just so the users can't access it
                    //All of the keys are on the left side of the keyboard because the right side is covered up by plastic
                    Intent i = new Intent(getApplicationContext(),Select_Cameras.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Wrong password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void CreateFiles(){ //Set up files on app start up that will be needed for file transfer, this sometimes doesn't work for some reason but we have failsafes later
        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        FileInputStream fis = null;
        for (int i = 0; i<5;i++){ //Creates a txt file for each URI chosen in Select_Cameras, this is to store their location permanantly in memory
            //They are also stored in scratch memory for direct reference, but this is destroyed on reset. The hard file is there so the cameras can be accessed whenever the app is used
            //The URI should be unique from tablet to tablet so this avoids data contamination
            File FileTrack = new File(d,"File"+i+".txt");
            try {
                if(!FileTrack.exists()){
                    Log.i("T","He file did not exist");
                    FileTrack.createNewFile();
                    WriteFile(FileTrack);
                }
                else{
                    Log.i("T","he file already exists");
                    fis = new FileInputStream(FileTrack);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String text;
                    while((text = br.readLine())!=null){
                        sb.append(text);
                    }
                    setUris(Uri.parse(sb.toString()),i+1);
                }
                fis = new FileInputStream(FileTrack);
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
        }
    private void WriteFile(File GifTracker){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(GifTracker);
            BufferedOutputStream Buff = new BufferedOutputStream(fos);
            byte[] bs = "content://com.android.externalstorage.documents/tree/primary%3ADCIM".getBytes();
            Buff.write(bs);
            Buff.flush();
            Buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Uri getUris(int reqCode){
        switch(reqCode){
            case 1:
                return camera1URI;
            case 2:
                return camera2URI;
            case 3:
                return camera3URI;
            case 4:
                return camera4URI;
            case 5:
                return SaveFolderURI;
            default:
                return null;
        }
    }
    public static void setUris(Uri uri, int Code){//Stores the URI of each camera for later grabbing
        switch(Code){
            case 1:
                camera1URI = uri;
                Log.i("Camera 1 uri", String.valueOf(camera1URI));
                break;
            case 2:
                camera2URI = uri;
                Log.i("Camera 2 uri", String.valueOf(camera2URI));
                break;
            case 3:
                camera3URI = uri;
                Log.i("Camera 3 uri", String.valueOf(camera3URI));
                break;
            case 4:
                camera4URI = uri;
                Log.i("Camera 4 uri", String.valueOf(camera4URI));
                break;
            case 5:
                SaveFolderURI = uri;
            default:
                Log.i("Set Uri Log", "Null return");
        }
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