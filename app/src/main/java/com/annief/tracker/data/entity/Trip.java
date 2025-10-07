package com.annief.tracker.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trips")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String tripName;
    private String lodging;
    private String startDate;  // yyyy-MM-dd
    private String endDate;    // yyyy-MM-dd

    public Trip() {}

    public Trip(long id, String tripName, String lodging, String startDate, String endDate) {
        this.id = id;
        this.tripName = tripName;
        this.lodging = lodging;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }

    public String getLodging() { return lodging; }
    public void setLodging(String lodging) { this.lodging = lodging; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}