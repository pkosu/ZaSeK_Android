package com.example.playgrounds01.Classes;


import android.os.Parcel;
import android.os.Parcelable;

public class PlaygroundClass implements Parcelable {

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

    // metoda nutná pro Parcelable
    protected PlaygroundClass(Parcel in) {
        id_pg = in.readString();
        gps_lat = in.readDouble();
        gps_long = in.readDouble();
        name = in.readString();
        type = in.readInt();
        pg_rank = in.readInt();
        id_evaluation = in.readInt();
        id_multimedia = in.readInt();
        vzdalenost = in.readFloat();
    }

    // metoda nutná pro Parcelable
    public static final Creator<PlaygroundClass> CREATOR = new Creator<PlaygroundClass>() {
        @Override
        public PlaygroundClass createFromParcel(Parcel in) {
            return new PlaygroundClass(in);
        }

        @Override
        public PlaygroundClass[] newArray(int size) {
            return new PlaygroundClass[size];
        }
    };

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

    public float getVzdalenostKm() {
        float km = (float) Math.round((vzdalenost / 1000) * 100 ) /100;

        return km;
    }

    public void setVzdalenost(float vzdalenost) {
        this.vzdalenost = vzdalenost;
    }


    // metoda nutná pro Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    // metoda nutná pro Parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_pg);
        dest.writeDouble(gps_lat);
        dest.writeDouble(gps_long);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeInt(pg_rank);
        dest.writeInt(id_evaluation);
        dest.writeInt(id_multimedia);
        dest.writeFloat(vzdalenost);
    }
}