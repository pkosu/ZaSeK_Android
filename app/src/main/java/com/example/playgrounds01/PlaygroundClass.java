package com.example.playgrounds01;

import java.io.Serializable;

public class PlaygroundClass implements Serializable {

    // data načtená z JSON databáze
    private String id_pg;
    private double gps_lat;
    private double gps_long;

    private String name;
    private int type;

    private int pg_rank;
    private int id_evaluation;
    private int id_multimedia;


    private float vzdalenost;

    public PlaygroundClass(double gps_lat, double gps_long) {
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.pg_rank = 0;
        this.name = "";
    }

    public PlaygroundClass(double gps_lat, double gps_long, int pg_rank, String name) {
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.pg_rank = pg_rank;
        this.name = name;
    }

    public PlaygroundClass(String id_pg, double gps_lat, double gps_long, String name, int type, int pg_rank) {
        this.id_pg = id_pg;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.name = name;
        this.type = type;
        this.pg_rank = pg_rank;
        this.id_evaluation = 0;
        this.id_multimedia = 0;

        //prozatím nevyužívá id_evaluation a id_multimedia
    }

    public double getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(double gps_lat) {
        this.gps_lat = gps_lat;
    }

    public double getGps_long() {
        return gps_long;
    }

    public void setGps_long(double gps_long) {
        this.gps_long = gps_long;
    }

    public int getPg_rank() {
        return pg_rank;
    }

    public void setPg_rank(int pg_rank) {
        this.pg_rank = pg_rank;
    }

    public String getName() {
        return name;
    }

    public String getMarkerTitle()
    {
        String ret = name + "\nVzdálenost: " + getVzdalenost() + "m";
        return ret;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVzdalenost() {
        return vzdalenost;
    }

    public void setVzdalenost(float vzdalenost) {
        this.vzdalenost = vzdalenost;
    }

}