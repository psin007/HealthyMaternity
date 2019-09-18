package com.example.rural_healthy_mom_to_be.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;

import java.util.UUID;

public class FormActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etHeight;
    private EditText etWeightBeforePreg;
    private EditText etCurrentWeight;
    private EditText etWeeksPregnant;
    private String username;
    private String height;
    private String prePregnancyWeight;
    private String currentWeight;
    private String weeksPregnant;

    LoggedInUserDb loggedInUserdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        loggedInUserdb = Room.databaseBuilder(getApplicationContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();
    }

    public void nextForm(View view) {
        int flag = 0;
        etUserName = (EditText) findViewById(R.id.etUserName);

        //If username is empty
        if (etUserName.getText().toString().isEmpty()) {
            etUserName.setError("This field is mandatory!");
            flag = 1;
        } else {
            username = etUserName.getText().toString();
        }
        etHeight = (EditText) findViewById(R.id.etHeight);
        //If height is empty
        if (etHeight.getText().toString().isEmpty()) {
            etHeight.setError("This field is mandatory!");
            flag = 1;
        } else if (etHeight.getText().toString().charAt(0) == '.') {
            etHeight.setError("The first digit should be between 0-9");
            flag = 1;
        } else if (Double.parseDouble(etHeight.getText().toString()) < 50 || Double.parseDouble(etHeight.getText().toString()) > 250) {
            etHeight.setError("Please enter the value within the range 50 - 250 cm!");
            flag = 1;
        } else {
            height = etHeight.getText().toString();
        }

        etWeightBeforePreg = (EditText) findViewById(R.id.etWeightBeforePreg);
        //If weight before pregnancy is empty
        if (etWeightBeforePreg.getText().toString().isEmpty()) {
            etWeightBeforePreg.setError("This field is mandatory!");
            flag = 1;
        } else if (etWeightBeforePreg.getText().toString().charAt(0) == '.') {
            etWeightBeforePreg.setError("The first digit should be between 0-9");
            flag = 1;
        } else if (Double.parseDouble(etWeightBeforePreg.getText().toString()) < 25 || Double.parseDouble(etWeightBeforePreg.getText().toString()) > 250) {
            etWeightBeforePreg.setError("Please enter the value within the  range 25 - 250 kg!");
            flag = 1;
        } else {
            prePregnancyWeight = etWeightBeforePreg.getText().toString();
        }

        if(flag == 0){
            Intent intent = new Intent(FormActivity.this, SecondPageForm.class);
//            intent.putExtra("username",username);
//            intent.putExtra("height",height);
//            intent.putExtra("prePregnancyWeight",prePregnancyWeight);
            startActivity(intent);
            finish();
        }
    }
}


