package com.example.playgrounds01;

import androidx.appcompat.app.AppCompatActivity;

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

    PlaygroundList playgroundList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_map);

        // nastavení jména activity
        this.setTitle("Výpis hřišť do Mapy ");

        // načtení playground listu z ResultListViewActivity
        playgroundList = getIntent().getParcelableExtra("playgroundList");

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                //nastavení prvního Markeru (pozice od které začíná vyhledávání -> současná pozice uživatele nebo uživatelem zadaná pozice)
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("Prvotní pozice");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                LatLng nastavPozici = new LatLng(playgroundList.getCurrentLocation().getLatitude(),
                        playgroundList.getCurrentLocation().getLongitude());

                markerOptions.position(nastavPozici);
                marker = mMap.addMarker(markerOptions);

                // nastavení ostatních Markerů -> markery pro jednotlivá hřiště
                for(int i = 0; i < playgroundList.size(); i++)
                {
                    PlaygroundClass pg = playgroundList.getOnIndex(i);

                    MarkerOptions options = new MarkerOptions();
                    options.position(new LatLng(pg.getGps_lat(), pg.getGps_long()));
                    options.title(pg.getMarkerTitle());
                    mMap.addMarker(options);
                }


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nastavPozici, 10));

                // nastavení pro zobrazení informací o hřišti při kliknutí na marker
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