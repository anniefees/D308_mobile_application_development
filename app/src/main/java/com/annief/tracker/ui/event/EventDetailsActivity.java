package com.annief.tracker.ui.event;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.annief.tracker.R;
import com.annief.tracker.data.entity.Event;
import com.annief.tracker.data.repo.DataRepository;

public class EventDetailsActivity extends AppCompatActivity {
    private DataRepository dataRepository;
    private long tripId;
    private long eventId;
    private EditText title;
    private EditText date;
    private Event current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        dataRepository = new DataRepository(getApplication());
        tripId = getIntent().getLongExtra("tripId", -1);
        eventId = getIntent().getLongExtra("eventId", -1);
        title = findViewById(R.id.eventTitleInput);
        date = findViewById(R.id.eventDateInput);
        Button save = findViewById(R.id.saveEventButton);
        Button delete = findViewById(R.id.deleteEventButton);

        if (eventId != -1) {
            current = dataRepository.getEvent(eventId);
            if (current != null) {
                title.setText(current.getTitle());
                date.setText(current.getDate());
            }
        }

        save.setOnClickListener(v -> {
            String t = title.getText().toString().trim();
            String d = date.getText().toString().trim();

            if (t.isEmpty() || d.isEmpty()) {
                Toast.makeText(this, "Title and date cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                if (current == null) {
                    Event e = new Event(tripId, t, d);
                    dataRepository.insertEvent(e);
                } else {
                    current.setTitle(t);
                    current.setDate(d);
                    dataRepository.updateEvent(current);
                }
                finish();
            } catch (IllegalArgumentException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        delete.setOnClickListener(v -> {
            if (current != null) {
                dataRepository.deleteEvent(current);
                finish();
            }
        });
    }
}
