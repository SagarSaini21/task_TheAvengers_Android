package com.example.task_theavengers_android.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "SubTask")
public class SubTask implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long subtaskId = 0;  //subtask id

    @ColumnInfo(name = "id")
    private long id = 0;

    @ColumnInfo(name = "subTaskTitle")
    String subTaskTitle;

    public SubTask(int subtaskId, int id, String subTaskTitle) {
        this.subtaskId = subtaskId;
        this.id = id;
        this.subTaskTitle = subTaskTitle;
    }

    public SubTask() {
    }

    public long getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(long subtaskId) {
        this.subtaskId = subtaskId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubTaskTitle() {
        return subTaskTitle;
    }

    public void setSubTaskTitle(String subTaskTitle) {
        this.subTaskTitle = subTaskTitle;
    }
}
