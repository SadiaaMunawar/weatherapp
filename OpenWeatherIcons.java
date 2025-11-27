package com.example.weatherapp;

public class OpenWeatherIcons {
    public static int iconRes(String code) {
        if (code == null) return R.drawable.cloudy;
        switch (code) {
            case "01d": case "01n": return R.drawable.sun;
            case "02d": case "02n": return R.drawable.cloudy;
            case "03d": case "03n": return R.drawable.cloudy;
            case "04d": case "04n": return R.drawable.cloudy;
            case "09d": case "09n": return R.drawable.rainy;
            case "10d": case "10n": return R.drawable.rainy;
            case "11d": case "11n": return R.drawable.thunder;
            case "13d": case "13n": return R.drawable.snowy;
            case "50d": case "50n": return R.drawable.fog;
            default: return R.drawable.cloudy;
        }
    }
}