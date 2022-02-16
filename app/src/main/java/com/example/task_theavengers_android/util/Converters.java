package com.example.task_theavengers_android.util;

import androidx.room.TypeConverter;
import java.util.Date;
/**
 * Author: Vergel dela Cruz
 * Date: Feb. 16, 2022
 * Description: Utility class to store Date fields in RoomDB.
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
