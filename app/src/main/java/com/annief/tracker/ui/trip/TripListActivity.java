package com.annief.tracker.ui.trip;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.annief.tracker.R;
import com.annief.tracker.data.entity.Trip;
import com.annief.tracker.data.repo.DataRepository;
import com.annief.tracker.ui.adapter.TripAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class TripListActivity extends AppCompatActivity {
    private final List<Trip> items = new ArrayList<>();
    private TripAdapter adapter; private DataRepository repo;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_trip_list);
        repo = new DataRepository(this);

        RecyclerView rv = findViewById(R.id.tripRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripAdapter(items, t -> {
            Intent i = new Intent(this, TripDetailsActivity.class);
            i.putExtra("tripId", t.getId());
            startActivity(i);
        });
        rv.setAdapter(adapter);

        ((FloatingActionButton)findViewById(R.id.addTripFab)).setOnClickListener(v ->
                startActivity(new Intent(this, TripDetailsActivity.class))
        );
    }

    @Override protected void onResume() {
        super.onResume();
        items.clear();
        items.addAll(repo.getAllTrips());
        adapter.notifyDataSetChanged();
    }
}