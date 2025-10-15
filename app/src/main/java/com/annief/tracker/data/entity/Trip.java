package com.annief.tracker.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trips")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String lodging;
    private String startDate;
    private String endDate;

    public Trip() {}

    public Trip(String name, String lodging, String startDate, String endDate) {
        this.name = name;
        this.lodging = lodging;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLodging() { return lodging; }
    public void setLodging(String lodging) { this.lodging = lodging; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}