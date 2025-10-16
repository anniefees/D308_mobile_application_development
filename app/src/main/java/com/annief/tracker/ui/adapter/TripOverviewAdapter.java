package com.annief.tracker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.annief.tracker.R;
import com.annief.tracker.data.entity.Event;
import com.annief.tracker.data.entity.Trip;

import java.util.List;
import java.util.Map;

public class TripOverviewAdapter extends RecyclerView.Adapter<TripOverviewAdapter.ViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    private final List<Trip> trips;
    private final Map<Long, List<Event>> tripEvents;
    private final OnTripClickListener listener;

    public TripOverviewAdapter(List<Trip> trips, Map<Long, List<Event>> tripEvents, OnTripClickListener listener) {
        this.trips = trips;
        this.tripEvents = tripEvents;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_overview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        List<Event> events = tripEvents.get(trip.getId());

        // Set trip info
        holder.tripName.setText(trip.getName());
        holder.lodging.setText("📍 " + trip.getLodging());
        holder.dates.setText("📅 " + trip.getStartDate() + " → " + trip.getEndDate());

        // Clear and populate events
        holder.eventsContainer.removeAllViews();

        if (events != null && !events.isEmpty()) {
            holder.eventsLabel.setVisibility(View.VISIBLE);
            holder.eventsContainer.setVisibility(View.VISIBLE);

            for (Event event : events) {
                TextView eventView = new TextView(holder.itemView.getContext());
                eventView.setText("• " + event.getTitle() + " (" + event.getDate() + ")");
                eventView.setTextSize(14);
                eventView.setPadding(0, 4, 0, 4);
                holder.eventsContainer.addView(eventView);
            }
        } else {
            holder.eventsLabel.setVisibility(View.GONE);
            holder.eventsContainer.setVisibility(View.GONE);
        }

        // Click listener
        holder.cardView.setOnClickListener(v -> listener.onTripClick(trip));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tripName;
        TextView lodging;
        TextView dates;
        TextView eventsLabel;
        LinearLayout eventsContainer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tripName = itemView.findViewById(R.id.tripName);
            lodging = itemView.findViewById(R.id.lodging);
            dates = itemView.findViewById(R.id.dates);
            eventsLabel = itemView.findViewById(R.id.eventsLabel);
            eventsContainer = itemView.findViewById(R.id.eventsContainer);
        }
    }
}