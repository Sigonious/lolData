package com.example.a.finalproject;

public class Match {
    private int kills, deaths, assists, matchID, summonerID;
    private double kda;

    Match(int matchID, int summonerID)
    {
        this.matchID = matchID;
        this.summonerID = summonerID;
    }

    //This method gets the number of kills from the given matchID
    public void setKills()
    {
        //pull kills from match through riot api
    }

    //This method gets the number of deaths from the given matchID
    public void setDeaths()
    {
        //pull deaths from match through riot api
    }

    public void setAssists()
    {
        //pull assists from match through riot api
    }

    public void setKDA()
    {
        kda = (kills+assists)/deaths;
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
}
