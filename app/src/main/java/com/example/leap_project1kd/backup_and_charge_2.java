package com.example.leap_project1kd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.anggrayudi.storage.callback.FileCallback;
import com.anggrayudi.storage.callback.FolderCallback;
import com.anggrayudi.storage.file.DocumentFileCompat;
import com.anggrayudi.storage.file.DocumentFileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class backup_and_charge_2 extends AppCompatActivity {//B&C2 has two purposes, 1 to showcase the instructions for the users to follow and 2 to initiate the commands given by SimpleStorageAPI
    ImageView Confirm;
    View decorView;
    int URITracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_and_charge2);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        Confirm = findViewById(R.id.Btn_Confirm);
        CreateConfirm();
    }
    public void CreateConfirm(){//Gives the second insturction for the users
        Confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                VideoThread thread = new VideoThread();
                thread.start();//A thread is required to start to transfer files, this is why the A8 was chosen, a single thread runs the UI, a second thread runs the application
                //Then a third thread is required to run the API, file mobility in android is layer protected, meaning it has to be done this way, a background core is used for variable control, 4-cores is the minimum to run this app.
                UserHome.TheTimeWarning = "Por favor, deixe as câmaras carregando por pelo menos 60 minutos";
                //The average backup takes ~90 seconds, however in extremely rare cases, this can go up to 20 minutes, for safety purposes and data preservation (as well as file management)
                //We ask the users to not use the same camera more than once per hour, this also allows for an hours charge which is roughly what the camera needs.
                Intent i = new Intent(getApplicationContext(),UserHome.class);
                startActivity(i);
            }
        });
    }

    class VideoThread extends Thread{
        public VideoThread(){
        }
        @Override public void run(){//As the thread starts, it initialises the URI of each camera, it does this by communicating with main and gathering the URIs placed there
            //This is why we run the consistent background core, the main activity is consistently loading small parts while the app is in focus, so to not destroy the needed variables
            Uri[] Uris={null,null,null,null,null};
            Uris[0] = MainActivity.getUris(1);
            Uris[1] = MainActivity.getUris(2);
            Uris[2] = MainActivity.getUris(3);
            Uris[3] = MainActivity.getUris(4);
            for(int x=0; x<=3; x++){
                if (Uris[x]!=null){
                    URITracker = x;
                    FileSave(Uris[x]);//Iterate through each camera, check which one is currently plugged in, if a camera is found to be plugged in, back it up
                }
                else{
                    Log.i(String.valueOf(x)," is null");
                }
            }
        }
    }
    public void FileSave(Uri uri){

        DocumentFile CameraPath = DocumentFileCompat.fromUri(this, uri);
        for (DocumentFile file : CameraPath.listFiles()) { //Once a URI is found to be attached to the tablet, then each file on the URI is checked
            Log.i("",file.getName());
            if(file.isDirectory()){
                Log.i("","is a Directory");
                File2Save(file.getUri());//If the file is a folder, check if the folder already exists on the tablet, if not (this happens on the first run each day) create a new folder and save it
            }else{
                if(file.canRead()||file.canWrite()){
                    FileSaveForReal(file.getUri());//If the file is not a folder, check that it is not a protected file, if it isn't, save the file.
                }
                Log.i("","is not a Directory");
            }

            Log.i("file.canRead(): " , String.valueOf(file.canRead()));
            Log.i("file.canWrite(): " , String.valueOf(file.canWrite()));

            Log.i("", String.valueOf(file.getUri()));
        }

    }
    public void File2Save(Uri URI) {
        String Folder = "null";
        switch (URITracker) { //Name the folder according to what URI it is parsed from.
            case 0:
                Folder = "Camera 1";
                break;
            case 1:
                Folder = "Camera 2";
                break;
            case 2:
                Folder = "Camera 3";
                break;
            case 3:
                Folder = "Camera 4";
                break;
        }
        //This is the code to save a folder
        Uri SDUri = MainActivity.getUris(5);
        DocumentFile SDPath = DocumentFileCompat.fromUri(this, SDUri);
        DocumentFile CameraPath = DocumentFileCompat.fromUri(this, URI);
        DocumentFileUtils.copyFolderTo(CameraPath, this, SDPath, true, Folder, new FolderCallback() {
            @Override
            public void onParentConflict(@NonNull DocumentFile destinationFolder, @NonNull ParentFolderConflictAction action, boolean canMerge) {
                action.confirmResolution(ConflictResolution.MERGE);
            }
            @Override
            public void onContentConflict(@NonNull DocumentFile destinationFolder, @NonNull List<FileConflict> conflictedFiles, @NonNull FolderContentConflictAction action) {
                handleFolderContentConflict(action, conflictedFiles);
            }
            public void handleFolderContentConflict(FolderContentConflictAction action, List<FileConflict> conflictedFiles)
            {
                askSolution(action, conflictedFiles, new ArrayList<FileConflict>(conflictedFiles.size()));
            }
            public void askSolution(FolderContentConflictAction action, List<FileConflict> conflictedFiles, List<FileConflict> newSolution)
            {
                FileConflict currentSolution = CollectionsKt.removeFirstOrNull(conflictedFiles);
                if (currentSolution == null)
                {
                    action.confirmResolution(newSolution);
                    return;
                }
                currentSolution.setSolution(FileCallback.ConflictResolution.REPLACE);
                conflictedFiles.forEach (it -> it.setSolution(currentSolution.getSolution()));
                newSolution.addAll(conflictedFiles);
                action.confirmResolution(newSolution);
            }

        });
    }
    public void FileSaveForReal(Uri URI){ //Code for saving specific files
        Uri SDUri = MainActivity.getUris(5);
        DocumentFile SDPath = DocumentFileCompat.fromUri(this,SDUri);
        DocumentFile CameraPath = DocumentFileCompat.fromUri(this, URI);
        DocumentFileUtils.copyFileTo(CameraPath, this, SDPath, null, new FileCallback() {
            @Override
            public void onCompleted(@NotNull Object result) {
                Log.i("File Copied","Sucessfully");
            }
            @Override
            public void onReport(Report report) {
                Log.d("On Report%s", String.valueOf(report.getProgress()));
            }

            @Override
            public void onFailed(ErrorCode errorCode) {
                Log.d("Error: %s", errorCode.toString());
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