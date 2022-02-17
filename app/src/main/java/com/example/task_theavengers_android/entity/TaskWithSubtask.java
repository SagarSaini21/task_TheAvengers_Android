package com.example.task_theavengers_android.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class TaskWithSubtask  implements Serializable {
    @Embedded
    public TaskWithImages tasks;

    @Relation(parentColumn = "id",
    entityColumn = "id",
            entity = SubTask.class)
    public List<SubTask> subTaskList;

    public TaskWithSubtask(TaskWithImages tasks, List<SubTask> subTaskList) {
        this.tasks = tasks;
        this.subTaskList = subTaskList;
    }
}
