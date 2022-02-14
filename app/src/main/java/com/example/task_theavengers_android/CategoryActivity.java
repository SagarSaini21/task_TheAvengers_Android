package com.example.task_theavengers_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.task_theavengers_android.adapter.CategoryAdapter;
import com.example.task_theavengers_android.databinding.ActivityCategoryBinding;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.util.TaskRoomDatabase;
import java.util.List;

/**
 * Activity for Categories.
 * It allows users to view list of categories in the room DB.
 * It also allows user to add, edit and delete categories.
 */
public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
        //,CategoryAdapter.ItemClickListener {
    private TaskRoomDatabase taskRoomDatabase;
    private ActivityCategoryBinding binding;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerView;

    /**
     * Initiliazes the Activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get Singleton instance of Task Room DB
        taskRoomDatabase = TaskRoomDatabase.getInstance(this);
        // Bind Add button click listener to this class
        binding.btnAddNew.setOnClickListener(this);
        // Get initial list of categories
        categoryList = taskRoomDatabase.categoryDao().getAllCategories();
        // Setup RecyclerView
        recyclerView = binding.viewSubtasks;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(this, categoryList);
        //categoryAdapter.setClickListener(this);
        recyclerView.setAdapter(categoryAdapter);
        binding.imgBack.setOnClickListener(this);
    }

    /**
     * Calls the addCategory function when the
     * Add button is click.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                // Intent i = new Intent(this,MainActivity.class);
                // startActivity(i);
                finish();
                break;
            case R.id.btn_add_new:
                addCategory();
                break;
        }
        addCategory();
    }

    /**
     * Checks that the category name entered by the user
     * is not empty else
     * inserts a category into the category table.
     * If empty, dialog appears to inform user to enter
     * a category name.
     */
    private void addCategory() {
        String categoryName = binding.edtCategory.getText().toString();
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter category name.", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.edtCategory.setText("");
        Category category = new Category(categoryName);
        taskRoomDatabase.categoryDao().insertCategory(category);
        categoryList = taskRoomDatabase.categoryDao().getAllCategories();
        categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerView.setAdapter(categoryAdapter);
    }
    /*
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + categoryAdapter.getItem(position).getName() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
     */
}
