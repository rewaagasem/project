package com.rewaagasem.university_project_android.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rewaagasem.university_project_android.databinding.ActivitySplashBinding;

public class Splash_activity extends AppCompatActivity {

//    private static final int SPLASH_TIME_OUT = 2000;


    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goNextPage.setOnClickListener(v->{
            Intent intent = new Intent(this, com.rewaagasem.university_project_android.ui.auth.LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Splash_activity.this.finish();
        });

    }


//   @Override
//    protected void onResume() {
//        super.onResume();
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    super.run();
//                    sleep(SPLASH_TIME_OUT);
//                } catch (InterruptedException e) {
//                    System.out.println(e.toString());
//                } finally {
//                    Intent intent = new Intent(Splash_activity.this,LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//
//                }
//            }
//        };
//        thread.start();
//    }
}