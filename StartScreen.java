package com.example.a.finalproject;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.CountDownTimer;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.util.Log;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import org.json.JSONTokener;

        import android.os.AsyncTask;
        import android.widget.Toast;

public class StartScreen extends AppCompatActivity{
    final String summonerURL1 = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/";
    final String summonerURL2 = "?api_key=0fb229c6-5def-4bbb-9be6-067c9ec1b6dd";
    final String matchURL1 = "https://na.api.pvp.net/api/lol/na/v2.2/matchlist/by-summoner/";
    final String matchURL2 = "?rankedQueues=RANKED_SOLO_5x5&beginIndex=0&endIndex=10&api_key=0fb229c6-5def-4bbb-9be6-067c9ec1b6dd";
    final String matchInfoURL1 = "https://na.api.pvp.net/api/lol/na/v2.2/match/";
    final String matchInfoURL2 = "?api_key=0fb229c6-5def-4bbb-9be6-067c9ec1b6dd";
    String stringID = "not initialized";
    String sumName;
    EditText sumsum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        sumsum = (EditText) findViewById(R.id.summonerName);
        Button submit = (Button)findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(StartScreen.this);
                sumName = sumsum.getText().toString();
                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<String, String, String> {

        private Exception exception;
        int summID;

        protected void onPreExecute() {

        }

        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(summonerURL1 + sumName + summonerURL2);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("THE RESPONSE IS", response);

            try {
                JSONObject objecto = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject object = objecto.getJSONObject(sumName.toLowerCase());
                summID =(int) object.get("id");
                System.out.println(summID);
                StartScreen.this.sendData(summID);
            } catch (JSONException e) {
                Log.e("JSON ERROR", e.getMessage(), e);
            }
        }
    }

    private void sendData(int data)
    {
        stringID = Integer.toString(data);
        System.out.println(stringID);
        new getMatchList().execute(stringID);
    }

    class getMatchList extends AsyncTask<String, String, String> {

        private Exception exception;
        String[] matchIDs = new String[10];

        protected void onPreExecute() {

        }

        protected String doInBackground(String... ID) {
            String ID1 = ID[0];
            try {
                URL url = new URL(matchURL1 + ID1 + matchURL2);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("THE RESPONSE IS", response);

            try {
                JSONObject jArray = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray jsonArray = jArray.getJSONArray("matches");
                for(int i = 0; i<jsonArray.length(); i++)
                {
                    JSONObject singleMatch = jsonArray.getJSONObject(i);
                    matchIDs[i] = Integer.toString((Integer) singleMatch.get("matchId"));
                }
                StartScreen.this.sendMatches(matchIDs);
            } catch (JSONException e) {
                Log.e("JSON ERROR", e.getMessage(), e);
            }
        }
    }

    private void sendMatches(String[] matches)
    {
        new getMatchInfo().execute(matches);
    }

    class getMatchInfo extends AsyncTask<String, String, Match[]> {

        Match[] listOfMatches = new Match[10];

        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(), "Getting data for recent matches.",Toast.LENGTH_LONG).show();
        }

        protected Match[] doInBackground(String... matchID)
        {
            for(int i = 0; i<10; i++)
            {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Iteration count for getMatchInfo: " + i);

                String match = matchID[i];
                String temp = "";
                try {
                    URL url = new URL(matchInfoURL1 + match + matchInfoURL2);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        temp = stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
                try {
                    int partID = -1;
                    JSONObject match2 = (JSONObject) new JSONTokener(temp).nextValue();
                    JSONArray pIDs = (JSONArray) match2.getJSONArray("participantIdentities");
                    JSONArray parts = (JSONArray) match2.getJSONArray("participants");
                    for(int j = 0; j<pIDs.length(); j++) {
                        JSONObject checkForPlayer = pIDs.getJSONObject(j);
                        JSONObject player = checkForPlayer.getJSONObject("player");
                        String checkSumName = player.getString("summonerName");
                        if(checkSumName.toLowerCase().equals(sumName)) {
                            partID = (int) checkForPlayer.get("participantId");
                            break;
                        }
                    }
                    System.out.println("LOOK HERE FOR SUMMONER NAME: "+sumName);
                    System.out.println("LOOK HERE FOR PARTICIPANT ID: "+partID);
                    if(partID < 1)
                    {
                        throw new ArrayIndexOutOfBoundsException(partID);
                    }
                    JSONObject playerInfo = (JSONObject) parts.get(partID - 1);
                    JSONObject playerStats = (JSONObject) playerInfo.get("stats");
                    int kills = (int) playerStats.get("kills");
                    int deaths = (int) playerStats.get("deaths");
                    int assists = (int) playerStats.get("assists");
                    int matchIDasInt = Integer.parseInt(match);
                    Match match1 = new Match();
                    match1.setKills(kills);
                    match1.setDeaths(deaths);
                    match1.setAssists(assists);
                    match1.setKDA();
                    match1.setMatchID(matchIDasInt);
                    listOfMatches[i] = match1;
                } catch (JSONException e) {
                    Log.e("JSON ERROR", e.getMessage(), e);
                }
            }
            return listOfMatches;
            }

            protected void onPostExecute(Match[] response)
            {
                //get response and pass to finishStartSceen method to send to Stats.java
                StartScreen.this.finishStartScreen(response);
            }
        }

    private void finishStartScreen(Match[] matchesToSend)
    {
        Intent intent = new Intent(getBaseContext(), Stats.class);
        System.out.println(sumName);
        intent.putExtra("sumName", sumName);
        intent.putExtra("matchList",matchesToSend);
        startActivity(intent);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}