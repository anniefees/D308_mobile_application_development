package com.annief.tracker.data.repo;

import android.content.Context;
import com.annief.tracker.data.dao.EventDao;
import com.annief.tracker.data.dao.TripDao;
import com.annief.tracker.data.db.AppDatabase;
import com.annief.tracker.data.entity.Event;
import com.annief.tracker.data.entity.Trip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataRepository {

    private final TripDao tripDao;
    private final EventDao eventDao;
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DataRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.tripDao = db.tripDao();
        this.eventDao = db.eventDao();
    }

    // Trip operations
    public List<Trip> getAllTrips() {
        return tripDao.getAll();
    }

    public Trip getTrip(long id) {
        return tripDao.findById(id);
    }

    public long insertTrip(Trip trip) {
        validateTrip(trip);
        return tripDao.insert(trip);
    }

    public int updateTrip(Trip trip) {
        validateTrip(trip);
        return tripDao.update(trip);
    }

    public int deleteTripIfNoEvents(Trip trip) {
        int eventCount = tripDao.countEventsForTrip(trip.getId());
        if (eventCount == 0) {
            return tripDao.delete(trip);
        }
        return 0; // Indicates deletion was blocked
    }

    // Event operations
    public List<Event> getEventsForTrip(long tripId) {
        return eventDao.getForTrip(tripId);
    }

    public long insertEvent(Event event) {
        validateEvent(event);
        return eventDao.insert(event);
    }

    public int updateEvent(Event event) {
        validateEvent(event);
        return eventDao.update(event);
    }

    public int deleteEvent(Event event) {
        return eventDao.delete(event);
    }

    public Event getEvent(long id) {
        return eventDao.findById(id);
    }

    // Validation logic
    private void validateTrip(Trip trip) {
        LocalDate startDate = LocalDate.parse(trip.getStartDate(), ISO_DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(trip.getEndDate(), ISO_DATE_FORMATTER);
        if (!endDate.isAfter(startDate) && !endDate.isEqual(startDate)) {
            throw new IllegalArgumentException("End date must be on or after the start date.");
        }
    }

    private void validateEvent(Event event) {
        Trip parentTrip = getTrip(event.getTripId());
        LocalDate eventDate = LocalDate.parse(event.getDate(), ISO_DATE_FORMATTER);
        LocalDate tripStartDate = LocalDate.parse(parentTrip.getStartDate(), ISO_DATE_FORMATTER);
        LocalDate tripEndDate = LocalDate.parse(parentTrip.getEndDate(), ISO_DATE_FORMATTER);

        if (eventDate.isBefore(tripStartDate) || eventDate.isAfter(tripEndDate)) {
            throw new IllegalArgumentException("Event date must be within the trip's start and end dates.");
        }
    }
}
