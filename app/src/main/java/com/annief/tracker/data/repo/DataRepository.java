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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataRepository {
    private final TripDao tripDao;
    private final EventDao eventDao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DataRepository(Context ctx) {
        AppDatabase db = AppDatabase.getInstance(ctx);
        this.tripDao = db.tripDao();
        this.eventDao = db.eventDao();
    }

    private <T> T run(Callable<T> task) {
        try { return io.submit(task).get(); } catch (Exception e) { throw new RuntimeException(e); }
    }

    public List<Trip> getAllTrips() { return run(tripDao::getAll); }
    public Trip getTrip(long id) { return run(() -> tripDao.findById(id)); }
    public long insertTrip(Trip t) { validateTrip(t); return run(() -> tripDao.insert(t)); }
    public int updateTrip(Trip t) { validateTrip(t); return run(() -> tripDao.update(t)); }
    public int deleteTripIfNoEvents(Trip t) { return run(() -> tripDao.countEventsForTrip(t.getId()) > 0 ? 0 : tripDao.delete(t)); }

    public List<Event> getEventsForTrip(long tripId) { return run(() -> eventDao.getForTrip(tripId)); }
    public long insertEventValidated(Event e) { validateEvent(e); return run(() -> eventDao.insert(e)); }
    public int updateEventValidated(Event e) { validateEvent(e); return run(() -> eventDao.update(e)); }
    public int deleteEvent(Event e) { return run(() -> eventDao.delete(e)); }
    public Event getEvent(long id) { return run(() -> eventDao.findById(id)); }
    private void validateTrip(Trip t) {
        LocalDate s = LocalDate.parse(t.getStartDate(), ISO);
        LocalDate e = LocalDate.parse(t.getEndDate(), ISO);
        if (!e.isAfter(s)) throw new IllegalArgumentException("End date must be after start date");
    }

    private void validateEvent(Event ev) {
        Trip parent = getTrip(ev.getTripId());
        LocalDate d = LocalDate.parse(ev.getEventDate(), ISO);
        LocalDate s = LocalDate.parse(parent.getStartDate(), ISO);
        LocalDate e = LocalDate.parse(parent.getEndDate(), ISO);
        if (d.isBefore(s) || d.isAfter(e)) throw new IllegalArgumentException("Event date must be within trip");
    }
}