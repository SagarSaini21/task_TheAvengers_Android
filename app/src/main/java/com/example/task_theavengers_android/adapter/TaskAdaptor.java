package com.example.task_theavengers_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.HomePage;
import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.TaskWithImages;
import com.example.task_theavengers_android.util.CategoryColor;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import org.joda.time.Chronology;
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
    private Context  context;

    public TaskAdaptor(Context context, List<TaskWithImages> taskList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.taskList = taskList;
        this.context = context;
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
        TaskWithImages task = taskList.get(position);
        holder.title.setText(task.task.getName());
        holder.desc.setText(task.task.getDescription());
        DateTimeFormatter timeFormatter = DateTimeFormat.longDate();
        String createdDate = timeFormatter.print(new DateTime(task.task.getCreateDate().getTime()));
        holder.date.setText(createdDate);
        DateTime date = new DateTime();
        DateTime endInstant = new DateTime(task.task.getDueDate().getTime());
        holder.due.setText(Days.daysBetween(date.toLocalDate(), endInstant.toLocalDate()).getDays() + " days left");
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
          holder.progressBar.setProgress(25);
          holder.progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, date, due;
        Button colorBtn;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.taskTitle);
            desc = itemView.findViewById(R.id.taskDesc);
            date = itemView.findViewById(R.id.dateText);
            due = itemView.findViewById(R.id.dueText);
            colorBtn = itemView.findViewById(R.id.categoryColor);
            progressBar = itemView.findViewById(R.id.subTasksProgressBar);
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
}
