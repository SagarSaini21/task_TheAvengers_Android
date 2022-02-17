package com.example.task_theavengers_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.HomePage;
import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.TaskWithImages;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import java.util.List;

/**
 * Adapter to display the list of task in a list.
 */
public class TaskAdaptor extends RecyclerView.Adapter<TaskAdaptor.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<TaskWithImages> taskList;
    private TaskRoomDatabase taskRoomDatabase;
    private Context  context;

    public TaskAdaptor(Context context, List<TaskWithImages> taskList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.taskList = taskList;
        this.context = context;
        taskRoomDatabase = TaskRoomDatabase.getInstance(context);
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
        holder.date.setText(task.task.getCreateDate().toString());
        holder.due.setText(task.task.getDueDate().toString());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, date, due;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.taskTitle);
            desc = itemView.findViewById(R.id.taskDesc);
            date = itemView.findViewById(R.id.dateText);
            due = itemView.findViewById(R.id.dueText);
        }

      @Override
      public void onClick(View v) {

      }
    }
}
