package com.annief.tracker.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.annief.tracker.data.dao.EventDao;
import com.annief.tracker.data.dao.TripDao;
import com.annief.tracker.data.entity.Event;
import com.annief.tracker.data.entity.Trip;

@Database(entities = {Trip.class, Event.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract TripDao tripDao();
    public abstract EventDao eventDao();

    public static AppDatabase getInstance(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            ctx.getApplicationContext(),
                            AppDatabase.class,
                            "triptracker.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}