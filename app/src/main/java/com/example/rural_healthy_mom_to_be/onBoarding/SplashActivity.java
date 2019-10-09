package com.example.rural_healthy_mom_to_be.onBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rural_healthy_mom_to_be.R;

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
                        Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
                        startActivity(intent);
                        finish();

                }
            }
        };
        timer.start();
    }
}
