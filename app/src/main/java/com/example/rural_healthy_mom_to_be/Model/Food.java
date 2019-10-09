package com.example.rural_healthy_mom_to_be.Model;


import java.util.Collection;

public class Food {
    private Integer foodid;
    private String name;
    private Double calorieamount;
    private String servingunit;
    private Double fat;

    public Food() {
    }

    public Food(Integer foodid, String name,  Double calorieamount, String servingunit,  Double fat) {
        this.foodid = foodid;
        this.name = name;
        this.calorieamount = calorieamount;
        this.servingunit = servingunit;
        this.fat = fat;
    }

    public Food(Integer foodid, String name, String category, Double calorieamount, String servingunit, Double servingamount, Double fat) {
        this.foodid = foodid;
        this.name = name;
        this.calorieamount = calorieamount;
        this.servingunit = servingunit;

        this.fat = fat;
    }

    public Integer getFoodid() {
        return foodid;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Double getCalorieamount() {
        return calorieamount;
    }

    public void setCalorieamount(Double calorieamount) {
        this.calorieamount = calorieamount;
    }

    public String getServingunit() {
        return servingunit;
    }

    public void setServingunit(String servingunit) {
        this.servingunit = servingunit;
    }



    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

}
