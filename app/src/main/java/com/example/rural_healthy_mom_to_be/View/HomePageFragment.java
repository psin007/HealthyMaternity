package com.example.rural_healthy_mom_to_be.View;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;
import com.example.rural_healthy_mom_to_be.Repository.WeightValuesAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomePageFragment  extends Fragment {
    View vHomePage;
    LoggedInUserDb loggedInUserdb;
    TextView homeHeader;
    TextView homeDate;
    TextView weekLabel;
    TextView weightBeforePreg;
    TextView currentWeight;
    TextView minWeight;
    TextView maxWeight;
    TextView message;
    double currentWeightValue;
    double weightBeforePregValue;
    int currentWeek;
    String BMIClass;
    LoggedinUser currentUser;

    //TODO remove static and global variable
    static public double maxWeightValue;
    static public double minWeightValue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vHomePage = inflater.inflate(R.layout.homepagefragment, container, false);
        loggedInUserdb = Room.databaseBuilder(vHomePage.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();
        homeHeader = vHomePage.findViewById(R.id.home_header_text);
        homeDate = vHomePage.findViewById(R.id.home_date_text);
        weekLabel = vHomePage.findViewById(R.id.weekladbel);
      //  weightBeforePreg = vHomePage.findViewById(R.id.weight_before_preg);
        currentWeight = vHomePage.findViewById(R.id.weight_current);
        minWeight = vHomePage.findViewById(R.id.min_ideal);
        maxWeight = vHomePage.findViewById(R.id.max_ideal);
        message = vHomePage.findViewById(R.id.msg);
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        return vHomePage;
    }


    public void editCurrentWeight(View view) {
        Log.d("editCurrentWeight","editWeight");
        final EditText editWeight = new EditText(this.getActivity());
        editWeight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editWeight.setHint("Enter new weight value");
        editWeight.setPadding(55,55,55,55);
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setView(editWeight);

        alert.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("editCurrentWeight",editWeight.getText().toString());
                if(!editWeight.getText().toString().isEmpty())
                {
                currentUser.setCurrentWeight(Double.parseDouble(editWeight.getText().toString()));
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute();
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


    public void alertIdealMessage(View view) {
        final TextView textView = new TextView(this.getActivity());
        textView.setText("You can check here how much of weight you should have gained this week and compare it with your progress. ");
        textView.setPadding(25,25,25,25);
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setView(textView);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    public void messageDialog(View view) {
        final TextView textView = new TextView(this.getActivity());
        textView.setText("You can generate report of your weight gain during different weeks of pregnancy and share it with your doctor to get feedback ! ");
        textView.setPadding(25,25,25,25);
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setView(textView);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }


    private class UpdateDatabase extends AsyncTask<Void, Void, String > {


        @Override
        protected String doInBackground(Void... voids) {
            loggedInUserdb.loggedInUserDao().updateUsers(currentUser);
            return currentUser.getCurrentWeight()+"";
        }
        @Override
        protected void onPostExecute(String newWeight) {
            currentWeight.setText(newWeight);
            if(currentWeightValue < minWeightValue){
                message.setText("Great going! Just a little bit lower than recommended weight gain  this week.");
            }
            else if(currentWeightValue > maxWeightValue){
                message.setText("Your progress is amazing! Just a bit over than recommended weight gain this week.");
            }
            else{
                message.setText("You are perfect! Your weight gain is in recommended range this week.");
            }        }

    }


    private class ReadDatabase extends AsyncTask<Void, Void, LoggedinUser> {


        @Override
        protected LoggedinUser doInBackground(Void... voids) {
            List<LoggedinUser> userList = loggedInUserdb.loggedInUserDao().getAll();
            currentUser = userList.get(0);
            return userList.get(0);
        }
        protected void onPostExecute(LoggedinUser details) {
            //Iter2
            weekLabel.setText("WEEK "+String.valueOf(details.getCurrentWeek()));
            //homeHeader.setText("You are now "+details.getCurrentWeek()+" weeks Pregnant");
           // homeHeader.setText("WELCOME "+ details.getUsername().toUpperCase());
            homeHeader.setText("HELLO "+ details.getUsername().toUpperCase());
            currentWeek = details.getCurrentWeek();
            BMIClass = details.getBMIClass();
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
         //   homeDate.setText("Today: "+formattedDate);
            homeDate.setText("Welcome to Healthy Maternity");

          //  welcomeMesage.setText("Hi "+details.getUsername()+",\nWelcome to Healthy Maternity!");

        //    weightBeforePreg.setText(details.getWeightBeforePregnancy()+"");

            currentWeight.setText(details.getCurrentWeight()+"  ");
            currentWeightValue = details.getCurrentWeight();
            weightBeforePregValue = details.getWeightBeforePregnancy();

            GetWeightValues getWeightValues = new GetWeightValues();
            getWeightValues.execute();

        }
    }
    private class GetWeightValues extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return WeightValuesAPI.getWeightValues();

        }

        @Override
        protected void onPostExecute(String result) {
            JSONArray jsonarray = null;
            double minGain;
            int week;
            String bmiClass;
            double maxGain;
            Log.d("week",currentWeek+"");
            Log.d("BMI",BMIClass);

            try {
                jsonarray = new JSONArray(result);
                for(int i =0;i<jsonarray.length();i++){
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    week = jsonobject.getInt("week");
                    bmiClass = jsonobject.getString("BMI_Classification");

                    if(week == currentWeek && bmiClass.equals(BMIClass)){
                        minGain = jsonobject.getDouble("min_weight_gain");
                        maxGain = jsonobject.getDouble("max_weight_gain");
                        maxWeightValue = weightBeforePregValue + maxGain;
                        minWeightValue  = weightBeforePregValue + minGain;
                        minWeight.setText(String.format("%.1f",minWeightValue) + " KG");
                        maxWeight.setText(String.format("%.1f",maxWeightValue) + " KG");

                    }
                }

                if(currentWeightValue < minWeightValue){
                    message.setText("Great going! Just a little bit lower than recommended weight gain  this week.");
                }
                else if(currentWeightValue > maxWeightValue){
                    message.setText("Your progress is amazing! Just a bit over than recommended weight gain this week.");
                }
                else{
                    message.setText("You are perfect! Your weight gain is in recommended range this week.");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface FragmentInteracion{
        public void editWeightClicked(View v);


    }
}
