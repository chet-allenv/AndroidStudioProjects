package com.example.assignment10.models;

import android.graphics.drawable.Icon;

import java.io.Serializable;

public class Forecast implements Serializable {

    private String startTime;
    private int temperature;
    private int precipitationPercent;
    private String windSpeed;
    private String shortForecast;
    private String weatherIconURL;

    public Forecast() {}

    public Forecast(String startTime, int temperature, int precipitationPercent, String windSpeed, String shortForecast, String weatherIconURL) {
        this.startTime = startTime;
        this.temperature = temperature;
        this.precipitationPercent = precipitationPercent;
        this.windSpeed = windSpeed;
        this.shortForecast = shortForecast;
        this.weatherIconURL = weatherIconURL;
    }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public int getTemperature() { return temperature; }
    public void setTemperature(int temperature) { this.temperature = temperature; }

    public int getPrecipitationPercent() { return precipitationPercent; }
    public void setPrecipitationPercent(int precipitationPercent) { this.precipitationPercent = precipitationPercent; }

    public String getWindSpeed() { return windSpeed; }
    public void setWindSpeed(String windSpeed) { this.windSpeed = windSpeed; }

    public String getShortForecast() { return shortForecast; }
    public void setShortForecast(String shortForecast) { this.shortForecast = shortForecast; }

    public String getWeatherIconURL() { return weatherIconURL; }
    public void setWeatherIconURL(String weatherIconURL) { this.weatherIconURL = weatherIconURL; }
}
