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
    double averageKills, averageDeaths, averageAssists, averageKDA, averageCreepScore;
    Match[] matchList = new Match[10];
    double totalKills, totalDeaths, totalAssists, totalCreepScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //This intent gets the array of Match objects sent from the original activity
        Intent intent = getIntent();
        final Match[] matches = (Match[]) getIntent().getSerializableExtra("matchList");
        sumName = intent.getStringExtra("sumName");

        //Creating objects from activity_stats.xml
        EditText name = (EditText) findViewById(R.id.summonerName);
        TextView SummonerName = (TextView) findViewById(R.id.summoner);
        TextView KDA = (TextView) findViewById(R.id.KDA);
        TextView Kills = (TextView) findViewById(R.id.Kills);
        TextView Deaths = (TextView) findViewById(R.id.Deaths);
        TextView Assists = (TextView) findViewById(R.id.Assists);
        TextView Creeps = (TextView) findViewById(R.id.Creeps);
        Button BackButton = (Button) findViewById(R.id.BackButton);
        Button kdaButton = (Button) findViewById(R.id.kdaButton);
        Button killButton = (Button) findViewById(R.id.killButton);
        Button deathButton = (Button) findViewById(R.id.deathButton);
        Button assistButton = (Button) findViewById(R.id.assistButton);
        Button creepButton = (Button) findViewById(R.id.creepButton);

        SummonerName.setText(sumName);

        //Assigning value to all of the averages
        findAverageKills(matches);
        findAverageDeaths(matches);
        findAverageAssists(matches);
        findAverageKDA();
        findAverageCreepScore(matches);

        //Formatting used to stop KDA from being excessively long
        Format format = new DecimalFormat("#.00");

        //Setting the text of all text boxes
        String formattedKDA = format.format(averageKDA);
        KDA.setText(formattedKDA);
        Kills.setText(Double.toString(averageKills));
        Deaths.setText(Double.toString(averageDeaths));
        Assists.setText(Double.toString(averageAssists));
        Creeps.setText(Double.toString(averageCreepScore));

        //This button returns to the start screen
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Stats.this, StartScreen.class));
            }
        });

        //
        // The following buttons each link to their respective classes that
        // create a graph based on the data type associated with the button name
        //

        kdaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kdaGraph = new Intent(getBaseContext(), kdaGraph.class);
                kdaGraph.putExtra("matches",matches);
                startActivity(kdaGraph);
            }
        });

        killButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent killsGraph = new Intent(getBaseContext(), killsGraph.class);
                killsGraph.putExtra("matches",matches);
                startActivity(killsGraph);
            }
        });

        deathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deathsGraph = new Intent(getBaseContext(), deathsGraph.class);
                deathsGraph.putExtra("matches",matches);
                startActivity(deathsGraph);
            }
        });

        assistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assistsGraph = new Intent(getBaseContext(), assistsGraph.class);
                assistsGraph.putExtra("matches",matches);
                startActivity(assistsGraph);
            }
        });

        creepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creepGraph = new Intent(getBaseContext(), creepGraph.class);
                creepGraph.putExtra("matches", matches);
                startActivity(creepGraph);
            }
        });
    }

    public void findAverageKDA()
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
        averageKills = totalKills/matches.length;
    }

    public void findAverageDeaths(Match[] matches)
    {
        totalDeaths = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalDeaths += matches[i].getDeaths();
        }
        averageDeaths = totalDeaths/matches.length;
    }

    public void findAverageAssists(Match[] matches)
    {
        totalAssists = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalAssists += matches[i].getAssists();
        }
        averageAssists = totalAssists / matches.length;
    }

    public void findAverageCreepScore(Match[] matches)
    {
        totalCreepScore = 0;
        for(int i = 0; i<matches.length; i++)
        {
            totalCreepScore += matches[i].getCreepScore();
        }
        averageCreepScore = totalCreepScore / matches.length;
    }
}