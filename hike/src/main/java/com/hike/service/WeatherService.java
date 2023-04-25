package com.hike.service;

import com.hike.models.WeatherData;

import java.util.List;

public interface WeatherService {
    List<WeatherData> getWeatherData(Double latitude, Double longitude);
}
