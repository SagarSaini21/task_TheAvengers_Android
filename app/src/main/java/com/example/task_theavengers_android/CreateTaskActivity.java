package com.example.task_theavengers_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {
    EditText title,categorySelected,desc;
    Button addTask,addCat,addImages,back,next,recordButton,playButton;
    private static int REQUEST_MICROPHONE=200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private boolean isRecording=false;

    // Variables for images
    ImageSwitcher img;
    private ArrayList<Uri> imageURI;
    private static final int PICK_IMAGES_CODE=0;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        title=findViewById(R.id.edt_title);
        categorySelected=findViewById(R.id.edt_category);
        desc=findViewById(R.id.edt_description);
        imageURI = new ArrayList<>();
        img = findViewById(R.id.imgdemo);
        isRecording=false;


        // Requesting Mic Permissions
        if(isMicrophonePresent()){
            getMicPerm();
        }else{
            Toast.makeText(this, "No Mic", Toast.LENGTH_SHORT).show();
        }

        //Buttons
        addTask=findViewById(R.id.btn_create_task);
        addCat=findViewById(R.id.btn_add_new);
        addImages=findViewById(R.id.btn_add_images);
        back= findViewById(R.id.btnback);
        next= findViewById(R.id.btnnext);
        recordButton=findViewById(R.id.btnRecord);
        playButton=findViewById(R.id.btnplay);
        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               imageURI.clear();
               pickImageIntent();
            }
        });



//---------------Image Switcher implementation---------------------------------------------------

        img.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView= new ImageView(getApplicationContext());
                return imageView;

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position>0){
                    position--;
                        img.setImageURI(imageURI.get(position));
                }else{
                    Toast.makeText(CreateTaskActivity.this, "No Previous Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imageURI.size()-1){
                    position++;
                    img.setImageURI(imageURI.get(position));
                }else{
                    Toast.makeText(CreateTaskActivity.this, "No Next Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

//-------------------------- xxxxxxxxxxxxxxxxx ----------------------

    recordButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(isRecording)
           {
              recordButton.setText("Record");
              isRecording=false;
              mediaRecorder.stop();
              mediaRecorder.release();
              mediaRecorder = null;
               Toast.makeText(CreateTaskActivity.this, "Recording Stopped", Toast.LENGTH_SHORT).show();
           }else{
               try
               {
                   mediaRecorder = new MediaRecorder();
                   mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                   mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                   mediaRecorder.setOutputFile(getFilePath());
                   mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                   mediaRecorder.prepare();
                   mediaRecorder.start();
                   recordButton.setText("Stop");
                   isRecording = true;
                   Toast.makeText(CreateTaskActivity.this, "Recording Started...", Toast.LENGTH_SHORT).show();
               }catch(Exception e){
                   e.printStackTrace();
                   Toast.makeText(CreateTaskActivity.this, "error", Toast.LENGTH_SHORT).show();

               }

           }

        }
    });

    playButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getFilePath());
                mediaPlayer.prepare();
                mediaPlayer.start();

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    });
    }

//-------------------Functions for Picking Images---------------------------

    private void pickImageIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGES_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for(int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageURI.add(imageUri);
                    }
                  img.setImageURI(imageURI.get(0));
                    position=0;
                }else if(data.getData() != null) {
                    Uri imageUri=data.getData();
                    imageURI.add(imageUri);
                    img.setImageURI(imageURI.get(0));
                    position=0;

                }
            }
        }
    }
//-----------------XXXXXXXX---------------------------------------

//----------------Microphone Permission Methods----------------

    private boolean isMicrophonePresent(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }

    }

    private void getMicPerm()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);

        }
    }
    private String getFilePath()
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory,"testname"+".mp3");
        return file.getPath();
    }


}