package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.VH> {

    public interface OnPickListener {
        void onPick(String cityName);
    }

    private final List<MainActivity.LocationItem> items;
    private final OnPickListener onPick;

    public LocationAdapter(List<MainActivity.LocationItem> items, OnPickListener onPick) {
        this.items = items;
        this.onPick = onPick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        MainActivity.LocationItem it = items.get(pos);

        h.primary.setText(it.name);
        h.secondary.setText(it.now + "   H: " + it.high + "   L: " + it.low);

        int resId = OpenWeatherIcons.iconRes(h.icon.getContext(), it.icon);
        h.icon.setImageResource(resId);

        h.itemView.setOnClickListener(v -> {
            if (onPick != null) {
                onPick.onPick(it.name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView primary, secondary;

        VH(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.itemIcon);
            primary = itemView.findViewById(R.id.itemPrimary);
            secondary = itemView.findViewById(R.id.itemSecondary);
        }
    }
}
