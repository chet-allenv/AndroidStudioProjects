package com.example.assignment10.models;

import java.io.Serializable;

public class City implements Serializable {

    private String name;
    private String state;
    private double lat;
    private double lon;

    public City() {}

    public City(String name, String state, double lat, double lon) {
        this.name = name;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }
}
