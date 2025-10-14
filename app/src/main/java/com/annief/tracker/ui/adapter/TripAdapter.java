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
    public interface OnClick { void onClick(Trip t); }
    private final List<Trip> items; private final OnClick onClick;
    public TripAdapter(List<Trip> items, OnClick onClick) { this.items = items; this.onClick = onClick; }
    static class VH extends RecyclerView.ViewHolder {
        TextView name, dates, lodging;
        VH(View v){ super(v); name=v.findViewById(R.id.tripNameText); dates=v.findViewById(R.id.tripDatesText); lodging=v.findViewById(R.id.lodgingText); }
    }
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vType){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_trip, p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h, int i){
        Trip t = items.get(i);
        h.name.setText(t.getTripName());
        h.dates.setText(t.getStartDate() + " \u2192 " + t.getEndDate());
        h.lodging.setText(t.getLodging());
        h.itemView.setOnClickListener(v -> onClick.onClick(t));
    }
    @Override public int getItemCount(){ return items.size(); }
}