package com.example.rural_healthy_mom_to_be.onBoarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.View.NavigationDrawer;

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //
                    SharedPreferences loggedUser = getApplicationContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
                    final String loggedIn = loggedUser.getString("loggedIn","no");
                    if(loggedIn.equals("yes")){
                        Intent intent = new Intent(SplashActivity.this, NavigationDrawer.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    //
//                        Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
//                        startActivity(intent);
                    finish();

                }
            }
        };
        timer.start();
    }
}
