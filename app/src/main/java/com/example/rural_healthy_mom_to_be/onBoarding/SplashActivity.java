package com.example.rural_healthy_mom_to_be.onBoarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.View.NavigationDrawer;
/*
* This is the first class to run. It shows splash screen and if user is logged in, it dirests to homepage, otherwise show flashcards
* */
public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME = 1500; // 1500 ms duration for splash screen to run

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

                    SharedPreferences loggedUser = getApplicationContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
                    final String loggedIn = loggedUser.getString("loggedIn","no");
                    //if user is logged in , directly go to homescreen
                    if(loggedIn.equals("yes")){
                        Intent intent = new Intent(SplashActivity.this, NavigationDrawer.class);
                        startActivity(intent);
                        finish();
                    }
                    //else show flashcards to tell about the application to user
                    else{
                        Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    finish();

                }
            }
        };
        timer.start();
    }
}
