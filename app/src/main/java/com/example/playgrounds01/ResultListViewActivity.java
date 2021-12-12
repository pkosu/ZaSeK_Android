package com.example.playgrounds01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.playgrounds01.myapp.R;

import java.util.ArrayList;

public class ResultListViewActivity extends AppCompatActivity {


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

        // nastavení jména activity
        this.setTitle("Výpis hřišť do ListView ");

        Bundle extras = getIntent().getBundleExtra("bundle");

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

        
        listView = findViewById(R.id.list_view);

        playgroundStringList = playgroundList.getStringArrayList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(ResultListViewActivity.this,
                android.R.layout.simple_list_item_1, playgroundStringList);

        listView.setAdapter(arrayAdapter);
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

}