package com.example.task_theavengers_android.data;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.task_theavengers_android.entity.SubTask;
import com.example.task_theavengers_android.entity.TaskWithSubtask;

import java.util.List;

@Dao
public interface SubTaskDAO {

    @Insert(onConflict = REPLACE)
    void insert(SubTask subTask);

    @Query("SELECT * FROM SubTask  ORDER BY subtaskId DESC")
    List<SubTask> getAllSubTasks();

    @Query("UPDATE SubTask SET subTaskStatus = :status WHERE subtaskId = :id")
    void updateStatusChange(Boolean status, Long id);


    // @Query("SELECT * FROM subtask WHERE subtaskId = subtaskId")
    //List<SubTask> findSubTaskForTask(final int subtaskId);

    @Query("UPDATE SubTask SET subTaskTitle = :subTaskTitle")
    void update(String subTaskTitle);

    @Delete
    void delete(SubTask subTask);

    @Query("SELECT * FROM SubTask  WHERE id = :id ORDER BY subtaskId DESC")
    public List<SubTask> loadTaskWithSubTask(long id);
//
//    @Query("SELECT * FROM SubTask WHERE id = :id ORDER BY subtaskId DESC")
//    List<SubTask> getAllTaskWithSubtask(long id);



}
