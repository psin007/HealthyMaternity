package com.example.rural_healthy_mom_to_be.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.Food;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.NutritionAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFoodInDiaryFragment extends Fragment {
    View vFoodAdd;
    String foodToSearch;
    Food food;
    TextView tvCaloriesFacts;
    TextView tvFatFacts;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vFoodAdd = inflater.inflate(R.layout.add_a_meal, container, false);
        tvCaloriesFacts = vFoodAdd.findViewById(R.id.tv_cal_values);
        tvFatFacts = vFoodAdd.findViewById(R.id.tv_fat_values);
        new GetNDBNumber().execute("pasta");
        return vFoodAdd;

    }
    class GetNDBNumber extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return NutritionAPI.getNDBNO(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonobject = new JSONObject(result);

                JSONArray jsonArray= jsonobject.getJSONObject("list").getJSONArray("item");
                String nbdno=jsonArray.getJSONObject(0).getString("ndbno");
                GetNutrientData getNutrientData = new GetNutrientData();
                getNutrientData.execute(nbdno);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    class GetNutrientData extends AsyncTask<String,Void,String> {

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
        String fatFact="";
        String calFact="";
        try {
            food = new Food();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients");
            for(int i = 0;i<jsonArray.length();i++){
                String nutrientId = jsonArray.getJSONObject(i).getString("nutrient_id");
                if(nutrientId.equals("204")){
                    fat = Double.parseDouble(jsonArray.getJSONObject(i) .getString("value"));
                    JSONArray measuresArray = jsonArray.getJSONObject(i).getJSONArray("measures");
                    servingAmount = measuresArray.getJSONObject(0).getDouble("qty");
                    servingUnit = measuresArray.getJSONObject(0).getString("label");
                    food.setFat(fat);
                    food.setServingunit(servingUnit);
                    fatFact = "\nFat - "+fat;

                }
                if(nutrientId.equals("208")){
                    calories = Double.parseDouble(jsonArray.getJSONObject(i) .getString("value"));
                    food.setCalorieamount(calories);
                    calFact = "\nCalories -"+calories;
                }
            }
            String NutrientFact = "Nutrient facts:"+fatFact+calFact;
            tvCaloriesFacts.setText(food.getCalorieamount()+"");
            tvFatFacts.setText(food.getFat()+"");

            //set nutrientTv values

        }

        catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
