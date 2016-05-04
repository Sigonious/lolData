package com.example.a.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;

public class Stats extends AppCompatActivity{
    String sumName;
    final String matchURL1 = "https://na.api.pvp.net/api/lol/na/v2.2/matchlist/by-summoner/";
    final String matchURL2 = "?api_key=0fb229c6-5def-4bbb-9be6-067c9ec1b6dd";
    double averageKills, averageDeaths, averageAssists, averageKDA;
    Match[] matchList = new Match[10];
    double totalKills, totalDeaths, totalAssists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //This intent gets the array of Match objects sent from the original activity
        Intent intent = getIntent();
        final Match[] matches = (Match[]) getIntent().getSerializableExtra("matchList");
        sumName = intent.getStringExtra("sumName");

        EditText name = (EditText) findViewById(R.id.summonerName);
        TextView SummonerName = (TextView) findViewById(R.id.summoner);
        TextView KDA = (TextView) findViewById(R.id.KDA);
        TextView Kills = (TextView) findViewById(R.id.Kills);
        TextView Deaths = (TextView) findViewById(R.id.Deaths);
        TextView Assists = (TextView) findViewById(R.id.Assists);
        Button BackButton = (Button) findViewById(R.id.BackButton);
        Button kdaButton = (Button) findViewById(R.id.kdaButton);
        Button killButton = (Button) findViewById(R.id.killButton);
        Button deathButton = (Button) findViewById(R.id.deathButton);
        Button assistButton = (Button) findViewById(R.id.assistButton);

        SummonerName.setText(sumName);

        findAverageKills(matches);
        findAverageDeaths(matches);
        findAverageAssists(matches);
        findAverageKDA(matches);

        Format format = new DecimalFormat("#.00");
        String formattedKDA = format.format(averageKDA);
        KDA.setText(formattedKDA);
        Kills.setText(Double.toString(averageKills));
        Deaths.setText(Double.toString(averageDeaths));
        Assists.setText(Double.toString(averageAssists));

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Stats.this, StartScreen.class));
            }
        });

        kdaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 5/4/2016 create a graph view that displays kda for each game
            }
        });

        killButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 5/4/2016 create a graph view that displays kills for each game
            }
        });

        deathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 5/4/2016 create a graph view that displays deaths for each game
            }
        });

        assistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 5/4/2016 create a graph view that displays assists for each game
            }
        });
    }

    public void findAverageKDA(Match[] matches)
    {
        averageKDA = (totalKills+totalAssists)/totalDeaths;
    }

    public void findAverageKills(Match[] matches)
    {
        totalKills = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalKills += matches[i].getKills();
        }
        averageKills = (double) totalKills/matches.length;
    }

    public void findAverageDeaths(Match[] matches)
    {
        totalDeaths = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalDeaths += matches[i].getDeaths();
        }
        averageDeaths = (double) totalDeaths/matches.length;
    }

    public void findAverageAssists(Match[] matches)
    {
        totalAssists = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalAssists += matches[i].getAssists();
        }
        averageAssists = (double) totalAssists / matches.length;
    }
}