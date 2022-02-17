package com.example.task_theavengers_android.entity;


import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Vergel dela Cruz
 * Date: Feb. 16, 2022
 * Description: Entity class to store task data into Room Database.
 */
@Entity(tableName = "task")
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    /*
    @ForeignKey
            (entity = Image.class,
                    parentColumns = "id",
                    childColumns = "id_fkimage",
                    onDelete = CASCADE
            )
    private int id_fkimage;

    public int getId_fkimage() {
        return id_fkimage;
    }

    public void setId_fkimage(int id_fkimage) {
        this.id_fkimage = id_fkimage;
    }
*/
    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Date createDate;

    @NonNull
    private Date dueDate;

    @NonNull
    private String category;

    @NonNull
    private boolean completed;

    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private String audioPath;


    public Task(@NonNull String name, @NonNull String description, @NonNull Date createDate, @NonNull Date dueDate, @NonNull String category,@NonNull boolean completed , String audioPath) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.category = category;
        this.completed = completed;
        this.audioPath = audioPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(@NonNull Date createDate) {
        this.createDate = createDate;
    }

    @NonNull
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(@NonNull Date dueDate) {
        this.dueDate = dueDate;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
