package com.example.task_theavengers_android.util;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.task_theavengers_android.data.CategoryDao;
import com.example.task_theavengers_android.entity.Category;


/**
 * Author: Vergel dela Cruz
 * Date: Feb. 11, 2022
 * Description: Singleton class for Task Database using Room DB.
 */
@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    private static final String DB_NAME = "task_room_db";
    private static volatile TaskRoomDatabase INSTANCE;

    public static TaskRoomDatabase getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskRoomDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        return INSTANCE;
    }
}