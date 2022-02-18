package com.example.task_theavengers_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.task_theavengers_android.adapter.DetailsImagesAdapter;
import com.example.task_theavengers_android.adapter.HomeCategoryAdapter;
import com.example.task_theavengers_android.adapter.SubTaskAdapter;
import com.example.task_theavengers_android.data.CategoryDao;
import com.example.task_theavengers_android.data.SubTaskDAO;
import com.example.task_theavengers_android.data.TaskDao;
import com.example.task_theavengers_android.databinding.ActivityTaskDetailBinding;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.Image;
import com.example.task_theavengers_android.entity.SubTask;
import com.example.task_theavengers_android.entity.Task;
import com.example.task_theavengers_android.entity.TaskWithImages;
import com.example.task_theavengers_android.entity.TaskWithSubtask;
import com.example.task_theavengers_android.util.CategoryColor;
import com.example.task_theavengers_android.util.SubTaskClickListener;
import com.example.task_theavengers_android.util.TaskRoomDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {
    ActivityTaskDetailBinding binding;
    Task tasks, selectedTask;
    SubTask subTask;
    boolean isOldTask = false;
    SubTaskAdapter adapter;
    List<SubTask> subTaskList = new ArrayList<>();
    List<TaskWithImages> taskWithImagesList = new ArrayList<>();
    TaskWithImages taskWithImages;
    TaskRoomDatabase database;
    DetailsImagesAdapter imagesAdapter;
    SubTask selectedSubTask;
    SubTaskDAO subTaskDAO;
    Button markCompletedButton;
    TaskDao taskDao;
    ImageView imageBackBtn;
    RecyclerView imagesRecyclerView;
    Long taskId;
    ProgressBar progressBar;
    CategoryDao categoryDao;
    LinearLayout progressWrapper;
    ImageButton playButton;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imagesRecyclerView = findViewById(R.id.viewPagerMain);
        progressBar = findViewById(R.id.task_progress);
        progressWrapper = findViewById(R.id.progressWrapper);
        playButton = findViewById(R.id.playButton);
        markCompletedButton = findViewById(R.id.btn_mark_completed);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageBackBtn = findViewById(R.id.img_back);
        //initialize database
        database = database.getInstance(this);
        subTaskDAO = database.getInstance(this).subTaskDAO();
        taskDao = database.getInstance(this).taskDao();
        categoryDao = database.categoryDao();


        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
              taskId = Long.parseLong(String.valueOf(extras.get("id")));
            }
            tasks = taskDao.getAllTaskById(taskId);
            binding.txtTaskTitle.setText(tasks.getName());
            binding.txtCreatedDate.setText(String.valueOf(tasks.getCreateDate()));
            binding.txtDescription.setText(tasks.getDescription());
            //isOldTask = true;
            taskWithImagesList = taskDao.getAllTasksWithImages();
            taskWithImages = taskDao.getTaskWithImagesById(taskId);
            prepareImagesList();

            Log.e("TASK STATUS", ""+taskWithImages.task.isCompleted());
            if(taskWithImages.task.isCompleted()){
              Log.e("TASK STATUS", ""+taskWithImages.task.isCompleted());
              markCompletedButton.setText("Mark Not Completed");
            }

            // set the progress bar
            Category category = getCategoryByName(taskWithImages.task.getCategory());
            if(category != null){
              // Log.e("CATEGORY COLOR => ", ""+ CategoryColor.getColor(getApplicationContext(), category));
              int color = CategoryColor.getColor(getApplicationContext(), category);
              GradientDrawable shape =  new GradientDrawable();
              shape.setCornerRadius(20);
              shape.setColor(color);
              // set the progress bar color
              // Log.e("VALUE IS => ", ""+calculateProgress());
              updateProgressBar();
            }

          Log.e("TASK ID IS => ", ""+taskWithImages.task.getId());

        }catch (Exception e){
            e.printStackTrace();
        }

        //adding subtask
        binding.imgAddSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtSubTask.getText().toString().matches("")){
                    Toast.makeText(TaskDetailActivity.this, "Enter Sub Task field", Toast.LENGTH_SHORT).show();
                }else {
                    String subTaskName = binding.edtSubTask.getText().toString();
                    subTask = new SubTask(tasks.getId(), subTaskName);
                    subTask.setSubTaskTitle(subTaskName);
                    subTask.setId(tasks.getId());
                    subTaskDAO.insert(subTask);

                    subTaskList = subTaskDAO.loadTaskWithSubTask(tasks.getId());
                    updateRecycler();
                    binding.edtSubTask.setText("");
                    taskWithImages = taskDao.getTaskWithImagesById(taskId);
                    updateProgressBar();
                }
            }
        });

        //onback press
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetailActivity.this,HomePage.class);
                intent.putExtra("task",tasks);
                setResult(Activity.RESULT_OK,intent);
                startActivity(intent);
                finish();
                Log.e("TASK STATUS IN BACK => ", ""+taskWithImages.task.isCompleted());
            }
        });

        //deleting the whole task
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertForDeleteTask();

            }
        });
        updateRecycler();

        playButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            try {
              MediaPlayer mediaPlayer = new MediaPlayer();
              mediaPlayer.setDataSource(taskWithImages.task.getAudioPath());
              mediaPlayer.prepare();
              mediaPlayer.start();

            }catch (Exception e)
            {
              e.printStackTrace();
              Log.e("AUDIO ERROR => ", ""+e.toString());
            }
          }
        });


        markCompletedButton.setOnClickListener(new View.OnClickListener() {
          @SuppressLint("LongLogTag")
          @Override
          public void onClick(View v) {
            taskWithImages = taskDao.getTaskWithImagesById(taskId);
            int completed = 0;
            for(int i = 0; i < taskWithImages.subTaskList.size(); i++){
              if(taskWithImages.subTaskList.get(i).getSubTaskStatus()){
                completed++;
              }
            }
            Task taskMain = taskDao.getAllTaskById(taskId);
            if(!taskWithImages.task.isCompleted()){

              if(completed == taskWithImages.subTaskList.size()){
                Log.e("1 BUTTON CLICK TASK STATUS", ""+taskMain.isCompleted());
                Log.e("COMPLETED", ""+completed);
                Log.e("SIZE", ""+taskWithImages.subTaskList.size());
                Log.e("TASK ID", ""+taskWithImages.task.getId());
                // taskDao.updateTaskStatus(taskWithImages.task.getId(), true);
                //taskDao.updateTask(taskId, taskWithImages.task.getName(), taskWithImages.task.getDescription(), taskWithImages.task.getCreateDate(), taskWithImages.task.getDueDate(), taskWithImages.task.getCategory(), true);
                Task task = taskMain;
                task.setCompleted(true);
                taskDao.updateTask(task);
                markCompletedButton.setText("Mark Not Completed");
                finish();
              }
              else{
                Toast.makeText(getApplicationContext(), "You have incompleted sub tasks!", Toast.LENGTH_SHORT).show();
              }
              return;
            }
            else{
              Log.e(" 2BUTTON CLICK TASK STATUS", ""+taskWithImages.task.isCompleted());
              // taskDao.updateTaskStatus(taskId, false);
              // taskDao.updateTask(taskId, taskWithImages.task.getName(), taskWithImages.task.getDescription(), taskWithImages.task.getCreateDate(), taskWithImages.task.getDueDate(), taskWithImages.task.getCategory(), false);
              Task task = taskMain;
              task.setCompleted(false);
              taskDao.updateTask(task);
              markCompletedButton.setText("Mark Completed");
              finish();
              return;
            }
          }
        });
    }

    private void updateProgressBar(){
      Category category = getCategoryByName(taskWithImages.task.getCategory());
      int color = CategoryColor.getColor(getApplicationContext(), category);
      progressBar.setProgress(calculateProgress());
      progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
    }

    private int calculateProgress(){
      Double value = 0.0;
      if(taskWithImages != null){
        int count = taskWithImages.subTaskList.size();
        int completed = 0;
        for(int i = 0; i < count; i++){
          if(taskWithImages.subTaskList.get(i).getSubTaskStatus()){
            completed++;
          }
        }
        if(count > 0){
          value = (Double) (Double.valueOf(completed)/Double.valueOf(count)) * 100;
          Log.e("COUNTS", ""+count+"/"+completed+"="+value);
          if(completed > 0){

          }
          else{
            rearrangeProgress();
          }
        }
      }
      return Integer.valueOf(value.intValue());
    }

    private void rearrangeProgress(){
      // Log.e("REARRANGE", "rearrange");
//      final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
//      int view_height = (int) (0 * scale + 0.5f);
//      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
//      progressWrapper.setLayoutParams(params);

    }

    private Category getCategoryByName(String name){
      Category category = null;
      List<Category> categoryList = categoryDao.getAllCategories();
      for(int i = 0; i < categoryList.size(); i++){
        if(categoryList.get(i).getName().equals(name)){
          category = categoryList.get(i);
          break;
        }
      }
      return category;
    }

    private void prepareImagesList(){
      List<Image> images = taskDao.getAllImageByTaskId(taskId);
      Log.e("IMAGES SIZE => ", ""+taskWithImages.images.size());
      imagesAdapter = new DetailsImagesAdapter(this, taskWithImages.images);
      imagesRecyclerView.setAdapter(imagesAdapter);
    }

    // update sub task status
    public void updateSubTaskStatus(SubTask task){
      if(task != null){
        subTaskDAO.updateStatusChange(task.getSubTaskStatus(), task.getSubtaskId());
        // Log.e("STATUS", ""+task.getSubTaskStatus());
        Snackbar snackbar = Snackbar
          .make(binding.scrollview, "Sub Task status updated", Snackbar.LENGTH_LONG);
        // Log.e("TASK UPDATE", "Sub Task status updated");
        taskWithImages = taskDao.getTaskWithImagesById(taskId);
        updateProgressBar();
      }
    }

    //initializing recyclerview
    private void updateRecycler() {
        List<SubTask>subTaskList = database.subTaskDAO().loadTaskWithSubTask(tasks.getId());
        binding.viewSubtasks.setHasFixedSize(true);
        binding.viewSubtasks.setLayoutManager(new LinearLayoutManager(this));
        Category category = getCategoryByName(taskWithImages.task.getCategory());
        int color = CategoryColor.getColor(getApplicationContext(), category);
        adapter = new SubTaskAdapter(TaskDetailActivity.this, subTaskList,subTaskListener, color);
        binding.viewSubtasks.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
    }

    //subtask completed
    private final SubTaskClickListener subTaskListener = new SubTaskClickListener() {
        @Override
        public void onClick(SubTask subTask) {
            Snackbar snackbar = Snackbar
                    .make(binding.scrollview, "subtask completed", Snackbar.LENGTH_LONG);
            snackbar.show();
            selectedSubTask = subTask;
            database.subTaskDAO().delete(selectedSubTask);
            subTaskList.remove(selectedSubTask);
            updateRecycler();

        }

        //deleting the subtasks
        @Override
        public void onClickDelete(SubTask subTask, ImageView imageView) {

         //  alertForDeleteSubTask(subTask);

        }
    };


    private void alertForDeleteTask(){
        builder = new AlertDialog.Builder(TaskDetailActivity.this);
        builder.setMessage("Do you want ro delete the task?")
                .setCancelable(false)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        selectedTask = tasks;
                        database.taskDao().deleteTask(selectedTask.getId());
                        Intent intent = new Intent(getApplicationContext(),HomePage.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("DELETE TASK");
        alert.show();
    }
}
