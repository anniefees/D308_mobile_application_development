package com.annief.tracker.ui.trip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.annief.tracker.R;
import com.annief.tracker.data.db.AppDatabase;
import com.annief.tracker.data.entity.Trip;
import com.annief.tracker.ui.event.EventListActivity;

public class TripDetailsActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText name;
    private EditText lodging;
    private EditText start;
    private EditText end;
    private Trip current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        db = AppDatabase.getInstance(this);
        name = findViewById(R.id.tripNameInput);
        lodging = findViewById(R.id.lodgingInput);
        start = findViewById(R.id.startDateInput);
        end = findViewById(R.id.endDateInput);
        Button save = findViewById(R.id.saveTripButton);
        Button delete = findViewById(R.id.deleteTripButton);
        Button events = findViewById(R.id.eventsButton);

        long tripId = getIntent().getLongExtra("tripId", -1);
        if (tripId != -1) {
            current = db.tripDao().getById(tripId);
            if (current != null) {
                name.setText(current.getName());
                lodging.setText(current.getLodging());
                start.setText(current.getStartDate());
                end.setText(current.getEndDate());
            }
        }

        save.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String l = lodging.getText().toString().trim();
            String s = start.getText().toString().trim();
            String e = end.getText().toString().trim();
            if (current == null) {
                current = new Trip(n, l, s, e);
                long id = db.tripDao().insert(current);
                current.setId(id);
            } else {
                current.setName(n);
                current.setLodging(l);
                current.setStartDate(s);
                current.setEndDate(e);
                db.tripDao().update(current);
            }
            finish();
        });

        delete.setOnClickListener(v -> {
            if (current != null) {
                int count = db.tripDao().countEventsForTrip(current.getId());
                if (count == 0) {
                    db.tripDao().delete(current);
                    finish();
                }
            }
        });

        events.setOnClickListener(v -> {
            if (current != null) {
                Intent i = new Intent(this, EventListActivity.class);
                i.putExtra("tripId", current.getId());
                startActivity(i);
            }
        });
    }
}