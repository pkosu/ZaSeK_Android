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
import android.widget.Button;
import android.widget.ListView;
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
    PlaygroundList playgroundList = new PlaygroundList();
    ArrayList<String> playgroundStringList;
    Location currentloc;
    Button btnMapActivity;
    int velikostVyberu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);

        //načtení data z databáze na internetu
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        // nastavení jména activity
        this.setTitle("Výpis hřišť do ListView ");

        Bundle extras = getIntent().getBundleExtra("bundle");

        velikostVyberu = extras.getInt("velVyber");
        currentloc = extras.getParcelable("location");


        listView = findViewById(R.id.list_view);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String txt = ((TextView) view).getText().toString();
                String txt = (String)parent.getItemAtPosition(position);
                int pozice = (int)parent.getItemIdAtPosition(position);
                String poziceString = Integer.toString(pozice);

                /*
                Toast.makeText(getApplicationContext(), txt + " \nID v listView: "
                        + poziceString, Toast.LENGTH_SHORT).show();
*/

                Intent intent = new Intent(ResultListViewActivity.this,
                        PlaygroundDetailActivity.class);
                intent.putExtra("playground", playgroundList.getOnIndex(pozice));
                startActivity(intent);
            }
        });

        btnMapActivity = findViewById(R.id.btn_PrejdiDoMapaActivity);
        btnMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultListViewActivity.this, ResultMapActivity.class);

                intent.putExtra("bundle", extras);

                startActivity(intent);
            }
        });
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //zobraz progress dialog
            progressDialog = new ProgressDialog(ResultListViewActivity.this);
            progressDialog.setMessage("Zpracovávám výsledky");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            try
            {
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

                    //results += "ID: " + id_pg + "\n" + "Name: " + name +"\n\n";
                }



                //Toast.makeText(ResultListViewActivity.this, results, Toast.LENGTH_SHORT).show();

                if(currentloc != null)
                {
                    playgroundList.setVelVyberu(velikostVyberu);
                    playgroundList.setCurrentLocation(currentloc);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Souřadnice nenalezeny", Toast.LENGTH_SHORT).show();
                }


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