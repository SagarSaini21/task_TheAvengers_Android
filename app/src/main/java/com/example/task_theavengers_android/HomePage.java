package com.example.task_theavengers_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.task_theavengers_android.adapter.HomeCategoryAdapter;
import com.example.task_theavengers_android.adapter.TaskAdaptor;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.TaskWithImages;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import java.util.List;

public class HomePage extends AppCompatActivity {

  private TaskRoomDatabase taskRoomDatabase;
  private List<Category> categoryList;
  private List<TaskWithImages> tasksList;

  private HomeCategoryAdapter categoryAdapter;
  private TaskAdaptor taskAdaptor;
  private RecyclerView recyclerView, taskRecyclerView;
  private ImageView toCategory, toAddNewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // get the room database instance
        taskRoomDatabase = TaskRoomDatabase.getInstance(this);
        // connect the elements
        toAddNewTask = findViewById(R.id.toAddNewTask);
        recyclerView = findViewById(R.id.recyclerView);
        taskRecyclerView = findViewById(R.id.recyclerTasksListView);
        toCategory = findViewById(R.id.toCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        prepareCategoryList();
        prepareTasks();
        // trigger methods
        goToCategories(); // category button trigger
        goToAddNewTask(); // to new task button trigger
    }

    // method prepare tasks
    private void prepareTasks(){
      // get the tasks
      tasksList = taskRoomDatabase.taskDao().getAllTasksWithImages();
      taskAdaptor = new TaskAdaptor(this, tasksList);
      taskRecyclerView.setAdapter(taskAdaptor);
    }

 /* private final TaskClickListener taskClickListener = new TaskClickListener() {
    @Override
    public void onClick(TaskWithImages taskWithImages) {
      Intent intent = new Intent(HomePage.this,TaskDetailActivity.class);
      //pass the particular item
      intent.putExtra("old_task", (Parcelable) taskWithImages.task);
      startActivity(intent);
    }

  };*/

    // method prepare category list
    private void prepareCategoryList(){
      // get the categories
      Category allCategory = new Category("All");
      allCategory.setId(0);
      categoryList = taskRoomDatabase.categoryDao().getAllCategories();
      categoryList.add(0, allCategory);
      categoryAdapter = new HomeCategoryAdapter(this, categoryList);
      recyclerView.setAdapter(categoryAdapter);
    }

    public void getCategoryClick(Category category){
      Log.e("CATEGORY NAME => ", ""+category.getName());
      categoryAdapter.notifyDataSetChanged();
    }

    // method to go to categories page
    private void goToCategories(){
      toCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(HomePage.this, CategoryActivity.class);
          startActivityForResult(intent, 101);
        }
      });
    }

    // method to go to categories page
    private void goToAddNewTask(){
      toAddNewTask.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(HomePage.this, CreateTaskActivity.class);
          startActivityForResult(intent, 102);
        }
      });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

      // categories intent result
      if(requestCode == 101 && resultCode == RESULT_CANCELED){
        prepareCategoryList();
      }

      // to add new task intent result
      if(requestCode == 102 && resultCode == RESULT_CANCELED){
        prepareTasks();
      }

      super.onActivityResult(requestCode, resultCode, data);
    }
}
