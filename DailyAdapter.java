package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.Holder> {
    private List<MainActivity.DailyItem> items;

    public DailyAdapter(List<MainActivity.DailyItem> items) {
        this.items = items;
    }

    public void setItems(List<MainActivity.DailyItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_daily.xml layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_daily_adapter, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        MainActivity.DailyItem it = items.get(pos);

        // Format date nicely
        h.date.setText(formatDate(it.date));
        h.desc.setText(it.desc);
        h.min.setText("Min: " + it.min);
        h.max.setText("Max: " + it.max);

        // Set weather icon
        h.icon.setImageResource(OpenWeatherIcons.iconRes(it.icon));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView date, desc, min, max;

        Holder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.itemIcon);
            date = itemView.findViewById(R.id.itemDate);
            desc = itemView.findViewById(R.id.itemDesc);
            min = itemView.findViewById(R.id.itemMin);
            max = itemView.findViewById(R.id.itemMax);
        }
    }

    private String formatDate(String iso) {
        try {
            SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outFmt = new SimpleDateFormat("EEE, MMM dd");
            java.util.Date d = inFmt.parse(iso);
            return outFmt.format(d);
        } catch (Exception e) {
            return iso;
        }
    }
}
