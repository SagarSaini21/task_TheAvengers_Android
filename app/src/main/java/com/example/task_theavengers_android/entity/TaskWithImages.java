package com.example.task_theavengers_android.entity;

import androidx.room.Embedded;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;
/**
 * Author: Vergel dela Cruz
 * Date: Feb. 16, 2022
 * Description: Data class to hold Task with Images (One to Many relationship) in Room DB.
 */
public class TaskWithImages implements Serializable {
    @Embedded
    public Task task;
    @Relation(
            parentColumn = "id",
            entityColumn = "image_task_id"
    )

    public List<Image> images;

    @Relation(parentColumn = "id", entityColumn = "id", entity = SubTask.class)
    public List<SubTask> subTaskList;

    public TaskWithImages(Task task, List<Image> images, List<SubTask> subTaskList) {
        this.task = task;
        this.images = images;
        this.subTaskList = subTaskList;
    }

  public Task getTask() {
    return task;
  }

  public List<Image> getImages() {
    return images;
  }

  public List<SubTask> getSubTaskList() {
    return subTaskList;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public void setImages(List<Image> images) {
    this.images = images;
  }

  public void setSubTaskList(List<SubTask> subTaskList) {
    this.subTaskList = subTaskList;
  }
}
