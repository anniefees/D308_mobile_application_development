package com.annief.tracker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.annief.tracker.R;
import com.annief.tracker.ui.trip.TripListActivity;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openTrips = findViewById(R.id.openTripsButton);
        openTrips.setOnClickListener(v ->
                startActivity(new Intent(this, TripListActivity.class))
        );
    }
}