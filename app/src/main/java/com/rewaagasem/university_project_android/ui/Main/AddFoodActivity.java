package com.rewaagasem.university_project_android.ui.Main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rewaagasem.university_project_android.R;
import com.rewaagasem.university_project_android.databinding.ActivityAddFoodBinding;
import com.rewaagasem.university_project_android.firebase.MyFirebaseProvider;
import com.rewaagasem.university_project_android.model.Food;
import com.rewaagasem.university_project_android.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

public class AddFoodActivity extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_reference = database.getReference("Users").child(user.getUid());
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri image_uri;

    User mUser;
    Food food;
    ActivityAddFoodBinding binding;
    String[] food_categories = {"Fish", "Vegetables",
            "Juice", "Fruits",
            "Fries", "Frozen"};
    String food_id = "";
    private static final int REQUEST_GALLERY_CODE_PRIMARY = 201;
    String cat;

    final List<Food> my_records = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Intent intent = getIntent();
        food_id = intent.getStringExtra("food_id");
        if (food_id == null) {
            food_id = "";
        }else{
            setData(food_id);
        }

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                food_categories);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.category.setAdapter(ad);

        binding.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat = food_categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.uploadImage.setOnClickListener(v -> {
            selectImagePrimary();
        });

        binding.saveRecord.setOnClickListener(v -> {

            if (validate_fields()) {
                uploadImage();
            }
        });
    }

    private boolean validate_fields() {
        if (binding.foodName.getText().toString().isEmpty()) {
            binding.foodName.setError(getResources().getString(R.string.required));
            return false;
        }
        if (binding.calory.getText().toString().isEmpty()) {
            binding.calory.setError(getResources().getString(R.string.required));
            return false;
        }
        return true;
    }

    private void selectImagePrimary() {
        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_CODE_PRIMARY);
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE_PRIMARY) {
            if (resultCode == RESULT_OK) {
                String path = getRealPathFromURI(this, data.getData());
                Log.d("path", path);

                image_uri = data.getData();
                binding.attachImage.setImageURI(image_uri);
            }
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static File getFile(String path) {
        return new File(path);
    }

    private void uploadImage() {
        if (image_uri != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference

            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(image_uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    progressDialog.dismiss();
                                    Toast.makeText(AddFoodActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT).show();
                                    Food new_food = new Food();
                                    new_food.setName(binding.foodName.getText().toString());
                                    new_food.setCalory(binding.calory.getText().toString());
                                    new_food.setCategory(cat);

                                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            new_food.setKey_name(task.getResult().toString());
                                            new_food.setUid(user.getUid());

                                            if(food_id.isEmpty()){
                                                String mfood_id = new Date().getTime() +"";
                                                new_food.setId(mfood_id);
                                                DatabaseReference foods_refernce = database.getReference("Food").child(user.getUid()).child(mfood_id);

                                                foods_refernce.setValue(new_food).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(AddFoodActivity.this, "Food Added Successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                DatabaseReference foods_refernce = database.getReference("Food").child(user.getUid()).child(food_id);
                                                new_food.setId(food_id);
                                                foods_refernce.setValue(new_food).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(AddFoodActivity.this, "Food Updated Successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddFoodActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }


    public void setData(String food_id) {

        MyFirebaseProvider.getChildDatabaseReference("Food").child(user.getUid()).child(food_id).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        food = snapshot.getValue(Food.class);
                        binding.calory.setText(food.getCalory());
                        for (int j = 0; j < food_categories.length; j++) {
                            if (food.getCategory().equals(food_categories[j])) {
                                binding.category.setSelection(j);
                            }
                        }
                        binding.foodName.setText(food.getName());
                        Picasso.get().load(food.getKey_name()).into(binding.attachImage);
                        Bitmap bitmap = ((BitmapDrawable)binding.attachImage.getDrawable()).getBitmap();
                        image_uri =  getImageUri(AddFoodActivity.this,bitmap);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(AddFoodActivity.this.getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing");
        return Uri.parse(path);
    }
}