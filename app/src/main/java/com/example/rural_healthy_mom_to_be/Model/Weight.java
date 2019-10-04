package com.example.rural_healthy_mom_to_be.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Weight {
    @PrimaryKey(autoGenerate = true)
    public int wid;
    @ForeignKey(entity = LoggedinUser.class,
        parentColumns = "userid",
        childColumns = "uid",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "uid")
    public int uid;
    @ColumnInfo(name = "weight")
    public int weight;
    @ColumnInfo(name = "week")
    public int week;

    public Weight() {
    }


    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
