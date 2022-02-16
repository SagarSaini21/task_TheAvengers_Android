package com.example.task_theavengers_android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.task_theavengers_android.entity.Category;
import java.util.List;

/**
 * Author: Vergel dela Cruz
 * Date: Feb. 11, 2022
 * Description: Dao Interface class for Category. Use this class
 * to perform CRUD operations on the category table.
 */
@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Category category);

    @Query("DELETE FROM category")
    void deleteAllCategories();

    @Query("DELETE FROM category where id = :id")
    void deleteCategory(long id);

    @Query("UPDATE category SET name = :name WHERE id = :id")
    int updateCategory(long id, String name);

    @Query("SELECT * FROM category ORDER BY id")
    List<Category> getAllCategories();

    @Query("SELECT * FROM category where name = :name  ORDER BY id")
    Category getCategoryMatchingName(String name);
}
