package com.example.playgrounds01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.playgrounds01.myapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ResultListViewActivity extends AppCompatActivity {


    String myUrl = "https://api.naspisek.cz/api/playground/list";   // url databáze JSON
    ProgressDialog progressDialog;  //progress bar pro zobrazení čekání při načítání


    ListView listView;

    ImageView btnToolbarToMap;
    TextView textViewToolbarTitle;

    PlaygroundList playgroundList = new PlaygroundList();    // třída pro List s playgroundClass
    ArrayList<String> playgroundStringList;     // String ArrayList pro výpis do ListView
    Location currentloc;    //proměnná pro udržení hodnot současné lokace -> pro výpočet vzdálenosti hřišť od pozice uživatele
    int velikostVyberu;     //proměnná pro určení počtu vypsaných výsledků

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);

        //načtení data z databáze na internetu
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        // nastavení jména activity
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbat_title);
        textViewToolbarTitle.setText("Výpis hřišť do ListView");



        // načtení dat z předchozí activity
        Bundle extras = getIntent().getBundleExtra("bundle");

        velikostVyberu = extras.getInt("velVyber");
        currentloc = extras.getParcelable("location");

        // nastavení vlastností pro listview
        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int poziceVyberu = (int)parent.getItemIdAtPosition(position);

                Intent intent = new Intent(ResultListViewActivity.this,
                        PlaygroundDetailActivity.class);
                
                intent.putExtra("playground", playgroundList.getOnIndex(poziceVyberu));
                startActivity(intent);
            }
        });


        // nastavení vlastností pro button v toolbaru na přechod do zobrazení mapy (nová activity)
        btnToolbarToMap = (ImageView) findViewById(R.id.toolbar_mapBtn);
        btnToolbarToMap.setVisibility(View.VISIBLE);
        btnToolbarToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // zobrazení chybové hlášky v případě, kdy se nepodařilo načíst žádná data o hřištích
                if(playgroundList.size() < 1)
                {
                    Toast.makeText(ResultListViewActivity.this, "Nebyly načteny žádná data k hřištím", Toast.LENGTH_SHORT).show();
                    return;
                }

                // předání dat o hřištích
                Intent intent = new Intent(ResultListViewActivity.this, ResultMapActivity.class);

                intent.putExtra("playgroundList", playgroundList);
                //intent.putExtra("bundle2", extras);

                startActivity(intent);
            }
        });
    }

    // asynchronní načtení dat o hřištích z databáze
    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //zobraz progress dialog při načítání
            progressDialog = new ProgressDialog(ResultListViewActivity.this);
            progressDialog.setMessage("Zpracovávám výsledky");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // stopnutí zobrazení čekání běham načítání z DB
            progressDialog.dismiss();

            try
            {
                // načtení data z JSON a převedení do custom tříd
                JSONArray jsonArray = new JSONArray(s);

                String results = "";

                for(int i = 0; i < jsonArray.length(); i++)
                {
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


                if(currentloc != null)
                {
                    // předání dat pro nastavení počtu zobrazených výsledků a výchozí lokace, ze které se provede výpočet vzdálenosti
                    playgroundList.setVelVyberu(velikostVyberu);
                    playgroundList.setCurrentLocation(currentloc);
                }
                else
                {
                    Toast.makeText(ResultListViewActivity.this, "Souřadnice nenalezeny", Toast.LENGTH_SHORT).show();
                }

                // iniciliazece String listu s výsledky pro vypsání do ListView v activitě
                playgroundStringList = playgroundList.getStringArrayList();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(ResultListViewActivity.this,
                        android.R.layout.simple_list_item_1, playgroundStringList);

                listView.setAdapter(arrayAdapter);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            // připojení k DB
            String result = "";
            try
            {
                URL url;
                HttpsURLConnection urlConnection = null;
                try
                {
                    url = new URL(myUrl);

                    urlConnection = (HttpsURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while(data != -1)
                    {
                        result += (char) data;
                        data = isw.read();
                    }

                    return result;
                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally
                {
                    if(urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }

            return result;
        }
    }

}