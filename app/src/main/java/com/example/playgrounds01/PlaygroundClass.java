package com.example.playgrounds01;

import java.io.Serializable;

public class PlaygroundClass implements Serializable {
    private double gps_latitude;
    private double gps_longitude;

    private int rank;
    private String popis;
    private float vzdalenost;

    public PlaygroundClass(double gps_latitude, double gps_longitude) {
        this.gps_latitude = gps_latitude;
        this.gps_longitude = gps_longitude;
        this.rank = 0;
        this.popis = "";
    }

    public PlaygroundClass(double gps_latitude, double gps_longitude, int rank, String popis) {
        this.gps_latitude = gps_latitude;
        this.gps_longitude = gps_longitude;
        this.rank = rank;
        this.popis = popis;
    }

    public double getGps_latitude() {
        return gps_latitude;
    }

    public void setGps_latitude(double gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    public double getGps_longitude() {
        return gps_longitude;
    }

    public void setGps_longitude(double gps_longitude) {
        this.gps_longitude = gps_longitude;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPopis() {
        return popis;
    }

    public String getMarkerTitle()
    {
        String ret = popis + "\nVzdálenost: " + getVzdalenost() + "m";
        return ret;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public float getVzdalenost() {
        return vzdalenost;
    }

    public void setVzdalenost(float vzdalenost) {
        this.vzdalenost = vzdalenost;
    }

}
