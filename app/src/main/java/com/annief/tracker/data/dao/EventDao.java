package com.annief.tracker.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.annief.tracker.data.entity.Event;
import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events WHERE tripId = :tripId ORDER BY eventDate")
    List<Event> getForTrip(long tripId);

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    Event findById(long id);

    @Insert
    long insert(Event e);

    @Update
    int update(Event e);

    @Delete
    int delete(Event e);
}