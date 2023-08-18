package com.VinishReddy.ExSort;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.externsort_prot1.R;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import lib.folderpicker.FolderPicker;

public class MainActivity extends AppCompatActivity {
    public  String  locationSource="/storage/emulated/0";
    public  String  locationDestination="/storage/emulated/0";
    public  String  process ="s";

    boolean[] selectedbool;

    Button e3,des,reverse,clear;
    TextView tw;
    TextView textView;
    ArrayList <String> extensions = new ArrayList<>(),selected=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);

        clear = findViewById(R.id.button3);
        des=findViewById(R.id.textView4);
        e3 = findViewById(R.id.textView3);
        reverse = findViewById(R.id.button2);
        e3.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tw = findViewById(R.id.datetext1);
        Button b = findViewById(R.id.button);
        selectedbool = new boolean[200];


        textView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Select Formats(Source)");


            if(extensions.size()==0){
                builder.setMessage("\nNo Files With Formats Found");
            }
            else {
                builder.setMultiChoiceItems(returnstr(extensions), selectedbool, (dialog, which, isChecked) -> {

                    if (isChecked) {
                        selected.add(extensions.get(which));
                    } else {
                        selected.remove(extensions.get(which));
                    }
                });
            }
            builder.setPositiveButton("ok", (dialog, which) -> {
                System.out.println(selected);
                dialog.dismiss();
            });
            builder.show();
        });



        resetextensions();





        b.setOnClickListener(v -> {
            if(selected.size()==0) {
                Toastmsg("Select AtLeast One Format To Proceed");
                return;
            }
            String temp;
            tw.setText("");
            File source1 = new File(locationSource);
            int flag=0;
            String[] files1 = source1.list();
            for(int loop=0;selected.size()-1>=loop;++loop) {
                for (int i = 0; i < Objects.requireNonNull(files1).length; i++) {
                    int length = selected.get(loop).length();
                    if (!(new File(locationSource + "/" + files1[i]).isHidden()) && !(new File(locationSource + "/" + files1[i]).isDirectory())) {
                        if (files1[i].length() > (length + 1) && files1[i].substring(files1[i].length() - (length + 1)).equals("."+selected.get(loop))) {
                            flag = 100;
                        }
                    }
                }
            }
            if (!(flag == 100))
            {
                Toastmsg("Nothing Found by selected extensions");
                return;
            }
            for(int loop=0;selected.size()-1>=loop;loop++)
            {
                for (int i = 0; i < Objects.requireNonNull(files1).length; i++)
                {
                    if(!(new File(locationSource+"/"+ files1[i]).isHidden()) && !(new File(locationSource+"/"+ files1[i]).isDirectory()))
                    {
                        int length=selected.get(loop).length();
                        if(files1[i].length()>(length+1) && files1[i].substring(files1[i].length() -(length+1)).equals("."+selected.get(loop)))
                        {
                        flag =3;
                        temp=tw.getText().toString();
                        temp ="     "+ temp + "\n" + files1[i];
                        try
                        {
                            flag=movefile(locationSource+"/"+ files1[i],locationDestination+"/"+ files1[i], files1[i]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (flag==1)
                        {
                            tw.setText(temp);
                        }
                        }

                    }
                }
                Toastmsg("Moving of "+ selected.get(loop) +" file('s) finished");
            }
            resetextensions();
        });
        reverse.setOnClickListener(v -> {
            String temp =locationSource;
            locationSource=locationDestination;
            locationDestination=temp;
            Toastmsg("Swaped Source and Destinaton");
        });
        e3.setOnClickListener(v -> {
            process = "s";
            Intent intent = new Intent(MainActivity.this, FolderPicker.class);
            startActivityForResult(intent,10);
        });
        e3.setOnLongClickListener(v -> {
            Toastmsg("Source : "+locationSource);
            return true;
        });

        des.setOnClickListener(v -> {
            process = "d";
            Intent intent = new Intent(MainActivity.this, FolderPicker.class);
            startActivityForResult(intent,10);
        });
        des.setOnLongClickListener(v -> {
            Toastmsg("Destination : "+locationDestination);
            return true;
        });
        clear.setOnClickListener(v -> tw.setText(""));

    }

    private void resetextensions() {
        extensions.clear();
        selected.clear();
        Arrays.fill(selectedbool, false);
        File source = new File(locationSource);
        String[] files = source.list();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            if (!(new File(locationSource + "/" + files[i]).isHidden()) && !(new File(locationSource + "/" + files[i]).isDirectory())) {
                String f = FilenameUtils.getExtension(files[i]);
                if (!extensions.contains(f)) {
                    extensions.add(f);
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println(process);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
                if(process.equals("s")) {
                    locationSource = intent.getExtras().getString("data");
                    Toastmsg("Source : "+locationSource);
                    resetextensions();
                }
                if(process.equals("d")) {
                    locationDestination = intent.getExtras().getString("data");
                    Toastmsg("Destination : "+locationDestination);
                }

        }
    }
    public  int movefile(String s,String d,String f) throws IOException {
        Path source = Paths.get(s);
        Path destination = Paths.get(d);
        try {
            Files.move(source, destination);
        }catch (FileAlreadyExistsException e)
        {
            StyleableToast.makeText(this, "\""+f+"\""+ "already exists", Toast.LENGTH_LONG, R.style.toastTextStyle).show();
            return 0;
        }
        return 1;
    }
    void  Toastmsg(String Msg) { StyleableToast.makeText(this, Msg, Toast.LENGTH_SHORT, R.style.toastTextStyle).show(); }

    String[] returnstr(ArrayList<String> al)
    {
        String[] str = new String[al.size()];

        for (int i = 0; i < al.size(); i++) {
            str[i] = al.get(i);
        }
        return str;
    }
}
