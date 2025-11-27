package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Native Android (Java) weather app:
 * - Search by city
 * - Current weather: temp, description, humidity, wind, icon
 * - 3-day forecast: date, min/max temp, short description, icon
 * - Loading and error UI
 * - Last-searched city persisted
 */
public class MainActivity extends AppCompatActivity {

    private TextView tabCurrent, tabForecast, tabLocation;
    private View currentView, forecastView, locationView;

    private EditText searchBar;
    private Button btnSearch;
    private SharedPreferences prefs;

    private TextView tvLocation, tvTemp, tvDesc, tvHumidity, tvWind;
    private ImageView ivIcon;
    private RecyclerView rvHourly;

    private RecyclerView rvTodayTimeline;
    private RecyclerView rvNextDays;

    private RecyclerView rvLocations;

    private ProgressBar progress;
    private TextView tvError;

    private static final String PREFS = "WeatherPrefs";
    private static final String PREF_LAST_CITY = "lastCity";
    private static final String API_KEY = "YOUR_API_KEY"; // set your key

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.searchBar);
        btnSearch = findViewById(R.id.btnSearch);
        progress = findViewById(R.id.progress);
        tvError = findViewById(R.id.tvError);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        tabCurrent = findViewById(R.id.tabCurrent);
        tabForecast = findViewById(R.id.tabForecast);
        tabLocation = findViewById(R.id.tabLocation);

        currentView = findViewById(R.id.viewCurrent);
        forecastView = findViewById(R.id.viewForecast);
        locationView = findViewById(R.id.viewLocation);

        String lastCity = prefs.getString(PREF_LAST_CITY, "Miami");
        searchBar.setText(lastCity);

        btnSearch.setOnClickListener(v -> {
            String city = searchBar.getText().toString().trim();
            if (!TextUtils.isEmpty(city)) {
                fetchAll(city);
                prefs.edit().putString(PREF_LAST_CITY, city).apply();
            }
        });

        searchBar.setOnEditorActionListener((tv, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String city = searchBar.getText().toString().trim();
                if (!TextUtils.isEmpty(city)) {
                    fetchAll(city);
                    prefs.edit().putString(PREF_LAST_CITY, city).apply();
                }
                return true;
            }
            return false;
        });

        tvLocation = findViewById(R.id.tvLocation);
        tvTemp = findViewById(R.id.tvTemp);
        tvDesc = findViewById(R.id.tvDesc);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);
        ivIcon = findViewById(R.id.ivIcon);

        rvHourly = findViewById(R.id.rvHourly);
        rvHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHourly.setAdapter(new HourlyAdapter(new ArrayList<>()));

        rvTodayTimeline = findViewById(R.id.rvTodayTimeline);
        rvNextDays = findViewById(R.id.rvNextDays);
        rvTodayTimeline.setLayoutManager(new LinearLayoutManager(this));
        rvNextDays.setLayoutManager(new LinearLayoutManager(this));
        rvTodayTimeline.setAdapter(new HourlyAdapter(new ArrayList<>()));
        rvNextDays.setAdapter(new DailyAdapter(new ArrayList<>()));

        rvLocations = findViewById(R.id.rvLocations);
        rvLocations.setLayoutManager(new LinearLayoutManager(this));
        rvLocations.setAdapter(new LocationAdapter(getPresetLocations(), city -> {
            searchBar.setText(city);
            fetchAll(city);
            prefs.edit().putString(PREF_LAST_CITY, city).apply();
            switchTo(currentView);
            selectTab(tabCurrent);
        }));

        tabCurrent.setOnClickListener(v -> { switchTo(currentView); selectTab(tabCurrent); });
        tabForecast.setOnClickListener(v -> { switchTo(forecastView); selectTab(tabForecast); });
        tabLocation.setOnClickListener(v -> { switchTo(locationView); selectTab(tabLocation); });

        fetchAll(lastCity);
        switchTo(currentView);
        selectTab(tabCurrent);
    }

    private void switchTo(View target) {
        currentView.setVisibility(target == currentView ? View.VISIBLE : View.GONE);
        forecastView.setVisibility(target == forecastView ? View.VISIBLE : View.GONE);
        locationView.setVisibility(target == locationView ? View.VISIBLE : View.GONE);
    }

    private void selectTab(TextView selected) {
        tabCurrent.setAlpha(selected == tabCurrent ? 1f : 0.6f);
        tabForecast.setAlpha(selected == tabForecast ? 1f : 0.6f);
        tabLocation.setAlpha(selected == tabLocation ? 1f : 0.6f);
    }

    private void fetchAll(String city) {
        tvError.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        // Current weather
        new FetchTask(result -> {
            try {
                JSONObject obj = new JSONObject(result);
                if (!obj.has("main")) throw new RuntimeException("City not found");

                String name = obj.optString("name", city);
                double temp = obj.getJSONObject("main").getDouble("temp");
                int humidity = obj.getJSONObject("main").getInt("humidity");
                double wind = obj.getJSONObject("wind").getDouble("speed");
                JSONArray weatherArr = obj.getJSONArray("weather");
                String iconCode = weatherArr.getJSONObject(0).getString("icon");
                String desc = weatherArr.getJSONObject(0).getString("description");

                tvLocation.setText(name);
                tvTemp.setText(Math.round(temp) + "°C");
                tvDesc.setText(capitalize(desc));
                tvHumidity.setText("Humidity: " + humidity + "%");
                tvWind.setText("Wind: " + Math.round(wind) + " m/s");
                ivIcon.setImageResource(OpenWeatherIcons.iconRes(iconCode));
            } catch (Exception e) {
                tvError.setText("Failed to load current weather. Check city or network.");
                tvError.setVisibility(View.VISIBLE);
            } finally {
                progress.setVisibility(View.GONE);
            }
        }).execute("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric");

        // Forecast
        new FetchTask(result -> {
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray list = obj.getJSONArray("list");

                // Hourly (7 slots) for timeline
                List<HourlyItem> hourly = new ArrayList<>();
                for (int i = 0; i < Math.min(7, list.length()); i++) {
                    JSONObject slot = list.getJSONObject(i);
                    String dtTxt = slot.getString("dt_txt");
                    double t = slot.getJSONObject("main").getDouble("temp");
                    String icon = slot.getJSONArray("weather").getJSONObject(0).getString("icon");
                    String time = dtTxt.substring(11, 16);
                    hourly.add(new HourlyItem(time, Math.round(t) + "°C", icon));
                }

                // Daily aggregate: min/max + description + icon (prefer 12:00)
                Map<String, DayAgg> bucket = new LinkedHashMap<>();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject slot = list.getJSONObject(i);
                    String dtTxt = slot.getString("dt_txt");
                    String date = dtTxt.substring(0, 10);
                    double t = slot.getJSONObject("main").getDouble("temp");
                    JSONObject w = slot.getJSONArray("weather").getJSONObject(0);
                    String desc = w.getString("description");
                    String icon = w.getString("icon");

                    DayAgg agg = bucket.getOrDefault(date, new DayAgg());
                    agg.min = agg.min == null ? t : Math.min(agg.min, t);
                    agg.max = agg.max == null ? t : Math.max(agg.max, t);
                    if (agg.icon == null || dtTxt.contains("12:00:00")) {
                        agg.icon = icon;
                        agg.desc = desc;
                    }
                    bucket.put(date, agg);
                }

                // Build next 3 days (skip today if you want strictly "next")
                List<DailyItem> daily = new ArrayList<>();
                for (Map.Entry<String, DayAgg> e : bucket.entrySet()) {
                    if (daily.size() == 3) break;
                    DayAgg a = e.getValue();
                    daily.add(new DailyItem(
                            e.getKey(),
                            Math.round(a.min) + "°C",
                            Math.round(a.max) + "°C",
                            capitalize(a.desc),
                            a.icon
                    ));
                }

                // Bind to Forecast Report section
                ((HourlyAdapter) rvTodayTimeline.getAdapter()).setItems(hourly);
                ((DailyAdapter) rvNextDays.getAdapter()).setItems(daily);

            } catch (Exception e) {
                tvError.setText("Failed to load forecast. Try again.");
                tvError.setVisibility(View.VISIBLE);
            }
        }).execute("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric");
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // Preset locations
    private List<LocationItem> getPresetLocations() {
        List<LocationItem> list = new ArrayList<>();
        list.add(new LocationItem("Montreal, Canada", "-3°C", "-2°C", "-6°C", "13d"));
        list.add(new LocationItem("Tokyo, Japan", "12°C", "13°C", "9°C", "02d"));
        list.add(new LocationItem("Taipei, Taiwan", "18°C", "19°C", "17°C", "10d"));
        list.add(new LocationItem("Toronto, Canada", "-6°C", "-3°C", "-8°C", "13d"));
        return list;
    }

    // -------- helpers, models, async --------

    static class FetchTask extends AsyncTask<String, Void, String> {
        interface Callback { void onResult(String result); }
        private final Callback cb;
        FetchTask(Callback cb) { this.cb = cb; }
        @Override protected String doInBackground(String... urls) { return SimpleHttp.get(urls[0]); }
        @Override protected void onPostExecute(String s) { if (cb != null && s != null) cb.onResult(s); }
    }

    static class HourlyItem {
        final String time, temp, icon;
        HourlyItem(String time, String temp, String icon) { this.time = time; this.temp = temp; this.icon = icon; }
    }

    static class DailyItem {
        final String date, min, max, desc, icon;
        DailyItem(String date, String min, String max, String desc, String icon) {
            this.date = date; this.min = min; this.max = max; this.desc = desc; this.icon = icon;
        }
    }

    static class DayAgg { Double min, max; String desc, icon; }

    static class LocationItem {
        final String name, now, high, low, icon;
        LocationItem(String name, String now, String high, String low, String icon) {
            this.name = name; this.now = now; this.high = high; this.low = low; this.icon = icon;
        }
    }
}
