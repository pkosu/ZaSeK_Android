package com.example.playgrounds01;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;


public class PlaygroundList implements Parcelable {
    private ArrayList<PlaygroundClass> playgrounds_list;
    private Location currentLocation;
    private int velVyberuKm;

    // pouze pomocná proměnná pro sortování-> po přidání filtrace výsledku podle dalších kritérií
    // bude odebrána
    private int lastSortOptions = 1;

    public PlaygroundList() {
        playgrounds_list = new ArrayList<>();
        velVyberuKm = 0;

        //NacistTestovaciHriste();
    }

    // metoda nutná pro Parcelable
    protected PlaygroundList(Parcel in) {
        playgrounds_list = in.createTypedArrayList(PlaygroundClass.CREATOR);
        currentLocation = in.readParcelable(Location.class.getClassLoader());
        velVyberuKm = in.readInt();
    }

    // metoda nutná pro Parcelable
    public static final Creator<PlaygroundList> CREATOR = new Creator<PlaygroundList>() {
        @Override
        public PlaygroundList createFromParcel(Parcel in) {
            return new PlaygroundList(in);
        }

        @Override
        public PlaygroundList[] newArray(int size) {
            return new PlaygroundList[size];
        }
    };

    public PlaygroundClass getOnIndex(int index) {
        return playgrounds_list.get(index);
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        calcLenghBetweenLocations();
    }

    public void setVelVyberuKm(int velVyberuKm) {
        this.velVyberuKm = velVyberuKm;
    }

    private void calcLenghBetweenLocations() {
        if (playgrounds_list.size() == 0) return;

        for (PlaygroundClass pl : playgrounds_list) {
            float[] result = new float[1];
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                    pl.getGps_lat(), pl.getGps_long(), result);
            pl.setVzdalenost(result[0]);
        }

        SeradAVypis();
    }

    public void add(PlaygroundClass pg) {
        playgrounds_list.add(pg);
    }

    public int size() {
        return playgrounds_list.size();
    }

    public void SeradAOmezPodleKm(){

        SeradVyberPodleVzdalenosti();
        lastSortOptions = 0;
        
        if (velVyberuKm > 0) {
            int index = -1;
            for (int i = 0; i < playgrounds_list.size(); i++) {
                if (playgrounds_list.get(i).getVzdalenostKm() > velVyberuKm) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                playgrounds_list.subList(index, playgrounds_list.size()).clear();
            }
        }

    }

    public String SeradAVypis() {
        String ret = "";
        if (lastSortOptions == 1) {
            SeradVyberPodleVzdalenosti();
            lastSortOptions = 0;
            ret = "Seřazeno podle vzdálenosti";
        } else {
            SeradVyberPodleRanku();
            lastSortOptions = 1;
            ret = "Seřazeno podle hodnocení";
        }

        return ret;
    }

    private void SeradVyberPodleVzdalenosti() {
        playgrounds_list.sort(new Comparator<PlaygroundClass>() {
            @Override
            public int compare(PlaygroundClass o1, PlaygroundClass o2) {
                if (o1.getVzdalenost() == o2.getVzdalenost()) {
                    return 0;
                }

                if (o1.getVzdalenost() > o2.getVzdalenost()) {
                    return 1;
                }

                return -1;
            }
        });
    }

    private void SeradVyberPodleRanku() {
        playgrounds_list.sort(new Comparator<PlaygroundClass>() {
            @Override
            public int compare(PlaygroundClass o1, PlaygroundClass o2) {
                if (o1.getPg_rank() == o2.getPg_rank()) {
                    return 0;
                }

                if (o1.getPg_rank() > o2.getPg_rank()) {
                    return 1;
                }

                return -1;
            }
        });
    }

    public ArrayList<String> getStringArrayList() {
        ArrayList<String> retList = new ArrayList<>();

        for (PlaygroundClass pg : playgrounds_list) {
            String txt = "Latitude: " + pg.getGps_lat() + " Longitude: " + pg.getGps_long() +
                    "\nPopis: " + pg.getName() + "\nRank: " + pg.getPg_rank() + "\nVzdálenost: " +
                    pg.getVzdalenostKm() + "km";
            retList.add(txt);
        }

        return retList;
    }

    public ArrayList<String> getStringArrayList(int pocetVyber) {
        ArrayList<String> retList = new ArrayList<>();

        for (PlaygroundClass pg : playgrounds_list) {
            String txt = "Latitude: " + pg.getGps_lat() + " Longitude: " + pg.getGps_long() +
                    "\nPopis: " + pg.getName() + "\nRank: " + pg.getPg_rank() + "\nVzdálenost: " +
                    pg.getVzdalenost();
            retList.add(txt);
        }

        return retList;
    }

    private void NacistTestovaciHriste() {
        playgrounds_list.add(new PlaygroundClass(49.72049, 18.30636, 4,
                "Dětské hřiště Paskov"));

        playgrounds_list.add(new PlaygroundClass(49.78171, 18.27890, 3,
                "Dětské hřiště Hrabová"));
        playgrounds_list.add(new PlaygroundClass(49.74667, 18.60231, 2,
                "SAPEKOR s.r.o."));
        playgrounds_list.add(new PlaygroundClass(49.69488, 18.41371, 1,
                "Sportovní a dětské hřiště Dobrá"));
        playgrounds_list.add(new PlaygroundClass(49.80092, 18.25116, 5,
                "Dětské hřiště a pískoviště F.Lýska"));
        playgrounds_list.add(new PlaygroundClass(49.78173, 18.44978, 4,
                "Pískoviště na Podlesí"));
    }

    // metoda nutná pro Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    // metoda nutná pro Parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(playgrounds_list);
        dest.writeParcelable(currentLocation, flags);
        dest.writeInt(velVyberuKm);
    }
}
