package com.annief.tracker.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "events", indices = @Index("tripId"))
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long tripId;
    private String eventTitle;
    private String eventDate;

    public Event() {}

    public Event(long id, long tripId, String eventTitle, String eventDate) {
        this.id = id;
        this.tripId = tripId;
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTripId() { return tripId; }
    public void setTripId(long tripId) { this.tripId = tripId; }
    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
}