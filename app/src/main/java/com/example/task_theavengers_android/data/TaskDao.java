package com.example.task_theavengers_android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.Image;
import com.example.task_theavengers_android.entity.SubTask;
import com.example.task_theavengers_android.entity.Task;
import com.example.task_theavengers_android.entity.TaskWithImages;

import java.util.Date;
import java.util.List;

/**
 * Author: Vergel dela Cruz
 * Date: Feb. 16, 2022
 * Description: Dao Interface class for Task. Use this class
 * to perform CRUD operations on the task table.
 */
@Dao
public interface TaskDao {
    @Transaction
    @Insert
    long insertTask(Task task);

    @Insert
    void insertImages(List<Image> images);

    @Insert
    void insertSubTasks(List<SubTask> subTasks);

    @Query("DELETE FROM task")
    void deleteAllTasks();

    @Query("DELETE FROM task where id = :id")
    void deleteTask(long id);

    @Query("UPDATE task SET name = :name, description = :description , createDate = :createDate, dueDate = :dueDate, category = :category, completed = :completed WHERE id= :id")
    int updateTask(int id, String name, String description, Date createDate, Date dueDate, String category, boolean completed);

    @Query("SELECT * FROM task ORDER BY id")
    List<Task> getAllTasks();

    @Query("SELECT * FROM image where id = :id")
    List<Image> getAllImageByTaskId(Long id);

    @Query("SELECT * FROM task where id = :id")
    Task getAllTaskById(Long id);

    @Query("SELECT * FROM task ORDER BY id")
    List<TaskWithImages> getAllTasksWithImages();

    @Query("SELECT * FROM task where id = :id")
    TaskWithImages getTaskWithImagesById(Long id);

    @Query("SELECT * FROM task WHERE name LIKE '%' || :search || '%' or description LIKE '%' || :search || '%' ORDER BY name")
    List<Task> getAllMatchingTasksOrderByTitle(String search);

    @Query("SELECT * FROM task WHERE name LIKE '%' || :search || '%' or description LIKE '%' || :search || '%' ORDER BY createDate")
    List<Task> getAllMatchingTasksOrderByCreateDate(String search);

    @Transaction
    @Query("SELECT * FROM task WHERE name LIKE '%' || :search || '%' or description LIKE '%' || :search || '%' ORDER BY name")
    List<TaskWithImages> getAllMatchingTasksWithImagesOrderByTitle(String search);


    @Transaction
    @Query("SELECT * FROM task WHERE name LIKE '%' || :search || '%' or description LIKE '%' || :search || '%' ORDER BY createDate")
    List<TaskWithImages> getAllMatchingTasksWithImagesOrderByCreateDate(String search);

    @Query("SELECT * FROM task where category = :name")
    List<TaskWithImages> getTaskWithImagesByCategoryId(String name);
}
