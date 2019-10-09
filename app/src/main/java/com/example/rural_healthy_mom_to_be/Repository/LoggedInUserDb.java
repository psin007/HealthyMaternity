package com.example.rural_healthy_mom_to_be.Repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.Model.Summary;
import com.example.rural_healthy_mom_to_be.Model.Weight;

@Database(entities = {LoggedinUser.class, Weight.class, Summary.class}, version = 6, exportSchema = false)
public abstract class LoggedInUserDb extends RoomDatabase {
    public abstract LoggedInUserDao loggedInUserDao();
    public abstract WeightDao weightDao();
    public abstract SummaryDao summaryDao();
    private static volatile LoggedInUserDb INSTANCE;
    static LoggedInUserDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LoggedInUserDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LoggedInUserDb.class, "LoggedInUserDb").build();
                }
            }
        }
        return INSTANCE;
    }
}
