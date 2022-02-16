package com.example.task_theavengers_android.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
/**
 * Author: Vergel dela Cruz
 * Date: Feb. 16, 2022
 * Description: Data class to hold Task with Images (One to Many relationship) in Room DB.
 */
public class TaskWithImages {
    @Embedded
    public Task task;
    @Relation(
            parentColumn = "id",
            entityColumn = "image_task_id"
    )
    public List<Image> images;

    public TaskWithImages(Task task, List<Image> images) {
        this.task = task;
        this.images = images;
    }
}
