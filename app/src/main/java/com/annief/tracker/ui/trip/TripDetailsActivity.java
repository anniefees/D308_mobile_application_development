package com.annief.tracker.ui.trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.annief.tracker.R;
import com.annief.tracker.data.db.AppDatabase;
import com.annief.tracker.data.entity.Trip;
import com.annief.tracker.data.repo.DataRepository;
import com.annief.tracker.ui.event.EventListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TripDetailsActivity extends AppCompatActivity {
    private AppDatabase db;
    private DataRepository repo;
    private EditText name;
    private EditText lodging;
    private EditText start;
    private EditText end;
    private Trip current;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        db = AppDatabase.getInstance(this);
        repo = new DataRepository(this);

        name = findViewById(R.id.tripNameInput);
        lodging = findViewById(R.id.lodgingInput);
        start = findViewById(R.id.startDateInput);
        end = findViewById(R.id.endDateInput);
        Button save = findViewById(R.id.saveTripButton);
        Button delete = findViewById(R.id.deleteTripButton);
        Button events = findViewById(R.id.eventsButton);
        Button share = findViewById(R.id.shareTripButton);

        // Debug: Check if share button exists
        if (share == null) {
            showToast("Share button not found - check XML layout");
        }

        // Make date fields non-editable and clickable
        start.setFocusable(false);
        start.setClickable(true);
        end.setFocusable(false);
        end.setClickable(true);

        // Set up date pickers
        start.setOnClickListener(v -> showStartDatePicker());
        end.setOnClickListener(v -> showEndDatePicker());

        long tripId = getIntent().getLongExtra("tripId", -1);
        if (tripId != -1) {
            current = db.tripDao().getById(tripId);
            if (current != null) {
                name.setText(current.getName());
                lodging.setText(current.getLodging());
                start.setText(current.getStartDate());
                end.setText(current.getEndDate());

                // Parse existing dates into calendars
                try {
                    if (current.getStartDate() != null && !current.getStartDate().isEmpty()) {
                        startCalendar.setTime(DATE_FORMAT.parse(current.getStartDate()));
                    }
                    if (current.getEndDate() != null && !current.getEndDate().isEmpty()) {
                        endCalendar.setTime(DATE_FORMAT.parse(current.getEndDate()));
                    }
                } catch (Exception e) {
                    // Use current date as default
                }
            }
        }

        save.setOnClickListener(v -> onSave());
        delete.setOnClickListener(v -> onDelete());
        events.setOnClickListener(v -> onViewEvents());

        if (share != null) {
            share.setOnClickListener(v -> onShare());
        }
    }

    private void showStartDatePicker() {
        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    startCalendar.set(year, month, dayOfMonth);
                    String dateStr = DATE_FORMAT.format(startCalendar.getTime());
                    start.setText(dateStr);

                    // If end date is before start date, update end date
                    if (endCalendar.before(startCalendar)) {
                        endCalendar.setTime(startCalendar.getTime());
                        end.setText(dateStr);
                    }
                },
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
        );
        picker.show();
    }

    private void showEndDatePicker() {
        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    endCalendar.set(year, month, dayOfMonth);

                    // Validate end date is not before start date
                    if (endCalendar.before(startCalendar)) {
                        showToast("End date cannot be before start date");
                        endCalendar.setTime(startCalendar.getTime());
                    }

                    String dateStr = DATE_FORMAT.format(endCalendar.getTime());
                    end.setText(dateStr);
                },
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to start date
        picker.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
        picker.show();
    }

    private void onSave() {
        String n = name.getText().toString().trim();
        String l = lodging.getText().toString().trim();
        String s = start.getText().toString().trim();
        String e = end.getText().toString().trim();

        // Validate all fields are filled
        if (n.isEmpty()) {
            showToast("Trip name is required");
            name.requestFocus();
            return;
        }

        if (l.isEmpty()) {
            showToast("Lodging is required");
            lodging.requestFocus();
            return;
        }

        if (s.isEmpty()) {
            showToast("Please select a start date");
            start.performClick();
            return;
        }

        if (e.isEmpty()) {
            showToast("Please select an end date");
            end.performClick();
            return;
        }

        // Save the trip using repository
        try {
            if (current == null) {
                current = new Trip(n, l, s, e);
                long id = repo.insertTrip(current);
                current.setId(id);
                showToast("Trip saved successfully");
            } else {
                current.setName(n);
                current.setLodging(l);
                current.setStartDate(s);
                current.setEndDate(e);
                repo.updateTrip(current);
                showToast("Trip updated successfully");
            }
            finish();
        } catch (Exception ex) {
            showToast("Error: " + ex.getMessage());
        }
    }

    private void onDelete() {
        if (current == null) {
            finish();
            return;
        }

        int count = db.tripDao().countEventsForTrip(current.getId());
        if (count > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Cannot Delete Trip")
                    .setMessage("This trip has " + count + " event(s). Please delete all events first.")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Trip")
                    .setMessage("Delete '" + current.getName() + "'?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.tripDao().delete(current);
                        showToast("Trip deleted");
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void onViewEvents() {
        if (current == null) {
            showToast("Please save the trip first");
            return;
        }
        Intent i = new Intent(this, EventListActivity.class);
        i.putExtra("tripId", current.getId());
        startActivity(i);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void onShare() {
        if (current == null) {
            showToast("Please save the trip first");
            return;
        }

        // Build the share message with all trip details
        StringBuilder shareText = new StringBuilder();
        shareText.append("Trip Details\n");
        shareText.append("═══════════════\n\n");
        shareText.append("Trip: ").append(current.getName()).append("\n");
        shareText.append("Lodging: ").append(current.getLodging()).append("\n");
        shareText.append("Start Date: ").append(current.getStartDate()).append("\n");
        shareText.append("End Date: ").append(current.getEndDate()).append("\n");

        // Add events if any - use DAO directly since we allow main thread queries
        java.util.List<com.annief.tracker.data.entity.Event> eventList =
                db.eventDao().getByTrip(current.getId());

        // Debug: show event count
        showToast("Found " + (eventList != null ? eventList.size() : 0) + " events");

        if (eventList != null && !eventList.isEmpty()) {
            shareText.append("\nEvents:\n");
            shareText.append("───────────────\n");
            for (com.annief.tracker.data.entity.Event event : eventList) {
                shareText.append("• ").append(event.getTitle())
                        .append(" (").append(event.getDate()).append(")\n");
            }
        } else {
            shareText.append("\nNo events scheduled\n");
        }

        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Trip: " + current.getName());
        shareText.append("\n\nShared from TripTracker");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        // Show chooser to let user pick app (Email, SMS, Clipboard, etc.)
        startActivity(Intent.createChooser(shareIntent, "Share trip via..."));
    }
}