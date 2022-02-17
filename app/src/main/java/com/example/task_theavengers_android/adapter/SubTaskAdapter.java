package com.example.task_theavengers_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.entity.TaskWithSubtask;
import com.example.task_theavengers_android.util.SubTaskClickListener;

import java.util.List;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskViewHolder>{
    Context context;
    List<TaskWithSubtask> subTaskList;
    SubTaskClickListener listener;

    public SubTaskAdapter(Context context, List<TaskWithSubtask> subTaskList, SubTaskClickListener listener) {
        this.context = context;
        this.subTaskList = subTaskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_subtasks_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskViewHolder holder, int position) {

        try{
            if (subTaskList!=null) {
                holder.txtSubTask.setText(subTaskList.get(position).subTaskList.get(position).getSubTaskTitle());


                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(subTaskList.get(position).subTaskList.get(holder.getAdapterPosition()));

                    }
                });
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClickDelete(subTaskList.get(position).subTaskList.get(holder.getAdapterPosition()),holder.imgDelete);
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
    ImageView imgDelete;
    RelativeLayout relativeLayout;

    public SubTaskViewHolder(@NonNull View itemView) {
        super(itemView);

        txtSubTask = itemView.findViewById(R.id.txt_sub_task);
        imgDelete = itemView.findViewById(R.id.img_delete);
        relativeLayout = itemView.findViewById(R.id.relative_layout);
    }
}
