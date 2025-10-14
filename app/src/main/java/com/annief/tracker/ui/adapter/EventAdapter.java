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
    public interface OnClick { void onClick(Event e); }
    private final List<Event> items; private final OnClick onClick;
    public EventAdapter(List<Event> items, OnClick onClick) { this.items = items; this.onClick = onClick; }
    static class VH extends RecyclerView.ViewHolder {
        TextView title, date;
        VH(View v){ super(v); title=v.findViewById(R.id.eventTitleText); date=v.findViewById(R.id.eventDateText); }
    }
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vType){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_event, p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h, int i){
        Event e = items.get(i);
        h.title.setText(e.getEventTitle());
        h.date.setText(e.getEventDate());
        h.itemView.setOnClickListener(v -> onClick.onClick(e));
    }
    @Override public int getItemCount(){ return items.size(); }
}