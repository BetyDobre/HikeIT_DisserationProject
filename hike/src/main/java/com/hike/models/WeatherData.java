package com.hike.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class WeatherData {
    private Integer temperature;
    private Integer feelsLike;
    private Integer humidity;
    private Double windSpeed;
    private String iconUrl;
    private LocalDate date;

    public WeatherData(Integer temperature, Integer feelsLike, Integer humidity, Double windSpeed, String iconCode) {
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";
    }
}