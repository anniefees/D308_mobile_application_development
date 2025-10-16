package com.annief.tracker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annief.tracker.R;
import com.annief.tracker.data.db.AppDatabase;
import com.annief.tracker.data.entity.Event;
import com.annief.tracker.data.entity.Trip;
import com.annief.tracker.ui.adapter.TripOverviewAdapter;
import com.annief.tracker.ui.trip.TripDetailsActivity;
import com.annief.tracker.ui.trip.TripListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private RecyclerView recyclerView;
    private TripOverviewAdapter adapter;
    private View emptyView;
    private final List<Trip> trips = new ArrayList<>();
    private final Map<Long, List<Event>> tripEvents = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        recyclerView = findViewById(R.id.recyclerOverview);
        emptyView = findViewById(R.id.emptyView);
        FloatingActionButton fab = findViewById(R.id.fabAddTrip);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripOverviewAdapter(trips, tripEvents, new TripOverviewAdapter.OnTripClickListener() {
            @Override
            public void onTripClick(Trip trip) {
                Intent i = new Intent(MainActivity.this, TripDetailsActivity.class);
                i.putExtra("tripId", trip.getId());
                startActivity(i);
            }
        });
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, TripListActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        trips.clear();
        tripEvents.clear();

        List<Trip> allTrips = db.tripDao().getAll();
        trips.addAll(allTrips);

        // Load events for each trip
        for (Trip trip : allTrips) {
            List<Event> events = db.eventDao().getByTrip(trip.getId());
            tripEvents.put(trip.getId(), events);
        }

        // Show empty state or data
        if (trips.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
    }
}