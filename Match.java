package com.example.a.finalproject;

import java.io.Serializable;

public class Match implements Serializable{
    private int kills, deaths, assists, matchID, summonerID;
    private double kda;

    Match(){}

    //This method gets the number of kills from the given matchID
    public void setKills(int kills)
    {
        this.kills = kills;
    }

    //This method gets the number of deaths from the given matchID
    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
    }

    public void setAssists(int assists)
    {
        this.assists = assists;
    }

    public void setKDA()
    {
       kda = (kills+assists)/Math.max(1,deaths);
    }

    public void setMatchID(int matchID)
    {
        this.matchID = matchID;
    }

    public int getKills()
    {
        return kills;
    }

    public int getDeaths()
    {
        return deaths;
    }

    public int getAssists()
    {
        return assists;
    }

    public double getKDA()
    {
        return kda;
    }

    public int getSummonerID(){return summonerID;}
}
