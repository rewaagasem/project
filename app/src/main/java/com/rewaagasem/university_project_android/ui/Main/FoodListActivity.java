package com.rewaagasem.university_project_android.ui.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rewaagasem.university_project_android.adapters.FoodAdapter;
import com.rewaagasem.university_project_android.databinding.ActivityFoodListBinding;
import com.rewaagasem.university_project_android.firebase.MyFirebaseProvider;
import com.rewaagasem.university_project_android.model.Food;
import com.rewaagasem.university_project_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    ActivityFoodListBinding binding;

    private FoodAdapter adapter;
    User mUser;
    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference recordsDBRef, userDBref,delete_reference;
    final List<Food>  my_records = new ArrayList<>();
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFoodListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        initUI();
        initDBRef();
        setData();
        
    }
    private void initUI(){
        adapter = new FoodAdapter(my_records, new FoodAdapter.FoodListener() {
            @Override
            public void onEditClickListener(Context context, String food_id) {
                Intent intent = new Intent(context, com.rewaagasem.university_project_android.ui.Main.AddFoodActivity.class);
                intent.putExtra("food_id",food_id);
                intent.putExtra("uid",user.getUid());
                startActivity(intent);
            }

            @Override
            public void onDeleteClickListener(String food_id) {
                deleteFood(food_id);
            }
        });
        binding.foodRv.setAdapter(adapter);
    }

    private void deleteFood(String food_id) {
//        Toast.makeText(FoodListActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();

        delete_reference = MyFirebaseProvider.getChildDatabaseReference("Food").child(user.getUid()).child(food_id);
        delete_reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FoodListActivity.this, "Food Removed Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initDBRef() {
        recordsDBRef = MyFirebaseProvider.getChildDatabaseReference("BMI_Rec");

        recordsDBRef.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userDBref = MyFirebaseProvider.getChildDatabaseReference("Users").child(user.getUid());
        userDBref.keepSynced(true);
        userDBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    
    public void setData() {
        my_records.clear();
        MyFirebaseProvider.getChildDatabaseReference("Food").child(user.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        my_records.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Food c = snapshot1.getValue(Food.class);
                            my_records.add(c);
                        }
                        adapter.notifyDataSetChanged();
                        
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}