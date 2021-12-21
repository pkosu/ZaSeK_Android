package com.example.playgrounds01;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.playgrounds01.myapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ResultMapActivity extends AppCompatActivity {

    GoogleMap mMap;

    Marker marker;

    Location currentloc;
    int velikostVyberu;

    PlaygroundList playgroundList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_map);

        // nastavení jména activity
        this.setTitle("Výpis hřišť do Mapy ");

        Bundle extras = getIntent().getBundleExtra("bundle");

        playgroundList = new PlaygroundList();

        velikostVyberu = extras.getInt("velVyber");
        currentloc = extras.getParcelable("location");

        if(currentloc != null)
        {
            playgroundList.setVelVyberu(velikostVyberu);
            playgroundList.setCurrentLocation(currentloc);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Souřadnice nenalezeny", Toast.LENGTH_SHORT).show();
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                //nastavení prvního Markeru
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("Prvotní pozice");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                LatLng nastavPozici = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());

                markerOptions.position(nastavPozici);
                marker = mMap.addMarker(markerOptions);

                // nastavení ostatních Markerů
                for(int i = 0; i < playgroundList.size(); i++)
                {
                    PlaygroundClass pg = playgroundList.getOnIndex(i);

                    MarkerOptions options = new MarkerOptions();
                    options.position(new LatLng(pg.getGps_lat(), pg.getGps_long()));
                    options.title(pg.getMarkerTitle());
                    mMap.addMarker(options);
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nastavPozici, 10));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

            }
        });
    }

}