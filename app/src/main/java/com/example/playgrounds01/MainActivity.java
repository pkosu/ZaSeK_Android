package com.example.playgrounds01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    // string pro vypsání hledané hodnoty
    String txtHledani = "Hledej do vzdálenosti: xxx km";

    // inicializace kompenent
    EditText editTextZadaniAdresy;
    TextView textViewZobrazeniVyberuVzdalenosti;
    SeekBar seekBarPojezdVyberuVelikosti;
    Button btnVyhledatPodleAdresy;


    final String playgourndsDbUrl = "https://api.naspisek.cz/api/playground/list";   // url databáze JSON
    ProgressDialog progressDialog;  //progress bar pro zobrazení čekání při načítání
    PlaygroundList playgroundList = new PlaygroundList();    // třída pro List s playgroundClass
    // pomocné proměnné aktivity
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

        // nastavení seekbaru
        seekBarPojezdVyberuVelikosti = findViewById(R.id.seekBar_pojezdVyberuVelikosti);
        seekBarPojezdVyberuVelikosti.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewZobrazeniVyberuVzdalenosti.setText(txtHledani.replace("xxx", String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // textView pro zobrazení zvolené vzdálenosti u hledání
        textViewZobrazeniVyberuVzdalenosti = findViewById(R.id.textView_zobrazeniVyberuVelikost);
        textViewZobrazeniVyberuVzdalenosti.setText(txtHledani.replace("xxx",
                String.valueOf(seekBarPojezdVyberuVelikosti.getProgress())));

        // inicializace míst
        Places.initialize(getApplicationContext(), "AIzaSyCbYAY2jnLSwxM62aPS5U2L486PzkSyWzk");

        // nastavení buttonu pro potvrzení hledání
        btnVyhledatPodleAdresy = findViewById(R.id.btn_vyhledat);
        btnVyhledatPodleAdresy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kontrola připojení k internetu
                if (!CheckInternetConnection()) {
                    return;
                }

                //načtení data z databáze na internetu
                MainActivity.MyAsyncTasks myAsyncTasks = new MainActivity.MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });

        // nastav EditText nonfocusable
        editTextZadaniAdresy = findViewById(R.id.editText_zadaniAdresy);
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


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    String souradnice = "Souřadnice: " + location.getLatitude() + ", " + location.getLongitude();
                    currentLoc = location;
                } else {
                    Toast.makeText(getApplicationContext(), "nenalezeno", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // kontrola konektivity internetu
    private boolean CheckInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

        if (connected) {
            Toast.makeText(getApplicationContext(), "JE připojeno", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "BEZ připojení -> zapněte data", Toast.LENGTH_SHORT).show();
        }

        return connected;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // pokud se podaří
            // incializuj místo
            Place place = Autocomplete.getPlaceFromIntent(data);
            // nastav adresu na EditText
            editTextZadaniAdresy.setText(place.getAddress());

            latlng = place.getLatLng();

            btnVyhledatPodleAdresy.setVisibility(View.VISIBLE);
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // pokud bude chyba
            // incializuj status
            Status status = Autocomplete.getStatusFromIntent(data);

            latlng = null;

            // zobraz Toast
            Toast.makeText(getApplicationContext(), status.getStatusMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }


    // asynchronní načtení dat o hřištích z databáze
    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //zobraz progress dialog při načítání
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Zpracovávám výsledky");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // stopnutí zobrazení čekání běham načítání z DB
            progressDialog.dismiss();

            try {
                // načtení data z JSON a převedení do custom tříd
                JSONArray jsonArray = new JSONArray(s);

                String results = "";

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String id_pg = jsonObject.getString("id_pg");
                    double gps_lat = jsonObject.getDouble("gps_lat");
                    double gps_long = jsonObject.getDouble("gps_long");
                    String name = jsonObject.getString("name");
                    int type = jsonObject.getInt("type");
                    int pg_rank = jsonObject.getInt("pg_rank");

                    PlaygroundClass pg = new PlaygroundClass(id_pg, gps_lat, gps_long, name, type, pg_rank);
                    playgroundList.add(pg);
                }

                // Chybová hláška, pokud nebyly nalezeny žádné výsledky
                if (playgroundList.size() < 1) {
                    Toast.makeText(MainActivity.this, "Nebyly nalezeny žádné výsledky", Toast.LENGTH_LONG).show();
                    return;
                }

                // předání dat pro nastavení počtu zobrazených výsledků a výchozí lokace, ze které se provede výpočet vzdálenosti
                if (latlng != null) {
                    Location selectLoc = new Location("Test");
                    selectLoc.setLatitude(latlng.latitude);
                    selectLoc.setLongitude(latlng.longitude);
                    playgroundList.setCurrentLocation(selectLoc);
                } else {
                    playgroundList.setCurrentLocation(currentLoc);
                }


                playgroundList.setVelVyberuKm(seekBarPojezdVyberuVelikosti.getProgress());
                // seřazení hřišť
                playgroundList.SeradAOmezPodleKm();

                // chybová hláška pokud v nastavené vzdálenosti nebyly nalezeny žádné výsledky
                if (playgroundList.size() < 1) {
                    Toast.makeText(MainActivity.this, "V zadané vzdálenosti nebyly nalezeny žádné výsledky",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // předání výpisu do Activity ResultListViewActivity
                Intent intent = new Intent(MainActivity.this, ResultListViewActivity.class);

                intent.putExtra("playgroundList", playgroundList);

                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Nepodařilo se načíst data..\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


        @Override
        protected String doInBackground(String... strings) {

            // připojení k DB
            String result = "";
            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(playgourndsDbUrl);

                    urlConnection = (HttpsURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();
                    }

                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Nepodařilo se připojit k DB..\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                return "Exception: " + e.getMessage();
            }

            return result;
        }
    }
}