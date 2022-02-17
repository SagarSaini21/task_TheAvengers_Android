package com.example.task_theavengers_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_theavengers_android.HomePage;
import com.example.task_theavengers_android.R;
import com.example.task_theavengers_android.entity.Category;
import com.example.task_theavengers_android.entity.Image;
import com.example.task_theavengers_android.util.TaskRoomDatabase;

import java.io.File;
import java.util.List;

public class DetailsImagesAdapter extends RecyclerView.Adapter<DetailsImagesAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Image> imageList;
    private TaskRoomDatabase taskRoomDatabase;
    private Context  context;

    public DetailsImagesAdapter(Context context, List<Image> imageList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.imageList = imageList;
        this.context = context;
        taskRoomDatabase = TaskRoomDatabase.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.details_image_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DetailsImagesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Image image = imageList.get(position);
        File imgFile = new  File(image.getPath());

        if(imgFile.exists()){
          Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
          holder.image.setImageBitmap(myBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.detailsImage);
        }

      @Override
      public void onClick(View v) {

      }
    }
}
