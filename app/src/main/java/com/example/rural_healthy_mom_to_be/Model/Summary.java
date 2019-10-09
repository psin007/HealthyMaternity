package com.example.rural_healthy_mom_to_be.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Summary {
    @PrimaryKey(autoGenerate = true)
    public int sid;
    @ForeignKey(entity = LoggedinUser.class,
            parentColumns = "userid",
            childColumns = "uid",
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "uid")
    public int uid;
    @ColumnInfo(name = "foodname")
    public String foodname;
    @ColumnInfo(name = "quantity")
    public int quantity;
    @ColumnInfo(name = "calories")
    public Double calories;
    @ColumnInfo(name = "sugar")
    public Double sugar;
    @ColumnInfo(name = "protein")
    public Double protein;
    @ColumnInfo(name = "fat")
    public Double fat;
    @ColumnInfo(name = "calcium")
    public Double calcium;
    @ColumnInfo(name = "energy")
    public Double energy;
    @ColumnInfo(name = "iron")
    public Double iron;
    @ColumnInfo(name = "cholesterol")
    public Double cholesterol;
    @ColumnInfo(name = "time")
    public String time;

//    public Summary(int uid, String foodname, int quantity, Double calories, Double sugar,
//                   Double protein, Double fat, Double calcium, Double energy, Double iron,
//                   Double cholesterol, String time) {
//        this.uid = uid;
//        this.foodname = foodname;
//        this.quantity = quantity;
//        this.calories = calories;
//        this.sugar = sugar;
//        this.protein = protein;
//        this.fat = fat;
//        this.calcium = calcium;
//        this.energy = energy;
//        this.iron = iron;
//        this.cholesterol = cholesterol;
//        this.time = time;
//    }
//updated one; without calories


    public Summary(int uid, String foodname, int quantity, Double calories,Double fat) {
        this.uid = uid;
        this.foodname = foodname;
        this.quantity = quantity;
        this.calories = calories;
        this.fat = fat;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCalcium() {
        return calcium;
    }

    public void setCalcium(Double calcium) {
        this.calcium = calcium;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getIron() {
        return iron;
    }

    public void setIron(Double iron) {
        this.iron = iron;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
