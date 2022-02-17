package com.example.task_theavengers_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task_theavengers_android.adapter.SubTaskAdapter;
import com.example.task_theavengers_android.data.SubTaskDAO;
import com.example.task_theavengers_android.data.TaskDao;
import com.example.task_theavengers_android.databinding.ActivityTaskDetailBinding;
import com.example.task_theavengers_android.entity.Image;
import com.example.task_theavengers_android.entity.SubTask;
import com.example.task_theavengers_android.entity.Task;
import com.example.task_theavengers_android.entity.TaskWithImages;
import com.example.task_theavengers_android.entity.TaskWithSubtask;
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
    SubTask selectedSubTask;
    SubTaskDAO subTaskDAO;
    TaskDao taskDao;
    ImageView imageBackBtn;
    Long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageBackBtn = findViewById(R.id.img_back);
        //initialize database
        database = database.getInstance(this);
        subTaskDAO = database.getInstance(this).subTaskDAO();
        taskDao = database.getInstance(this).taskDao();

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
            Log.e("IMAGES => ", ""+taskWithImages.images.size());
            for(int i = 0; i < taskWithImages.images.size(); i++){
              Log.e("IMAGE PATH => ", ""+taskWithImages.images.get(i).getPath());

//              File imgFile = new  File(taskWithImages.images.get(i).getPath());
//              if(imgFile.exists())
//              {
//                Log.e("IMAGE PATH => ", "FILE EXISTS");
//                ImageView myImage = new ImageView(this);
//                imageBackBtn.setImageURI(Uri.fromFile(imgFile));
//              }
            }
          //  subTaskList = subTaskDAO.getAllTaskWithSubtask(tasks.getTaskId());
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

            }
        });

        //deleting the whole task
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(TaskDetailActivity.this)
                        .setTitle("DELETE TASK")
                        .setMessage("The Task will be deleted.")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedTask = tasks;
                                database.taskDao().deleteTask(selectedTask.getId());
//                                taskWithSubtaskList.remove(selectedTask);
                                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });
        updateRecycler();
    }

    //initializing recyclerview
    private void updateRecycler() {
        List<SubTask>subTaskList = database.subTaskDAO().loadTaskWithSubTask(tasks.getId());
        binding.viewSubtasks.setHasFixedSize(true);
        binding.viewSubtasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubTaskAdapter(TaskDetailActivity.this, subTaskList,subTaskListener);
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
//            database.subTaskDAO().delete(selectedSubTask);
//            subTaskList.remove(selectedSubTask);
//            updateRecycler(taskWithSubtaskList);

        }

        //deleting the subtasks
        @Override
        public void onClickDelete(SubTask subTask, ImageView imageView) {

            new MaterialAlertDialogBuilder(TaskDetailActivity.this)
                    .setTitle("DELETE SUBTASK")
                    .setMessage("The subtask will be deleted.")
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedSubTask = subTask;
//                            database.subTaskDAO().delete(selectedSubTask);
//                            subTaskList.remove(selectedSubTask);
//                            updateRecycler(taskWithSubtaskList);

                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

        }
    };
}
