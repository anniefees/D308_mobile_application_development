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
    private DataRepository repo; private long tripId; private long eventId = -1;
    private EditText title, date;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_event_details);
        repo = new DataRepository(this);

        title = findViewById(R.id.eventTitleInput);
        date = findViewById(R.id.eventDateInput);

        tripId = getIntent().getLongExtra("tripId", -1);
        eventId = getIntent().getLongExtra("eventId", -1);

        if (eventId != -1) {
            Event e = repo.getEventsForTrip(tripId).stream().filter(ev -> ev.getId() == eventId).findFirst().orElse(null);
            if (e != null) {
                title.setText(e.getEventTitle());
                date.setText(e.getEventDate());
            }
        }

        Button save = findViewById(R.id.saveEventButton);
        Button delete = findViewById(R.id.deleteEventButton);
        save.setOnClickListener(v -> onSave());
        delete.setOnClickListener(v -> onDelete());
    }

    private void onSave(){
        String t = title.getText().toString().trim();
        String d = date.getText().toString().trim();
        if (t.isEmpty() || d.isEmpty()) { toast("All fields required"); return; }
        if (!d.matches("\\d{4}-\\d{2}-\\d{2}")) { toast("Use yyyy-MM-dd"); return; }
        Event e = new Event(eventId, tripId, t, d);
        try {
            if (eventId == -1) { eventId = repo.insertEventValidated(e); toast("Saved"); }
            else { repo.updateEventValidated(e); toast("Updated"); }
            finish();
        } catch (Exception ex) { toast(ex.getMessage()); }
    }

    private void onDelete(){
        if (eventId == -1) { finish(); return; }
        Event e = new Event(eventId, tripId, title.getText().toString(), date.getText().toString());
        repo.deleteEvent(e);
        finish();
    }

    private void toast(String s){ Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
}