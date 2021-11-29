package com.rewaaqasem.university_project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    TextView go_to_login;
    ImageView show_pass,hide_pass;
    EditText username,password,email,confirm_password;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        go_to_login = findViewById(R.id.go_to_login);
        show_pass= findViewById(R.id.show_password);
        hide_pass=findViewById(R.id.hide_password);
        username= findViewById(R.id.username);
        password=findViewById(R.id.password);
        email= findViewById(R.id.email);
        confirm_password= findViewById(R.id.confirm_password);
        register = findViewById(R.id.create);


        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_pass.setVisibility(View.VISIBLE);
                show_pass.setVisibility(View.GONE);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


        hide_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_pass.setVisibility(View.VISIBLE);
                hide_pass.setVisibility(View.GONE);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mUsername = username.getText().toString();
                String mEmail = email.getText().toString();
                String mPassword = password.getText().toString();
                String mConfirm_password = password.getText().toString();

                if(mUsername.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Username required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mEmail.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Email required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mPassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Password required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mConfirm_password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Confirm password required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(! mPassword.equals(mConfirm_password)){
                    Toast.makeText(RegisterActivity.this, "Confirm password required", Toast.LENGTH_SHORT).show();
                    return;
                }


                //TODO Register *****-*** :)--(:

            }
        });
    }
}