package com.example.rural_healthy_mom_to_be.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*
* Model class to set and get all the attributes of a  logged in user
* Consist only constructor, getter and setter
* */
@Entity
public class LoggedinUser {
    @PrimaryKey(autoGenerate = true)
    public int userid;
    @ColumnInfo(name = "username")
    public String username;
    @ColumnInfo(name = "BMIClass")
    public String BMIClass;
    @ColumnInfo(name = "heightInCm")
    public double heightInCm;
    @ColumnInfo(name = "weightBeforePregnancy")
    public double weightBeforePregnancy;
    @ColumnInfo(name = "WeekOfRegister")
    public int WeekOfRegister;
    @ColumnInfo(name = "currentWeek")
    public int currentWeek;
    @ColumnInfo(name = "currentWeight")
    public double currentWeight;



    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public LoggedinUser() {
    }

    public LoggedinUser(int userid, String username, String BMIClass, double heightInCm, double weightBeforePregnancy, int weekOfRegister, int currentWeek, double currentWeight) {
        this.userid = userid;
        this.username = username;
        this.BMIClass = BMIClass;
        this.heightInCm = heightInCm;
        this.weightBeforePregnancy = weightBeforePregnancy;
        this.WeekOfRegister = weekOfRegister;
        this.currentWeek = currentWeek;
        this.currentWeight = currentWeight;
    }

    public LoggedinUser(String username, String BMIClass, double heightInCm, double weightBeforePregnancy, int weekOfRegister, int currentWeek, double currentWeight) {
        this.username = username;
        this.BMIClass = BMIClass;
        this.heightInCm = heightInCm;
        this.weightBeforePregnancy = weightBeforePregnancy;
        this.WeekOfRegister = weekOfRegister;
        this.currentWeek = currentWeek;
        this.currentWeight = currentWeight;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBMIClass() {
        return BMIClass;
    }

    public void setBMIClass(String BMIClass) {
        this.BMIClass = BMIClass;
    }

    public double getHeightInCm() {
        return heightInCm;
    }

    public void setHeightInCm(double heightInCm) {
        this.heightInCm = heightInCm;
    }

    public double getWeightBeforePregnancy() {
        return weightBeforePregnancy;
    }

    public void setWeightBeforePregnancy(double weightBeforePregnancy) {
        this.weightBeforePregnancy = weightBeforePregnancy;
    }

    public int getWeekOfRegister() {
        return WeekOfRegister;
    }

    public void setWeekOfRegister(int weekOfRegister) {
        WeekOfRegister = weekOfRegister;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }
}
