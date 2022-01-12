package com.example.playgrounds01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.playgrounds01.myapp.R;

public class PlaygroundDetailActivity extends AppCompatActivity {

    TextView textView_Souradnice, textViewPopis, textViewRank, textViewVzdalenost;
    PlaygroundClass pgSelected;


    //toolbar komponenty
    TextView textViewToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground_detail);



        // nastavení jména activity
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbat_title);
        textViewToolbarTitle.setText("Výpis detail hřiště");

        //nastavení viditelnosti ikonek toolbaru
        findViewById(R.id.toolbar_mapBtn).setVisibility(View.GONE);
        findViewById(R.id.toolbar_listBtn).setVisibility(View.GONE);
        findViewById(R.id.toolbar_sortBtn).setVisibility(View.GONE);

        textView_Souradnice = findViewById(R.id.textView_Souradnice);
        textViewPopis = findViewById(R.id.textViewPopis);
        textViewRank = findViewById(R.id.textViewRank);
        textViewVzdalenost = findViewById(R.id.textViewVzdalenost);

        pgSelected = getIntent().getParcelableExtra("playground");

        if(pgSelected != null)
        {
            textView_Souradnice.setText("Souřadnice: " + pgSelected.getGps_lat() + ", " + pgSelected.getGps_long());
            textViewPopis.setText("Popis: " + pgSelected.getName());
            textViewRank.setText("Rank: " + pgSelected.getPg_rank());
            textViewVzdalenost.setText("Vzdálenost: " + pgSelected.getVzdalenostKm() + "km");
        }
        else
        {
            Toast.makeText(this, "Nenačten Detail", Toast.LENGTH_LONG).show();
        }


    }
}