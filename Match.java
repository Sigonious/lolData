package com.example.a.finalproject;

import java.io.Serializable;

/*
**This is the Match class used to store relevant data from the JSON files
* that are found from the RIOT api.
**/


public class Match implements Serializable{
    private int kills, deaths, assists, matchID, creepScore, summonerID;
    private double kda;

    Match(){}

    public void setKills(int kills){this.kills = kills;}

    public void setDeaths(int deaths){this.deaths = deaths;}

    public void setAssists(int assists){ this.assists = assists;}

    public void setKDA(){ kda = (kills+assists)/Math.max(1,deaths);}

    public void setMatchID(int matchID) { this.matchID = matchID;}

    public void setCreepScore(int creepScore){this.creepScore = creepScore;}

    public int getKills()
    {
        return kills;
    }

    public int getDeaths()
    {
        return deaths;
    }

    public int getAssists(){return assists; }

    public double getKDA(){return kda;}

    public int getSummonerID(){return summonerID;}

    public int getCreepScore(){return creepScore;}
}
