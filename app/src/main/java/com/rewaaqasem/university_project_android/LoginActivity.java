package com.rewaaqasem.university_project_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    TextView go_to_signup;
    ImageView show_pass,hide_pass;
    EditText username,password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        go_to_signup = findViewById(R.id.go_to_signup);
        show_pass= findViewById(R.id.show_password);
        hide_pass=findViewById(R.id.hide_password);
        username= findViewById(R.id.username);
        password=findViewById(R.id.password);
        login = findViewById(R.id.login);
        go_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mUsername = username.getText().toString();

                String mPassword = password.getText().toString();

                if(mUsername.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Username required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Password required", Toast.LENGTH_SHORT).show();
                    return;
                }


                //TODO Login *****-*** :)--(:

            }
        });

    }
}