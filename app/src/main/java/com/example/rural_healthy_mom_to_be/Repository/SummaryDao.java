package com.example.rural_healthy_mom_to_be.Repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.rural_healthy_mom_to_be.Model.Summary;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SummaryDao {

    @Query("SELECT * FROM Summary")
    List<Summary> getAll();

    @Query("SELECT * FROM Summary WHERE uid = :id LIMIT 1")
    Summary findByID(int id);

    //2010-10-10 00:00:00
    @Query("SELECT * FROM Summary WHERE strftime('%d/%m/%Y',time) = :date")
    List<Summary> findByDate(String date);

    @Insert
    void insertAll(Summary... summaries);

    @Insert
    long insert(Summary summary);

    @Delete
    void delete(Summary summary);

    @Update(onConflict = REPLACE)
    public void updateSum(Summary... summaries);

    @Query("DELETE FROM Summary")
    void deleteAll();
}
