package com.annief.tracker.ui.trip;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annief.tracker.R;
import com.annief.tracker.data.db.AppDatabase;
import com.annief.tracker.data.entity.Trip;
import com.annief.tracker.ui.adapter.TripAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TripListActivity extends AppCompatActivity implements TripAdapter.OnTripClick {
    private AppDatabase db;
    private TripAdapter adapter;
    private final List<Trip> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        db = AppDatabase.getInstance(this);
        RecyclerView rv = findViewById(R.id.recyclerTrips);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripAdapter(data, this);
        rv.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fabAddTrip);
        fab.setOnClickListener(v -> startActivity(new Intent(this, TripDetailsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        data.clear();
        data.addAll(db.tripDao().getAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent i = new Intent(this, TripDetailsActivity.class);
        i.putExtra("tripId", trip.getId());
        startActivity(i);
    }
}