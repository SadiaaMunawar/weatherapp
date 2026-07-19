# 🌦️ Weather App

A modern Flutter Weather Application that provides real-time weather information and forecasts using the OpenWeatherMap API. The application features dynamic weather backgrounds, dark/light mode, location-based weather, city search, offline storage, and a clean responsive user interface.

---

## 📱 Features

### 🌍 Current Weather
- Real-time weather updates
- Current temperature
- Weather description
- Humidity
- Wind speed
- Feels like temperature
- Atmospheric pressure

### 🔍 City Search
- Search weather by city name
- Instant weather updates
- Error handling for invalid cities

### 📍 Current Location
- Detect user's current location
- Display weather based on GPS
- Permission handling

### 📅 Weather Forecast
- 3-Day Weather Forecast
- Hourly Forecast
- Weather icons for each forecast

### 🎨 Dynamic User Interface
- Dynamic weather backgrounds
- Weather-specific icons
- Clean Material Design UI
- Responsive layout

### 🌙 Theme Support
- Light Mode
- Dark Mode
- Automatic theme switching

### 💾 Offline Support
- Stores last searched city
- Displays previous weather when offline
- Uses SharedPreferences

### 🔄 Additional Features
- Pull-to-refresh
- Loading animation
- Error messages
- Internet connectivity handling

---

# 📂 Project Structure

```
weather_app/
│
├── android/
├── ios/
├── lib/
│   ├── assets/
│   │   └── images/
│   │
│   ├── models/
│   │   ├── weather_model.dart
│   │   └── forecast_model.dart
│   │
│   ├── provider/
│   │   └── weather_provider.dart
│   │
│   ├── screens/
│   │   ├── home_screen.dart
│   │   ├── loading_screen.dart
│   │   └── search_screen.dart
│   │
│   ├── services/
│   │   ├── api_service.dart
│   │   └── local_storage_service.dart
│   │
│   ├── utils/
│   │   └── weather_icons.dart
│   │
│   ├── widgets/
│   │   ├── custom_card.dart
│   │   └── forecast_list.dart
│   │
│   └── main.dart
│
├── assets/
│   └── images/
│
├── pubspec.yaml
└── README.md
```

---

# 🛠️ Technologies Used

- Flutter
- Dart
- Provider (State Management)
- OpenWeatherMap API
- HTTP Package
- Shared Preferences
- Flutter SVG
- Geolocator
- Lottie Animations

---

# 📦 Dependencies

```yaml
provider
http
shared_preferences
flutter_svg
geolocator
permission_handler
lottie
flutter_dotenv
```

---

# 🔑 API

This application uses the **OpenWeatherMap API**.

Create a free account:

https://openweathermap.org/api

Generate an API key and place it inside your configuration file.

Example:

```
API_KEY=YOUR_API_KEY
```

---

# 🚀 Installation

### Clone Repository

```bash
git clone https://github.com/yourusername/weather_app.git
```

---

### Navigate to Project

```bash
cd weather_app
```

---

### Install Packages

```bash
flutter pub get
```

---

### Run Application

```bash
flutter run
```

---

# 📸 Screens

- Splash Screen
- Home Screen
- Search Screen
- Current Weather
- Hourly Forecast
- 3-Day Forecast
- Dark Theme
- Light Theme

---

# 🎯 Future Improvements

- Air Quality Index (AQI)
- Weather Maps
- Weather Notifications
- Sunrise & Sunset
- UV Index
- Weekly Forecast
- Multiple Saved Cities
- Language Support

---

# 👩‍💻 Developed By

**Sadia**

BS Information Technology

Air University Islamabad

---

# 📄 License

This project is developed for educational purposes and can be modified for learning and personal use.

---

## ⭐ If you found this project useful, consider giving it a star on GitHub!
