package com.example.playgrounds01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.playgrounds01.myapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // inicializace proměnných
    EditText editTextZadaniAdresy;
    TextView textViewZobrazeniVyberuVelikosti;
    SeekBar seekBarPojezdVyberuVelikosti;
    Button btnVyhledatPodleAdresy;
    LatLng latlng;
    Location currentLoc;
    FusedLocationProviderClient fusedLocationProviderClient;

    //toolbar komponenty
    TextView textViewToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // nastavení Toolbaru jména activity
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbat_title);
        textViewToolbarTitle.setText("PlayGrounds01");

        //nastavení viditelnosti ikonek toolbaru
        findViewById(R.id.toolbar_mapBtn).setVisibility(View.GONE);
        findViewById(R.id.toolbar_listBtn).setVisibility(View.GONE);
        findViewById(R.id.toolbar_sortBtn).setVisibility(View.GONE);

        // přiřazení proměnných
        editTextZadaniAdresy = findViewById(R.id.editText_zadaniAdresy);
        textViewZobrazeniVyberuVelikosti = findViewById(R.id.textView_zobrazeniVyberuVelikost);
        seekBarPojezdVyberuVelikosti = findViewById(R.id.seekBar_pojezdVyberuVelikosti);
        btnVyhledatPodleAdresy = findViewById(R.id.btn_vyhledat);

        // inicializace míst
        Places.initialize(getApplicationContext(),"AIzaSyCbYAY2jnLSwxM62aPS5U2L486PzkSyWzk");

        btnVyhledatPodleAdresy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultListViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("velVyber", seekBarPojezdVyberuVelikosti.getProgress());
                if(latlng != null)
                {
                    Location selectLoc = new Location("Test");
                    selectLoc.setLatitude(latlng.latitude);
                    selectLoc.setLongitude(latlng.longitude);
                    bundle.putParcelable("location", selectLoc);
                }
                else
                {
                    bundle.putParcelable("location", currentLoc);
                }
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        // nastav EditText nonfocusable
        editTextZadaniAdresy.setFocusable(false);
        editTextZadaniAdresy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inicializace listu pole míst
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                        Place.Field.NAME);
                // vytvoření obsahu
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(MainActivity.this);
                // start activity výsledky
                startActivityForResult(intent, 100);
            }
        });

        seekBarPojezdVyberuVelikosti.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewZobrazeniVyberuVelikosti.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null)
                {
                    String souradnice = "Souřadnice: " + location.getLatitude() + ", " + location.getLongitude();
                    currentLoc = location;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "nenalezeno", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            // pokud se podaří
            // incializuj místo
            Place place = Autocomplete.getPlaceFromIntent(data);
            // nastav adresu na EditText
            editTextZadaniAdresy.setText(place.getAddress());

            latlng = place.getLatLng();

            btnVyhledatPodleAdresy.setVisibility(View.VISIBLE);
        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR)
        {
            // pokud bude chyba
            // incializuj status
            Status status = Autocomplete.getStatusFromIntent(data);

            latlng = null;

            // zobraz Toast
            Toast.makeText(getApplicationContext(), status.getStatusMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}