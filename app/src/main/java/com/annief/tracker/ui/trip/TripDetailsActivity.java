package com.annief.tracker.ui.trip;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.annief.tracker.R;
import com.annief.tracker.data.entity.Trip;
import com.annief.tracker.data.repo.DataRepository;

public class TripDetailsActivity extends AppCompatActivity {
    private DataRepository repo; private long tripId = -1;
    private EditText name, lodging, start, end;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_trip_details);
        repo = new DataRepository(this);
        name = findViewById(R.id.tripNameInput);
        lodging = findViewById(R.id.lodgingInput);
        start = findViewById(R.id.startDateInput);
        end = findViewById(R.id.endDateInput);

        tripId = getIntent().getLongExtra("tripId", -1);
        if (tripId != -1) {
            Trip t = repo.getTrip(tripId);
            if (t != null) {
                name.setText(t.getTripName());
                lodging.setText(t.getLodging());
                start.setText(t.getStartDate());
                end.setText(t.getEndDate());
            }
        }

        Button save = findViewById(R.id.saveTripButton);
        Button delete = findViewById(R.id.deleteTripButton);
        save.setOnClickListener(v -> onSave());
        delete.setOnClickListener(v -> onDelete());
    }

    private void onSave(){
        String n = name.getText().toString().trim();
        String l = lodging.getText().toString().trim();
        String s = start.getText().toString().trim();
        String e = end.getText().toString().trim();
        if (n.isEmpty() || l.isEmpty() || s.isEmpty() || e.isEmpty()) { toast("All fields required"); return; }
        Trip t = new Trip(tripId, n, l, s, e);
        try {
            if (tripId == -1) {
                tripId = repo.insertTrip(t);
                toast("Saved");
            } else {
                repo.updateTrip(t);
                toast("Updated");
            }
            finish();
        } catch (Exception ex) {
            toast(ex.getMessage());
        }
    }

    private void onDelete(){
        if (tripId == -1) { finish(); return; }
        Trip t = repo.getTrip(tripId);
        int result = repo.deleteTripIfNoEvents(t);
        if (result == 0) toast("Cannot delete: events exist"); else finish();
    }

    private void toast(String s){ Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
}