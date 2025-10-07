package com.annief.tracker.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.annief.tracker.data.entity.Trip;

import java.util.List;

@Dao
public interface TripDao {

    @Query("SELECT * FROM trips ORDER BY startDate")
    List<Trip> getAll();

    @Query("SELECT * FROM trips WHERE id = :id LIMIT 1")
    Trip findById(long id);

    @Insert
    long insert(Trip trip);

    @Update
    int update(Trip trip);

    @Delete
    int delete(Trip trip);

    // used to block deleting a trip that still has events (we'll add events table next)
    @Query("SELECT COUNT(*) FROM events WHERE tripId = :tripId")
    int countEventsForTrip(long tripId);
}