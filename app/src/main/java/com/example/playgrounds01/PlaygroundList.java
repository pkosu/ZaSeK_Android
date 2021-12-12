package com.example.playgrounds01;

import android.location.Location;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;

public class PlaygroundList extends ArrayList<Parcelable> {
    private ArrayList<PlaygroundClass> playgrounds_list;
    private Location currentLocation;
    private int velVyberu;

    public PlaygroundList()
    {
        playgrounds_list = new ArrayList<PlaygroundClass>();
        velVyberu = 0;

        NacistTestovaciHriste();
    }

    public PlaygroundClass getOnIndex(int index)
    {
        return playgrounds_list.get(index);
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        calcLenghBetweenLocations();
    }

    public void setVelVyberu(int velVyberu) {
        this.velVyberu = velVyberu;
    }

    private void calcLenghBetweenLocations()
    {
        if(playgrounds_list.size() == 0) return;

        for(PlaygroundClass pl : playgrounds_list)
        {
            float[] result = new float[1];
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                    pl.getGps_latitude(), pl.getGps_longitude(), result);
            pl.setVzdalenost(result[0]);
        }

        SeradVyber();
    }

    public int size()
    {
        return playgrounds_list.size();
    }

    private void SeradVyber()
    {
        playgrounds_list.sort(new Comparator<PlaygroundClass>() {
            @Override
            public int compare(PlaygroundClass o1, PlaygroundClass o2) {
                if(o1.getVzdalenost() == o2.getVzdalenost())
                {
                    return 0;
                }

                if(o1.getVzdalenost() > o2.getVzdalenost())
                {
                    return 1;
                }

                return -1;
            }
        });

        if(velVyberu > 0)
        {
            if(velVyberu > playgrounds_list.size())
            {
                velVyberu = playgrounds_list.size();
            }
            playgrounds_list.subList(velVyberu, playgrounds_list.size()).clear();
        }

    }

    public ArrayList<String> getStringArrayList()
    {
        ArrayList<String> retList = new ArrayList<>();

        for(PlaygroundClass pg : playgrounds_list)
        {
            String txt = "Latitude: " + pg.getGps_latitude() + " Longitude: " + pg.getGps_longitude() +
                    "\nPopis: " + pg.getPopis() + "\nRank: " + pg.getRank() + "\nVzdálenost: " +
                    pg.getVzdalenost() + "m";
            retList.add(txt);
        }

        return retList;
    }

    public ArrayList<String> getStringArrayList(int pocetVyber)
    {
        ArrayList<String> retList = new ArrayList<>();

        for(PlaygroundClass pg : playgrounds_list)
        {
            String txt = "Latitude: " + pg.getGps_latitude() + " Longitude: " + pg.getGps_longitude() +
                    "\nPopis: " + pg.getPopis() + "\nRank: " + pg.getRank() + "\nVzdálenost: " +
                    pg.getVzdalenost();
            retList.add(txt);
        }

        return retList;
    }

    private void NacistTestovaciHriste()
    {
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

}
