package com.hike.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hike.models.WeatherData;
import com.hike.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Value("${openweathermap.api.key}")
    private String apiKey;
    private String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";

    public List<WeatherData> getWeatherData(Double latitude, Double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey +"&units=metric&cnt=3";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<WeatherData> weatherDataList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        JsonNode list = root.path("list");
        for (int i = 0; i < 3; i++) {
            Integer temperature = list.get(i).path("main").path("temp").asInt();
            Integer feelsLike = list.get(i).path("main").path("feels_like").asInt();
            Integer humidity = list.get(i).path("main").path("humidity").asInt();
            Double windSpeed = list.get(i).path("wind").path("speed").asDouble();
            String iconCode = list.get(i).path("weather").get(0).path("icon").asText();
            WeatherData weatherData = new WeatherData(temperature, feelsLike, humidity, windSpeed, iconCode);
            weatherData.setDate(date.plusDays(i));
            weatherDataList.add(weatherData);
        }

        return weatherDataList;
    }
}