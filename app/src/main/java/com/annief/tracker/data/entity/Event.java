package com.annief.tracker.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "events",
        foreignKeys = @ForeignKey(entity = Trip.class,
                parentColumns = "id",
                childColumns = "tripId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("tripId")})
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long tripId;
    private String title;
    private String date;

    public Event() {}

    public Event(long tripId, String title, String date) {
        this.tripId = tripId;
        this.title = title;
        this.date = date;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTripId() { return tripId; }
    public void setTripId(long tripId) { this.tripId = tripId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}