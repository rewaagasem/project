package com.rewaagasem.university_project_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rewaagasem.university_project_android.R;
import com.rewaagasem.university_project_android.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodVH> {


    List<Food> recordList;

    public  interface FoodListener{
        void onEditClickListener(Context context,String food_id);
        void onDeleteClickListener(String food_id);
    }

    FoodListener listener;
    public FoodAdapter(List<Food> recordList,FoodListener listener) {
        this.recordList =recordList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public FoodVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_row, parent, false);
        return new FoodVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodVH holder, int position) {
        holder.onBind(recordList.get(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }


    class FoodVH extends RecyclerView.ViewHolder {

        TextView food_name,food_category,food_calory;
        ImageView food_image,delete_image;
        Button edit_image;


        public FoodVH(@NonNull View itemView) {
            super(itemView);

            food_name = itemView.findViewById(R.id.food_name);
            food_category = itemView.findViewById(R.id.food_category);
            food_calory = itemView.findViewById(R.id.food_calory);
            food_image = itemView.findViewById(R.id.food_image);
            delete_image = itemView.findViewById(R.id.delete_image);
            edit_image = itemView.findViewById(R.id.edit_image);

        }

        public void onBind(Food record) {

            food_name.setText(record.getName());
            food_category.setText(record.getCategory());
            food_calory.setText(record.getCalory());
            Picasso.get().load(record.getKey_name()).into(food_image);
//            food_image.setImageBitmap(BitmapFactory.decodeByteArray(record.getImage(),0,record.getImage().length));
            delete_image.setOnClickListener(v->{
                listener.onDeleteClickListener(record.getId());
            });

            edit_image.setOnClickListener(v->{
                listener.onEditClickListener(itemView.getContext(),record.getId());
            });

        }
    }


}