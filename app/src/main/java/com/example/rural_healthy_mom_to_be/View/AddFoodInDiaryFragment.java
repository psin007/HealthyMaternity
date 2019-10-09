package com.example.rural_healthy_mom_to_be.View;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.Food;
import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.Model.Summary;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;
import com.example.rural_healthy_mom_to_be.Repository.NutritionAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddFoodInDiaryFragment extends Fragment {
    View vFoodAdd;
    String foodToSearch;
    View addQuantity;
    View layoutFacts;
    View quantityLayout;
    Food food;
    TextView tvCaloriesFacts;
    LoggedInUserDb loggedInUserdb;
    LoggedinUser currentUser;
    TextView tvFatFacts;
    EditText etSearchFood;
    EditText etQuantity;
    Button searchButton;
    Button addButton;
    TextView tvServingUnit;
    private List<LoggedinUser> userList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vFoodAdd = inflater.inflate(R.layout.add_a_meal, container, false);
        tvCaloriesFacts = vFoodAdd.findViewById(R.id.tv_cal_values);
        tvFatFacts = vFoodAdd.findViewById(R.id.tv_fat_values);
        etSearchFood = vFoodAdd.findViewById(R.id.et_searchFood);
        etQuantity = vFoodAdd.findViewById(R.id.etQuantity);
        searchButton = vFoodAdd.findViewById(R.id.searchButton);
        addButton = vFoodAdd.findViewById(R.id.confirmAddButton);
        addQuantity = vFoodAdd.findViewById(R.id.addQuantity);
        layoutFacts = vFoodAdd.findViewById(R.id.layoutFacts);
        quantityLayout = vFoodAdd.findViewById(R.id.quantityLayout);
        tvServingUnit = vFoodAdd.findViewById(R.id.tvServingUnit);

        loggedInUserdb = Room.databaseBuilder(vFoodAdd.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();

        ReadDatabase readdb = new ReadDatabase();
        readdb.execute();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(etSearchFood.getText().toString().isEmpty())) {
                    String foodToSearch = etSearchFood.getText().toString();
                    new GetNDBNumber().execute(foodToSearch);

                } else {
                    etSearchFood.setError("Enter food to search");
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertRecord insert = new InsertRecord();
                insert.execute(etSearchFood.getText().toString(),
                        etQuantity.getText().toString());
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertRecord insert = new InsertRecord();
                insert.execute(etSearchFood.getText().toString(),
                        etQuantity.getText().toString());
            }
        });

        return vFoodAdd;

    }

    class GetNDBNumber extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return NutritionAPI.getNDBNO(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.contains("errors")) {
                    etSearchFood.setError("Please enter another food item");
                } else {
                    addButton.setVisibility(View.VISIBLE);
                    addQuantity.setVisibility(View.VISIBLE);
                    layoutFacts.setVisibility(View.VISIBLE);
                    quantityLayout.setVisibility(View.VISIBLE);
                    JSONObject jsonobject = new JSONObject(result);

                    JSONArray jsonArray = jsonobject.getJSONObject("list").getJSONArray("item");
                    String nbdno = jsonArray.getJSONObject(0).getString("ndbno");
                    GetNutrientData getNutrientData = new GetNutrientData();
                    getNutrientData.execute(nbdno);

                }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }

        class GetNutrientData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                return NutritionAPI.getData(params[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                paresResult(result);
            }
        }

        private void paresResult(String result) {
            double fat;
            double calories;
            String servingUnit;
            double servingAmount;
            String category;
            String foodName;
            String fatFact = "";
            String calFact = "";
            try {
                food = new Food();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String nutrientId = jsonArray.getJSONObject(i).getString("nutrient_id");
                    if (nutrientId.equals("204")) {
                        fat = Double.parseDouble(jsonArray.getJSONObject(i).getString("value"));
                        JSONArray measuresArray = jsonArray.getJSONObject(i).getJSONArray("measures");
                        servingAmount = measuresArray.getJSONObject(0).getDouble("qty");
                        servingUnit = measuresArray.getJSONObject(0).getString("label");
                        food.setFat(fat);
                        food.setServingunit(servingUnit);
                        fatFact = "\nFat - " + fat;

                    }
                    if (nutrientId.equals("208")) {
                        calories = Double.parseDouble(jsonArray.getJSONObject(i).getString("value"));
                        food.setCalorieamount(calories);
                        calFact = "\nCalories -" + calories;
                    }
                }
                String NutrientFact = "Nutrient facts:" + fatFact + calFact;
                tvCaloriesFacts.setText(food.getCalorieamount() + "");
                tvFatFacts.setText(food.getFat() + "");
                tvServingUnit.setText(food.getServingunit());

                //set nutrientTv values

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private class ReadDatabase extends AsyncTask<Void, Void, LoggedinUser> {
            @Override
            protected LoggedinUser doInBackground(Void... voids) {
                userList = loggedInUserdb.loggedInUserDao().getAll();
                currentUser = userList.get(0);
                return null;
            }

            protected void onPostExecute() {
            }
        }

        private class InsertRecord extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String foodName = params[0];
                int unit = Integer.valueOf(params[1]);

                Summary newRecord = new Summary(currentUser.getUserid(), foodName, unit,
                        Double.valueOf(tvCaloriesFacts.getText().toString()),
                        Double.valueOf(tvFatFacts.getText().toString()));
                loggedInUserdb.summaryDao().insert(newRecord);
                return params[0];
            }

            protected void onPostExecute(String food) {
                Snackbar.make(getView(), "Record has been added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Fragment fragment = new FoodDiaryFragement();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
