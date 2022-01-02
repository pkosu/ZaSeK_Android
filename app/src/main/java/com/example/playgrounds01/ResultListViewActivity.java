package com.example.playgrounds01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    // proměnné pro kompnenty v aktivitě
    ListView listView;

    // toolbar proměnné
    ImageView btnToolbarToMap;
    ImageView btnToolbarSort;
    TextView textViewToolbarTitle;

    // proměnné pro List s hřišti
    PlaygroundList playgroundList;    // třída pro List s playgroundClass
    ArrayList<String> playgroundStringList;     // String ArrayList pro výpis do ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);

        // nastavení jména activity v toolbaru
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbat_title);
        textViewToolbarTitle.setText("Výpis hřišť do ListView");

        // načtení předaného playground listu z MainActivity
        playgroundList = getIntent().getParcelableExtra("playgroundList");

        // iniciliazece String listu s výsledky pro vypsání do ListView
        playgroundStringList = playgroundList.getStringArrayList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(ResultListViewActivity.this,
                android.R.layout.simple_list_item_1, playgroundStringList);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);

        // nastvení akce pro ListView -> vyběr konkrétního hřiště
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
                // předání dat o hřištích do activity ResultMapActivity
                Intent intent = new Intent(ResultListViewActivity.this, ResultMapActivity.class);

                intent.putExtra("playgroundList", playgroundList);

                startActivity(intent);
            }
        });

        // nastavení vlastností pro button v toolbaru na seřazení výsledků
        btnToolbarSort = (ImageView) findViewById(R.id.toolbar_sortBtn);
        btnToolbarSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String odpoved = playgroundList.SeradAVypis();

                // iniciliazece String listu s výsledky pro vypsání do ListView v activitě
                playgroundStringList = playgroundList.getStringArrayList();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(ResultListViewActivity.this,
                        android.R.layout.simple_list_item_1, playgroundStringList);

                listView.setAdapter(arrayAdapter);

                Toast.makeText(ResultListViewActivity.this, odpoved, Toast.LENGTH_SHORT).show();
            }
        });

    }
}