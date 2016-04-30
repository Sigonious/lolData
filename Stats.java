package com.example.a.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.media.Image;

public class Stats extends AppCompatActivity{
    int summonerID;
    double averageKills, averageDeaths, averageAssists, averageKDA;
    Match[] matchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        String sumName = intent.getStringExtra("blah");

        EditText name = (EditText) findViewById(R.id.summonerName);
        TextView SummonerName = (TextView) findViewById(R.id.summoner);
        TextView KDA = (TextView) findViewById(R.id.KDA);
        TextView Kills = (TextView) findViewById(R.id.Kills);
        TextView Deaths = (TextView) findViewById(R.id.Deaths);
        TextView Assists = (TextView) findViewById(R.id.Assists);
        Button BackButton = (Button) findViewById(R.id.BackButton);

        SummonerName.setText(sumName);

        //get summonerID from the API
        //summonerID = ???

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Stats.this, StartScreen.class));
            }
        });
    }

    public void findAverageKDA(Match[] matches)
    {
        double totalKDA = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalKDA += matches[i].getKDA();
        }
        averageKDA = totalKDA / matches.length;
    }

    public void findAverageKills(Match[] matches)
    {
        int totalKills = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalKills += matches[i].getKills();
        }
        averageKills = totalKills/matches.length;
    }

    public void findAverageDeaths(Match[] matches)
    {
        int totalDeaths = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalDeaths += matches[i].getDeaths();
        }
        averageDeaths = totalDeaths/matches.length;
    }

    public void findAverageAssists(Match[] matches)
    {
        int totalAssists = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalAssists += matches[i].getAssists();
        }
        averageAssists = totalAssists / matches.length;
    }
}
