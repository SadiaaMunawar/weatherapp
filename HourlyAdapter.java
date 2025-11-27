package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.Holder> {
    private List<MainActivity.HourlyItem> items;

    public HourlyAdapter(List<MainActivity.HourlyItem> items) {
        this.items = items;
    }

    public void setItems(List<MainActivity.HourlyItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView time, temp;

        Holder(View v) {
            super(v);
            icon = v.findViewById(R.id.itemIcon);
            time = v.findViewById(R.id.itemTime);
            temp = v.findViewById(R.id.itemTemp);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the correct item layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_hourly_adapter, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        MainActivity.HourlyItem it = items.get(pos);
        h.time.setText(it.time);
        h.temp.setText(it.temp);

        // Use iconRes with just the code string
        h.icon.setImageResource(OpenWeatherIcons.iconRes(it.icon));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
