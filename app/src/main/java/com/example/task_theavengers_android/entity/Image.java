package com.example.task_theavengers_android.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Author: Vergel dela Cruz
 * Date: Feb. 16, 2022
 * Description: Entity class to store image data into Room Database.
 */
@Entity(tableName = "image")
public class Image {

    @PrimaryKey(autoGenerate = true)
    private long id;
    public long image_task_id;
    @NonNull
    private String path;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getImage_task_id() {
        return image_task_id;
    }

    public void setImage_task_id(long image_task_id) {
        this.image_task_id = image_task_id;
    }

    @NonNull
    public String getPath() {
        return path;
    }

    public void setPath(@NonNull String path) {
        this.path = path;
    }
}
