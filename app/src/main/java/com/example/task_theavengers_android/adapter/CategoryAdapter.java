package com.example.task_theavengers_android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.util.CategoryColor;
import com.example.task_theavengers_android.util.TaskRoomDatabase;
import java.util.List;

/**
 * Adapter to display the list of categories in a list.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Category> categoryList;
    private TaskRoomDatabase taskRoomDatabase;
    //private ItemClickListener mClickListener;
    private Context  context;

    /**
     * Constructor for the category Adapter.
     * The contet, layout inflator , categorylist is initialized here.
     * @param context
     * @param categoryList
     */
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.categoryList = categoryList;
        this.context = context;
        taskRoomDatabase = TaskRoomDatabase.getInstance(context);
    }
    
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_category_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtCategory.setText(category.getName());
        holder.img_color_change.setColorFilter(CategoryColor.getColor(context, category));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    //public void setClickListener(CategoryActivity categoryActivity) { }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtCategory;
        ImageView ivDelete;
        ImageView img_color_change;
        /**
         * Binds the text, image view variables and click listener.
         * @param itemView
         */
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtCategory = itemView.findViewById(R.id.txt_category);

            // Handler for deleting categories
            ivDelete = itemView.findViewById(R.id.img_delete);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCategory();
                }
            });
            img_color_change = itemView.findViewById(R.id.img_color_change);

/*
            img_color_change = itemView.findViewById(R.id.img_color_change);
            img_color_change.setColorFilter(CategoryColor.getColor(context, null));
*/
        }

        /**
         * Displays a dialog to confirm deletion.
         * If user selects Yes, the category record is deleted from the table.
         * If user selects No, a message showing category is not deleted is displayed.
         */
        private void deleteCategory() {
            AlertDialog.Builder buider = new AlertDialog.Builder(context);
            buider.setTitle("Are you sure ?");
            buider.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    taskRoomDatabase.categoryDao().deleteCategory( categoryList.get(getAdapterPosition()).getId());
                    categoryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
            buider.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(context,"The category is not deleted", Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog =buider.create();
            alertDialog.show();
        }

        /**
         * Displays a dialog to prompt user to enter new category name.
         * @param category
         */
        public void updateCategory(Category category) {
            AlertDialog.Builder buider = new AlertDialog.Builder(context);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.dialog_update_category,null);
            buider.setView(view);
            AlertDialog alertDialog = buider.create();
            alertDialog.show();

            EditText nameET = view.findViewById(R.id.et_name);
            nameET.setText(category.getName());

            view.findViewById(R.id.btn_update_category_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = nameET.getText().toString().trim();
                    if (name.isEmpty()) {
                        nameET.setError("category is empty");
                        nameET.requestFocus();
                        return;
                    }
                    Category existingCategory = taskRoomDatabase.categoryDao().getCategoryMatchingName(name);
                    if (existingCategory !=  null
                            && existingCategory.getName().equals(name)
                            && existingCategory.getId() != category.getId()) {
                        nameET.setError("category already exists");
                        nameET.requestFocus();
                        return;
                    }
                    taskRoomDatabase.categoryDao().updateCategory(category.getId(),name);
                    loadCategories();
                    notifyItemChanged(getAdapterPosition());
                    alertDialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_cancel_save_category).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }

        /**
         * Retrieves the latest categories from the room database.
         */
        private void loadCategories() {
            categoryList = taskRoomDatabase.categoryDao().getAllCategories();
        }

        /**
         * Executed handler when the item is clicked.
         * @param view
         */
        @Override
        public void onClick(View view) {
            updateCategory(categoryList.get(getAdapterPosition()));
           // if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    /*
    Category getItem(int id) {
        return categoryList.get(id);
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }*/
}
