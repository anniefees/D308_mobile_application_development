package com.annief.tracker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annief.tracker.R;
import com.annief.tracker.data.entity.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.VH> {
    public interface OnEventClick { void onEventClick(Event e); }

    private final List<Event> items;
    private final OnEventClick listener;

    public EventAdapter(List<Event> items, OnEventClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Event e = items.get(position);
        holder.title.setText(e.getTitle());
        holder.date.setText(e.getDate());
        holder.itemView.setOnClickListener(v -> listener.onEventClick(e));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitle);
            date = itemView.findViewById(R.id.eventDate);
        }
    }
}