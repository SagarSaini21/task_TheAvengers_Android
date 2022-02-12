package com.example.task_theavengers_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {
    EditText title,categorySelected,desc;
    Button addTask,addCat,addImages,back,next;
    private static int REQUEST_MICROPHONE=200;
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

        // Requesting Mic Permissions
        if(isMicrophonePresent()){
            getMicPerm();
        }

        //Buttons
        addTask=findViewById(R.id.btn_create_task);
        addCat=findViewById(R.id.btn_add_new);
        addImages=findViewById(R.id.btn_add_images);
        back= findViewById(R.id.btnback);
        next= findViewById(R.id.btnnext);
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



}