package com.example.task_theavengers_android.util;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.entity.Category;

/**
 * Author:Vergel dela Cruz
 * A utility class that returns a distinct color for each category.
 * There is a limit of 10 colors.
 */
public class CategoryColor {
    private static int colors[] = {
            R.color.Red,
            R.color.Maroon,
            R.color.Yellow,
            R.color.Olive,
            R.color.Lime,
            R.color.Green,
            R.color.Aqua,
            R.color.Teal,
            R.color.Blue,
            R.color.Purple
    };

    /**
     * Returns a distinct color per category
     * @param context
     * @param category
     * @return
     */
    public static int getColor(Context context, Category category) {
        int result = 0;
        long categoryId = category.getId();
        result = ContextCompat.getColor(context, colors[(int) (categoryId % 10)]);
        return result;
    }
}
