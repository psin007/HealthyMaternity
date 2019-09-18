package com.example.rural_healthy_mom_to_be.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;

public class SecondPageForm extends AppCompatActivity {
    LoggedInUserDb loggedInUserdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page_form);
        loggedInUserdb = Room.databaseBuilder(getApplicationContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();
    }
    public void putValuesInSharedPreference(){
        SharedPreferences loggedUser = getApplicationContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor loggedUsered = loggedUser.edit();

        loggedUsered.putString("username","pooja");
        loggedUsered.putString("userHeight","160");
        loggedUsered.putString("prePregnancyWeight","68");
        loggedUsered.putString("currentWeight","69");
        loggedUsered.putString("weeksPregnant","3");
        loggedUsered.putString("loggedIn","yes");

        loggedUsered.commit();
        InsertDatabase insertDatabase = new InsertDatabase();
        insertDatabase.execute();
    }
    public void finishRegister(View view){
        putValuesInSharedPreference();
      Intent intent = new Intent(SecondPageForm.this,FormActivity.class);
    }

    private class InsertDatabase extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            LoggedinUser loggedinUser = new LoggedinUser("pooja", "obese", Double.valueOf("160"),Double.valueOf("68"), Integer.parseInt("3"), Integer.parseInt("3"), Double.valueOf("69"));
            long id = loggedInUserdb.loggedInUserDao().insert(loggedinUser);
            return id+"";
        }

        @Override protected void onPostExecute(String details) {
            Intent intent = new Intent(SecondPageForm.this, NavigationDrawer.class);
            startActivity(intent);
            finish();
        }
    }
}
