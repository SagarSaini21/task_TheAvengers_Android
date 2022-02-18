package com.example.task_theavengers_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.HomePage;
import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.TaskDetailActivity;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.Task;
import com.example.task_theavengers_android.entity.TaskWithImages;
import com.example.task_theavengers_android.util.CategoryColor;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Adapter to display the list of task in a list.
 */
public class TaskAdaptor extends RecyclerView.Adapter<TaskAdaptor.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<TaskWithImages> taskList;
    private List<Category> categoryList;
    private TaskRoomDatabase taskRoomDatabase;
    private List<TaskWithImages> taskWithImages;
    private Context  context;
    TaskWithImages task;

    public TaskAdaptor(Context context, List<TaskWithImages> taskList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.taskList = taskList;
        this.context = context;
        //this.listener = listener;
        taskRoomDatabase = TaskRoomDatabase.getInstance(context);
        categoryList = taskRoomDatabase.categoryDao().getAllCategories();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.task_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdaptor.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        task = taskList.get(position);
        holder.title.setText(task.task.getName());
        holder.desc.setText(task.task.getDescription());
        DateTimeFormatter timeFormatter = DateTimeFormat.longDate();
        String createdDate = timeFormatter.print(new DateTime(task.task.getCreateDate().getTime()));
        holder.date.setText(createdDate);
        DateTime date = new DateTime();
        DateTime endInstant = new DateTime(task.task.getDueDate().getTime());
        holder.due.setText(Days.daysBetween(date.toLocalDate(), endInstant.toLocalDate()).getDays() + " days left");
        GradientDrawable status_shape =  new GradientDrawable();
        status_shape.setCornerRadius(20);
        holder.completedStatus.setText("INCOMPLETE");
        status_shape.setColor(context.getResources().getColor(R.color.progress_bg));
        holder.completedStatus.setTextColor(Color.GRAY);
        Task task_main = taskRoomDatabase.taskDao().getAllTaskById(task.task.getId());
        Log.e("HOME PAGE - TASK DETAILS *** => ", ""+task_main.isCompleted()+"="+task_main.getId()+"="+task_main.getName());
        Log.e("HOME PAGE - TASK COMPLETED => ", ""+task.task.isCompleted());
        if(task.task.isCompleted()){
          holder.completedStatus.setText("COMPLETED");
          status_shape.setColor(context.getResources().getColor(R.color.light_green));
          holder.completedStatus.setTextColor(Color.WHITE);
        }
        holder.completedStatus.setBackground(status_shape);
        // get category to get the color
        Category category = getCategoryByName(task.task.getCategory());
        if(category != null){
          Log.e("CATEGORY COLOR => ", ""+CategoryColor.getColor(context, category));
          int color = CategoryColor.getColor(context, category);
          GradientDrawable shape =  new GradientDrawable();
          shape.setCornerRadius(20);
          shape.setColor(color);
          holder.colorBtn.setBackground(shape);
          // set the progress bar color
          int progress_value = calculateProgress(taskRoomDatabase.taskDao().getTaskWithImagesById(task.task.getId()));
          holder.progressBar.setProgress(progress_value);
          holder.progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
        }

        // click event
        holder.taskContainer.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(context, TaskDetailActivity.class);
            //pass the particular item
            intent.putExtra("id", taskList.get(position).task.getId());
            // Log.e("TASK IN ADAPTOR => ", ""+taskList.get(position).task.getName()+"-"+taskList.get(position).task.getId());
            (context).startActivity(intent);
          }
        });
    }
    public void filterList(List<TaskWithImages> filteredNotesList){
        taskList = filteredNotesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, date, due;
        Button colorBtn, completedStatus;
        ProgressBar progressBar;
        LinearLayout taskContainer;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.taskTitle);
            desc = itemView.findViewById(R.id.taskDesc);
            date = itemView.findViewById(R.id.dateText);
            due = itemView.findViewById(R.id.dueText);
            colorBtn = itemView.findViewById(R.id.categoryColor);
            progressBar = itemView.findViewById(R.id.subTasksProgressBar);
            taskContainer = itemView.findViewById(R.id.titleWrapper);
            completedStatus = itemView.findViewById(R.id.taskCompletedStatus);
        }

      @Override
      public void onClick(View v) {

      }
    }

    private Category getCategoryByName(String name){
      Category category = null;
      for(int i = 0; i < categoryList.size(); i++){
        if(categoryList.get(i).getName().equals(name)){
          category = categoryList.get(i);
          break;
        }
      }
      return category;
    }

//  private void updateProgressBar(){
//    Category category = getCategoryByName(taskWithImages.task.getCategory());
//    int color = CategoryColor.getColor(getApplicationContext(), category);
//    progressBar.setProgress(calculateProgress());
//    progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
//  }

  private int calculateProgress(TaskWithImages task){
    Double value = 0.0;
    if(task != null){
      int count = task.subTaskList.size();
      int completed = 0;
      for(int i = 0; i < count; i++){
        if(task.subTaskList.get(i).getSubTaskStatus()){
          completed++;
        }
      }
      if(count > 0){
        value = (Double) (Double.valueOf(completed)/Double.valueOf(count)) * 100;
        Log.e("COUNTS", ""+count+"/"+completed+"="+value);
        if(completed > 0){

        }
      }
    }
    return Integer.valueOf(value.intValue());
  }
}
