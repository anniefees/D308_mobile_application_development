package com.annief.tracker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annief.tracker.R;
import com.annief.tracker.data.entity.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.VH> {
    public interface OnTripClick { void onTripClick(Trip trip); }

    private final List<Trip> items;
    private final OnTripClick listener;

    public TripAdapter(List<Trip> items, OnTripClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Trip t = items.get(position);
        holder.title.setText(t.getName());
        String s = t.getStartDate() == null ? "" : t.getStartDate();
        String e = t.getEndDate() == null ? "" : t.getEndDate();
        holder.subtitle.setText(s + " - " + e);
        holder.itemView.setOnClickListener(v -> listener.onTripClick(t));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tripTitle);
            subtitle = itemView.findViewById(R.id.tripDates);
        }
    }
}