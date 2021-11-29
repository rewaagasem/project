package com.rewaaqasem.university_project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


    private static final int SPLASH_TIME = 5000;

    TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(SPLASH_TIME);
                } catch (InterruptedException e) {
                    //caught an exception
                } finally {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
            }
        };
        thread.start();
    }



}