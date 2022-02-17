package com.example.task_theavengers_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.TaskDetailActivity;
import com.example.task_theavengers_android.data.SubTaskDAO;
import com.example.task_theavengers_android.entity.SubTask;
import com.example.task_theavengers_android.entity.TaskWithImages;
import com.example.task_theavengers_android.entity.TaskWithSubtask;
import com.example.task_theavengers_android.util.CategoryColor;
import com.example.task_theavengers_android.util.SubTaskClickListener;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import java.util.List;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskViewHolder>{
    Context context;
    List<SubTask> subTaskList;
    SubTaskClickListener listener;
    int color;
    TaskRoomDatabase database;
    SubTaskDAO subTaskDAO;

    public SubTaskAdapter(Context context, List<SubTask> subTaskList, SubTaskClickListener listener, int color) {
        this.context = context;
        this.subTaskList = subTaskList;
        this.listener = listener;
        this.color = color;
        database = TaskRoomDatabase.getInstance(context);
        subTaskDAO = database.subTaskDAO();
    }

    @NonNull
    @Override
    public SubTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_task_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try{
            if (subTaskList!=null) {
                holder.txtSubTask.setText(subTaskList.get(position).getSubTaskTitle());
                holder.color.setColorFilter(color);
                Log.e("STTAUAS", ""+subTaskList.get(position).getSubTaskStatus());
                if(!subTaskList.get(position).getSubTaskStatus()){
                  holder.statusChange.setImageResource(R.drawable.ic_check_box_not_done);
                }
                else{
                  holder.statusChange.setImageResource(R.drawable.ic_baseline_check_box_24);
                }

                holder.statusChange.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Boolean status = !subTaskList.get(position).getSubTaskStatus();
                    Log.e("STATUS", ""+status);
                    if(!status){
                      holder.statusChange.setImageResource(R.drawable.ic_check_box_not_done);
                    }
                    else{
                      holder.statusChange.setImageResource(R.drawable.ic_baseline_check_box_24);
                    }
                    subTaskList.get(position).setSubTaskStatus(status);
                    TaskDetailActivity taskDetailActivity = (TaskDetailActivity) context;
                    taskDetailActivity.updateSubTaskStatus(subTaskList.get(position));
                  }
                });

                holder.txtSubTask.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                  }
                });
            }
        }catch (Exception ignored){

        }

    }

    @Override
    public int getItemCount() {
        return subTaskList.size();
    }
}

class SubTaskViewHolder extends RecyclerView.ViewHolder {
    TextView txtSubTask;
    ImageView statusChange, color;
    RelativeLayout relativeLayout;

    public SubTaskViewHolder(@NonNull View itemView) {
        super(itemView);
        txtSubTask = itemView.findViewById(R.id.subTaskName);
        statusChange = itemView.findViewById(R.id.itemStatus);
        color = itemView.findViewById(R.id.itemColor);
    }
}
