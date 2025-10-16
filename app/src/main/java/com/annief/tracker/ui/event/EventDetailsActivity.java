package com.annief.tracker.ui.event;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.annief.tracker.R;
import com.annief.tracker.data.entity.Event;
import com.annief.tracker.data.entity.Trip;
import com.annief.tracker.data.repo.DataRepository;
import com.annief.tracker.util.AlertHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {
    private DataRepository repo;
    private long tripId;
    private long eventId;
    private EditText title;
    private EditText date;
    private Event current;
    private Trip parentTrip;
    private Calendar eventCalendar = Calendar.getInstance();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        repo = new DataRepository(getApplication());

        tripId = getIntent().getLongExtra("tripId", -1);
        eventId = getIntent().getLongExtra("eventId", -1);

        title = findViewById(R.id.eventTitleInput);
        date = findViewById(R.id.eventDateInput);
        Button save = findViewById(R.id.saveEventButton);
        Button delete = findViewById(R.id.deleteEventButton);
        Button setAlert = findViewById(R.id.setEventAlertButton);

        // Make date field non-editable and clickable
        date.setFocusable(false);
        date.setClickable(true);
        date.setOnClickListener(v -> showDatePicker());

        // Load parent trip to get date range
        if (tripId != -1) {
            parentTrip = repo.getTrip(tripId);
        }

        // Load existing event if editing
        if (eventId != -1) {
            current = repo.getEvent(eventId);
            if (current != null) {
                title.setText(current.getTitle());
                date.setText(current.getDate());

                // Parse existing date into calendar
                try {
                    if (current.getDate() != null && !current.getDate().isEmpty()) {
                        eventCalendar.setTime(DATE_FORMAT.parse(current.getDate()));
                    }
                } catch (Exception e) {
                    // Use current date as default
                }
            }
        }

        save.setOnClickListener(v -> onSave());
        delete.setOnClickListener(v -> onDelete());
        setAlert.setOnClickListener(v -> onSetAlert());
    }

    private void showDatePicker() {
        if (parentTrip == null) {
            showToast("Error: Parent trip not found");
            return;
        }

        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    eventCalendar.set(year, month, dayOfMonth);
                    String dateStr = DATE_FORMAT.format(eventCalendar.getTime());
                    date.setText(dateStr);
                },
                eventCalendar.get(Calendar.YEAR),
                eventCalendar.get(Calendar.MONTH),
                eventCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set date range limits based on parent trip
        try {
            Calendar minDate = Calendar.getInstance();
            Calendar maxDate = Calendar.getInstance();
            minDate.setTime(DATE_FORMAT.parse(parentTrip.getStartDate()));
            maxDate.setTime(DATE_FORMAT.parse(parentTrip.getEndDate()));

            picker.getDatePicker().setMinDate(minDate.getTimeInMillis());
            picker.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        } catch (Exception e) {
            showToast("Error setting date range");
        }

        picker.show();
    }

    private void onSave() {
        String t = title.getText().toString().trim();
        String d = date.getText().toString().trim();

        if (t.isEmpty()) {
            showToast("Event title is required");
            title.requestFocus();
            return;
        }

        if (d.isEmpty()) {
            showToast("Please select an event date");
            date.performClick();
            return;
        }

        try {
            if (current == null) {
                Event e = new Event(tripId, t, d);
                repo.insertEvent(e);
                showToast("Event saved");
            } else {
                current.setTitle(t);
                current.setDate(d);
                repo.updateEvent(current);
                showToast("Event updated");
            }
            finish();
        } catch (IllegalArgumentException ex) {
            showToast("Error: " + ex.getMessage());
        }
    }

    private void onDelete() {
        if (current == null) {
            finish();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Delete '" + current.getTitle() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repo.deleteEvent(current);
                    showToast("Event deleted");
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void onSetAlert() {
        if (current == null || current.getDate() == null || current.getDate().isEmpty()) {
            showToast("Please save the event with a date first");
            return;
        }
        AlertHelper.scheduleEventAlert(this, current.getId(), current.getTitle(), current.getDate());
    }
}