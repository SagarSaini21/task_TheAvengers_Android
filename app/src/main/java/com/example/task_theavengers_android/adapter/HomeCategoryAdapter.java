package com.example.task_theavengers_android.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.HomePage;
import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.util.CategoryColor;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import java.util.List;

/**
 * Adapter to display the list of categories in a list.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Category> categoryList;
    private TaskRoomDatabase taskRoomDatabase;
    private Context  context;
    int lastCategoryClickedIndex = 0;

    /**
     * Constructor for the category Adapter.
     * The contet, layout inflator , categorylist is initialized here.
     * @param context
     * @param categoryList
     */
    public HomeCategoryAdapter(Context context, List<Category> categoryList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.categoryList = categoryList;
        this.context = context;
        taskRoomDatabase = TaskRoomDatabase.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HomeCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = categoryList.get(position);
        holder.categoryBtn.setText(category.getName());
        if(lastCategoryClickedIndex == position){
          holder.categoryBtn.setBackground(context.getDrawable(R.drawable.primary_button));
          holder.categoryBtn.setTextColor(Color.WHITE);
        }
        else{
          holder.categoryBtn.setBackground(context.getDrawable(R.drawable.white_round_border));
          holder.categoryBtn.setTextColor(Color.GRAY);
        }
        holder.categoryBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            lastCategoryClickedIndex = position;
            HomePage homePage = (HomePage) context;
            homePage.getCategoryClick(category);
          }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button categoryBtn;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            categoryBtn = itemView.findViewById(R.id.homeCategoryItemBtn);
        }

      @Override
      public void onClick(View v) {

      }
    }
}
