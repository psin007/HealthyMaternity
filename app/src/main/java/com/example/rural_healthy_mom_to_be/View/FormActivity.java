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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.Model.Weight;
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

    public void nextForm(View view){
        int flag = 0;
        etUserName = (EditText)findViewById(R.id.etUserName);

        //If username is empty
        if(etUserName.getText().toString().isEmpty()){
            etUserName.setError("This field is mandatory!");
            flag = 1;
        }
        else{
            username = etUserName.getText().toString();
        }
        etHeight = (EditText)findViewById(R.id.etHeight);
        //If height is empty
        if(etHeight.getText().toString().isEmpty()){
            etHeight.setError("This field is mandatory!");
            flag = 1;
        }
        else if(Double.parseDouble(etHeight.getText().toString()) < 50 || Double.parseDouble(etHeight.getText().toString()) > 250){
            etHeight.setError("Please enter the value within the available range 50 - 250 cm!");
            flag = 1;
        }
        else {
            height = etHeight.getText().toString();
        }

        etWeightBeforePreg = (EditText)findViewById(R.id.etWeightBeforePreg);
        //If weight before pregnancy is empty
        if(etWeightBeforePreg.getText().toString().isEmpty()){
            etWeightBeforePreg.setError("This field is mandatory!");
            flag = 1;
        }
        else if(Double.parseDouble(etHeight.getText().toString()) < 25 || Double.parseDouble(etHeight.getText().toString()) > 200){
            etWeightBeforePreg.setError("Please enter the value within the available range 25 - 200 kg!");
            flag = 1;
        }
        else{
            prePregnancyWeight = etWeightBeforePreg.getText().toString();
        }
        etCurrentWeight = (EditText)findViewById(R.id.etCurWeight);

        //If etCurrentWeight is empty
        if(etCurrentWeight.getText().toString().isEmpty()){
            etCurrentWeight.setError("This field is mandatory!");
            flag = 1;
        }
        else if(Double.parseDouble(etCurrentWeight.getText().toString()) < 25 || Double.parseDouble(etCurrentWeight.getText().toString()) > 200){
            etCurrentWeight.setError("Please enter the value within the available range 25 - 200 kg!");
            flag = 1;
        }
        else{
            currentWeight = etCurrentWeight.getText().toString();
        }

        //Check which radio button is selected
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioOption);
        int conceptionButtonId = rg.getCheckedRadioButtonId();
        RadioButton weekRadioButton = (RadioButton) findViewById(conceptionButtonId);
        String weekRadioButtonText = weekRadioButton.getText().toString();
        if(weekRadioButtonText.equals("I know my conception date")){
            //invoke date picker
        }
        else if(weekRadioButtonText.equals("I know my current week of pregnancy")){
            //invoke dialog box
         //   askCurWeek();
        }

//        etWeeksPregnant = (EditText)findViewById(R.id.et_curweek);
//        //If etWeeksPregnant is empty
//        if(etWeeksPregnant.getText().toString().isEmpty()){
//            etWeeksPregnant.setError("This field is mandatory!");
//            flag = 1;
//        }
//        else if(Integer.parseInt(etWeeksPregnant.getText().toString()) > 40){
//            etWeeksPregnant.setError("Please enter weeks between 0 to 40!");
//            flag = 1;
//        }
//        else{
//            weeksPregnant = etWeeksPregnant.getText().toString();
//        }


//        if(flag == 0){
//            checkIfUserExist();
//        }
    }

    public void askCurWeek(View view){

        final EditText curWeek = new EditText(this.getApplicationContext());
        curWeek.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        curWeek.setHint("Enter your current week of pregnancy");
        curWeek.setPadding(55,55,55,55);
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getApplicationContext());
        alert.setView(curWeek);

        alert.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!curWeek.getText().toString().isEmpty())
                {
                   //check if it is between 1 to 40 and then save in user's current week to be passed further
                   // weeksPregnant = curWeek.getText().toString();
                }

            }
        });
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    protected void checkIfUserExist(){
        CheckUserInDb checkUserInDb = new CheckUserInDb();
        checkUserInDb.execute();
    }


    private class CheckUserInDb extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {
            LoggedinUser loggedinUser = null;
            loggedinUser = loggedInUserdb.loggedInUserDao().findByUsername(username); //To do - bug in this line

            if(loggedinUser!=null) { //username already exists
                return "exists";
            }
            else{
                return "doesnotexists";
            }
        }
        @Override
        protected void onPostExecute(String userCheck) {
            if(userCheck.equals("exists")){
                etUserName.setError("Username already exists!");
            }
            else{
                putValuesInSharedPreference();

            }
        }
        protected void putValuesInSharedPreference(){
            SharedPreferences loggedUser = getApplicationContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor loggedUsered = loggedUser.edit();
            //ToDo: DO we really need to save this data other than loggedIn in our shared preference?


            loggedUsered.putString("username",username);
            loggedUsered.putString("userHeight",height);
            loggedUsered.putString("prePregnancyWeight",prePregnancyWeight);
            loggedUsered.putString("currentWeight",currentWeight);
            loggedUsered.putString("weeksPregnant",weeksPregnant);
            loggedUsered.putString("loggedIn","yes");

            loggedUsered.commit();
            InsertDatabase insertDatabase = new InsertDatabase();
            insertDatabase.execute();
        }
    }

    private class InsertDatabase extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            //ToDo: change the first parameter to make it unique
            //ToDo: calculate BMI class
            double heightInM = Double.parseDouble(height)*0.01;
            heightInM = heightInM *heightInM;
            double BMI = Double.parseDouble(prePregnancyWeight) / heightInM;
            String BMIClass="";
            if(BMI <18.5){
                BMIClass = "underweight";
            }
            else if(BMI >=18.5 && BMI <= 24.9){
                BMIClass = "normal weight";
            }
            else if(BMI >= 25 && BMI <=29.9 ){
                BMIClass = "overweight";
            }
            else if(BMI >=30){
                BMIClass = "obesity";
            }
            LoggedinUser loggedinUser = new LoggedinUser(username, BMIClass, Double.valueOf(height),Double.valueOf(prePregnancyWeight), Integer.parseInt(weeksPregnant), Integer.parseInt(weeksPregnant), Double.valueOf(currentWeight));
            //ToDo: check the return value for ensuring that the insert is succesfull or not.
            long id = loggedInUserdb.loggedInUserDao().insert(loggedinUser);

            loggedInUserdb.weightDao().deleteAll();
            Weight newWeight = new Weight((int)id,Double.valueOf(currentWeight),Integer.parseInt(weeksPregnant));
            loggedInUserdb.weightDao().insert(newWeight);
            return id+"";
        }

        @Override protected void onPostExecute(String details) {
            //ToDo: based on the return value make the neccesary redirection.
            Intent intent = new Intent(FormActivity.this, NavigationDrawer.class);
            startActivity(intent);
            finish();
        }
    }
}


