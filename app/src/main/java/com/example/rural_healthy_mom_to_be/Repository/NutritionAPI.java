package com.example.rural_healthy_mom_to_be.Repository;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NutritionAPI {
    private static final String API_KEY = "RQHAUOGvrQaZFz6yEGnoLKCNaiS2eO0ZcOWncfa2";

    public static String getNDBNO(String keyword) {



        URL url = null;

        HttpURLConnection connection = null;
        String textResult = "";

        try {
            url = new URL("https://api.nal.usda.gov/ndb/search/?format=json&q="+keyword+"&sort=r&max=25&offset=0&api_key="+API_KEY);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }


    public static String getData(String nbdno) {

        URL url = null;

        HttpURLConnection connection = null;
        String textResult = "";

        try {
            url = new URL("https://api.nal.usda.gov/ndb/reports/?ndbno="+nbdno+"&type=f&format=json&api_key="+API_KEY);
            //https://api.nal.usda.gov/ndb/reports/?ndbno=01009&type=f&format=json&api_key=RQHAUOGvrQaZFz6yEGnoLKCNaiS2eO0ZcOWncfa2
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }


    //get nutrient from json response
    public static String getNutrients(String result) {
        String snippet = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if (jsonArray != null && jsonArray.length() > 0) {
                snippet = jsonArray.getJSONObject(0).getString("snippet");
                snippet = snippet.replaceAll("[...]","");

            }
        } catch (Exception e) {
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

}

