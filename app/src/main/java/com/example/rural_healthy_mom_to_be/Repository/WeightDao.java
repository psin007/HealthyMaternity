package com.example.rural_healthy_mom_to_be.Repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.rural_healthy_mom_to_be.Model.Weight;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
/*
 * DAO class to call database Weight
 * */
@Dao
public interface WeightDao {

    @Query("SELECT * FROM Weight")
    List<Weight> getAll();

    @Query("SELECT * FROM Weight WHERE uid = :id LIMIT 1")
    Weight findByID(int id);

    @Insert
    void insertAll(Weight... weights);

    @Insert
    long insert(Weight weight);

    @Delete
    void delete(Weight weight);

    @Update(onConflict = REPLACE)
    public void updateUsers(Weight... weights);

    @Query("DELETE FROM Weight")
    void deleteAll();

}
