package com.example.task_theavengers_android.util;

import android.widget.ImageView;

import com.example.task_theavengers_android.entity.SubTask;


public interface SubTaskClickListener {
    void onClick(SubTask subTask);
    void onClickDelete(SubTask subTask, ImageView imageView);
}
