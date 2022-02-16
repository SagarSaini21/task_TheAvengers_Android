package com.example.task_theavengers_android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.Task;

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
    @Insert
    void insertTask(Task task);

    @Query("DELETE FROM task")
    void deleteAllTasks();

    @Query("DELETE FROM task where id = :id")
    void deleteTask(int id);

    @Query("UPDATE task SET name = :name, description = :description , createDate = :createDate, dueDate = :dueDate, category = :category, completed = :completed WHERE id = :id")
    int updateTask(int id, String name, String description, Date createDate, Date dueDate, String category, boolean completed);

    @Query("SELECT * FROM task ORDER BY id")
    List<Task> getAllTasks();

    @Query("SELECT * FROM task WHERE name LIKE '%' || :search || '%' or description LIKE '%' || :search || '%' ORDER BY name")
    List<Task> getAllMatchingTasksOrderByTitle(String search);

    @Query("SELECT * FROM task WHERE name LIKE '%' || :search || '%' or description LIKE '%' || :search || '%' ORDER BY createDate")
    List<Task> getAllMatchingTasksOrderByCreateDate(String search);

}
