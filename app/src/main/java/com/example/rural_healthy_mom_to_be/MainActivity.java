package com.example.rural_healthy_mom_to_be;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rural_healthy_mom_to_be.View.FormActivity;
import com.example.rural_healthy_mom_to_be.View.NavigationDrawer;
import com.example.rural_healthy_mom_to_be.View.WelcomeActvity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SharedPreferences loggedUser = getApplicationContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
        String loggedIn = loggedUser.getString("loggedIn","no");
        if(loggedIn.equals("yes")){
            Intent intent = new Intent(MainActivity.this, NavigationDrawer.class);
            startActivity(intent);
            //ToDo: RR added this line to remove the MainActivity from back stack.
            finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this, WelcomeActvity.class);
            startActivity(intent);
            //ToDo: RR added this line to remove the MainActivity from back stack.
            finish();
        }



    }
}
