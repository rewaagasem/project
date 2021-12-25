package com.rewaagasem.university_project_android.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rewaagasem.university_project_android.R;
import com.rewaagasem.university_project_android.databinding.ActivitySignUpBinding;
import com.rewaagasem.university_project_android.model.User;

public class Sign_upActivity extends AppCompatActivity {


    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;

    private SharedPreferences loginPreferences = null;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        binding.login.setOnClickListener(v->{
            startActivity(new Intent(Sign_upActivity.this, com.rewaagasem.university_project_android.ui.auth.LoginActivity.class));
        });

        binding.showPassword.setOnClickListener(v->{

            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.hidePassword.setVisibility(View.VISIBLE);
            binding.showPassword.setVisibility(View.GONE);
        });

        binding.hidePassword.setOnClickListener(v->{

            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.showPassword.setVisibility(View.VISIBLE);
            binding.hidePassword.setVisibility(View.GONE);
        });


        binding.create.setOnClickListener(v->{

            validateData();
        });

    }

    private void validateData() {


        if(binding.username.getText().toString().isEmpty()){
            binding.username.setError(getResources().getString(R.string.required));
            return ;
        }

        if(binding.email.getText().toString().isEmpty()){
            binding.email.setError(getResources().getString(R.string.required));
            return ;
        }

        if(binding.password.getText().toString().isEmpty()){
            binding.password.setError(getResources().getString(R.string.required));
            return ;
        }

        if(binding.rePassword.getText().toString().isEmpty()){
            binding.rePassword.setError(getResources().getString(R.string.required));
            return ;
        }

        if(! binding.password.getText().toString().equals(binding.rePassword.getText().toString())){
            Toast.makeText(Sign_upActivity.this, "Password are not similar", Toast.LENGTH_SHORT).show();
            binding.rePassword.setError(getResources().getString(R.string.required));
            return;
        }

        mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(binding.username.getText().toString()).build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loginPrefsEditor.putString("username", user.getDisplayName());
                                                loginPrefsEditor.commit();
                                                User user1 = new User();
                                                user1.setID(user.getUid());
                                                user1.setUsername(binding.username.getText().toString());
                                                user1.setEmail(binding.email.getText().toString());
                                              myRef = database.getReference("Users").child(user.getUid());
                                                myRef.setValue(user1);

                                                Intent intent = new Intent(Sign_upActivity.this, UserInformationActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });



                        } else {

                            Toast.makeText(Sign_upActivity.this, "Authentication failed." + task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}