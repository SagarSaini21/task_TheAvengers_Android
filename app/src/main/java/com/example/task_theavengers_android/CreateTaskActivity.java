package com.example.task_theavengers_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Spinner spinner_category;
    private TaskRoomDatabase taskRoomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        // Get Singleton class of Task Room DB
        taskRoomDatabase = TaskRoomDatabase.getInstance(this);

        title=findViewById(R.id.edt_title);
        categorySelected=findViewById(R.id.edt_category);
        desc=findViewById(R.id.edt_description);
        imageURI = new ArrayList<>();
        img = findViewById(R.id.imgdemo);
        isRecording=false;
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        // Populate spinner with catagories from Room DB
        spinner_category = findViewById(R.id.spinner_category);
        List<Category> categories = taskRoomDatabase.categoryDao().getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category cat:categories) {
            categoryNames.add(cat.getName());
        }
        ArrayAdapter<String> categoryNameAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                categoryNames
                );
        spinner_category.setAdapter(categoryNameAdapter);

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


//--------------------------Date Picker Functions--------------------------------
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
       // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
       // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";


        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
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