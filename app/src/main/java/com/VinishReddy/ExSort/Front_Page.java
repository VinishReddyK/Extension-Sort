package com.VinishReddy.ExSort;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.externsort_prot1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Front_Page extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        tv = findViewById(R.id.textView7);
        tv.setText("\n-This App will sort the files according to their file formats.\n\n-Select the source and destination folders.\n\n-Long press on source (or) destination buttons to see the path\n\n-If the source (or) destination folders are not changed by default they will be at /storage/emulated/0/ .\n\n-Select the file format('s) and hit Action.\n\n-The clear button just clears the text in the box after a round of use (Optional to use).\n\n-Screenshot this it only appears \"once\".");
        FloatingActionButton b = findViewById(R.id.floatingActionButton);
        int lol = 0;
        if(SDK_INT > Build.VERSION_CODES.Q)
        {
            initalcheck();
        }
        else {
            if(ContextCompat.checkSelfPermission(Front_Page.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Front_Page.this,MainActivity.class);
                startActivity(intent);
                Front_Page.this.finish();
            }
        }
        b.setOnClickListener(v -> {
            if(SDK_INT > Build.VERSION_CODES.Q)
            {
                requestPermission();
            }
            else{
                if(ContextCompat.checkSelfPermission(Front_Page.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(Front_Page.this,MainActivity.class);
                    startActivity(intent);
                    Front_Page.this.finish();
                }

                ActivityCompat.requestPermissions(Front_Page.this,new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                },lol);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initalcheck() {
        if (Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Front_Page.this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestPermission() {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}