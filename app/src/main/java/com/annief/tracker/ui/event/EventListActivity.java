package com.annief.tracker.ui.event;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annief.tracker.R;
import com.annief.tracker.data.db.AppDatabase;
import com.annief.tracker.data.entity.Event;
import com.annief.tracker.ui.adapter.EventAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity implements EventAdapter.OnEventClick {
    private AppDatabase db;
    private long tripId;
    private final List<Event> data = new ArrayList<>();
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        db = AppDatabase.getInstance(this);
        tripId = getIntent().getLongExtra("tripId", -1);
        RecyclerView rv = findViewById(R.id.recyclerEvents);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(data, this);
        rv.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fabAddEvent);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, EventDetailsActivity.class);
            i.putExtra("tripId", tripId);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        data.clear();
        data.addAll(db.eventDao().getByTrip(tripId));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEventClick(Event e) {
        Intent i = new Intent(this, EventDetailsActivity.class);
        i.putExtra("tripId", tripId);
        i.putExtra("eventId", e.getId());
        startActivity(i);
    }
}