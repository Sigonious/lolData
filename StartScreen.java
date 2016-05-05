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
        import android.widget.ProgressBar;
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
        import org.w3c.dom.Text;

        import android.os.AsyncTask;
        import android.widget.Toast;

public class StartScreen extends AppCompatActivity{

    //These URLs are used to retrieve data from the RIOT api. The documents for this can be
    //found at https://developer.riotgames.com/api/methods
    final String summonerURL1 = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/";
    final String summonerURL2 = "?api_key=0fb229c6-5def-4bbb-9be6-067c9ec1b6dd";
    final String matchURL1 = "https://na.api.pvp.net/api/lol/na/v2.2/matchlist/by-summoner/";
    final String matchURL2 = "?rankedQueues=RANKED_SOLO_5x5&beginIndex=0&endIndex=10&api_key=0fb229c6-5def-4bbb-9be6-067c9ec1b6dd";
    final String matchInfoURL1 = "https://na.api.pvp.net/api/lol/na/v2.2/match/";
    final String matchInfoURL2 = "?api_key=0fb229c6-5def-4bbb-9be6-067c9ec1b6dd";
    String stringID = "not initialized";
    String sumName;
    EditText sumsum;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        //Creating objects from activity screen
        final TextView update1 = (TextView) findViewById(R.id.update1);
        final TextView update2 = (TextView) findViewById(R.id.update2);
        final Button yesButton = (Button) findViewById(R.id.yesButton);
        final Button noButton = (Button) findViewById(R.id.noButton);
        final TextView text1 = (TextView) findViewById(R.id.text1);
        final TextView text2 = (TextView) findViewById(R.id.textView);
        sumsum = (EditText) findViewById(R.id.summonerName);
        final Button submit = (Button)findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        yesButton.setVisibility(View.INVISIBLE);
        noButton.setVisibility(View.INVISIBLE);
        update1.setVisibility(View.INVISIBLE);
        update2.setVisibility(View.INVISIBLE);

        //onclick listener for the first button on screen
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumName = sumsum.getText().toString();
                hideSoftKeyboard(StartScreen.this);
                sumsum.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                text1.setVisibility(View.INVISIBLE);
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                update1.setVisibility(View.VISIBLE);
                update2.setVisibility(View.VISIBLE);
            }
        });

        //using this button will call the retrievefeedtask function to start getting data
        //through the RIOT api.
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sumsum.getText().toString().contains(" ")) {
                    Toast.makeText(StartScreen.this, "Enter a name without spaces.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sumsum.getText().toString().equals("")) {
                    Toast.makeText(StartScreen.this, "Please enter a name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                text1.setVisibility(View.VISIBLE);
                text1.setText("Getting match history for: ");
                text2.setText(sumName);
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                update1.setVisibility(View.GONE);
                update2.setVisibility(View.GONE);
                new RetrieveFeedTask().execute();
            }
        });

        //Using this button will skip the api and use data stored within
        //the local SQLite database
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toStats = new Intent(getBaseContext(), Stats.class);
                startActivity(toStats);
            }
        });
    }

    //This function pulls the summonerID from the RIOT api using the string
    //entered in the text field from the initial screen
    class RetrieveFeedTask extends AsyncTask<String, String, String> {

        private Exception exception;
        int summID;

        protected void onPreExecute() {

        }

        //Gets the JSON String from the api
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

        //Uses the JSON string to find the summoner ID
        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
                Intent intent = new Intent(getBaseContext(), StartScreen.class);
                Toast.makeText(getBaseContext(), "Name not found", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(getBaseContext(), StartScreen.class);
                Toast.makeText(getBaseContext(), "Name not found", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        }
    }

    //Bridge between the retrievedatafeed thread and the getMatchList thread
    private void sendData(int data)
    {
        stringID = Integer.toString(data);
        System.out.println(stringID);
        new getMatchList().execute(stringID);
    }

    //This function uses the summonerID found from the previous step, and looks
    //for the first 10 ranked matches from the match list of the summonerID
    class getMatchList extends AsyncTask<String, String, String> {

        private Exception exception;
        String[] matchIDs = new String[10];

        protected void onPreExecute() {

        }

        //This method is to get the JSON String of the last 10 matches
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

        //This function uses the list of matches from the previous function to find
        //the match ID of each item in the list
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
                Intent intent = new Intent(getBaseContext(), StartScreen.class);
                Toast.makeText(getBaseContext(), "Not enough ranked games found.", Toast.LENGTH_LONG).show();
                startActivity(intent);
                Log.e("JSON ERROR", e.getMessage(), e);
                finish();
            }
        }
    }

    //Another bridge between the previous thread and the getMatchInfo thread
    private void sendMatches(String[] matches)
    {
        new getMatchInfo().execute(matches);
    }

    //This thread takes the array of match IDs and uses each one to find the details
    //of the respective matches and stores them into a Match object type. It then places
    //all of them into a Match[] array.
    class getMatchInfo extends AsyncTask<String, String, Match[]> {

        Match[] listOfMatches = new Match[10];

        //Shows a progress bar to indicate that the thread is working through the match list.
        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(), "Getting data for recent matches.",Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);
        }

        //This function does all of the heavy processing of the thread. It sends a total of 10
        //requests to the RIOT api, one for each matchID. Within the same loop, it then parses
        //through the match details in order to find relevant information, and stores that
        //data in a Match[] array. There is a 2.5 second delay between each iteration of the for
        //loop in order to limit the rate at which requests are sent to the URL. There is a limit of
        //10 requests every 10 seconds, so a 2.5 second wait is a safe amount for this program.
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
                    int creepScore = (int) playerStats.get("minionsKilled");
                    Match match1 = new Match();
                    match1.setKills(kills);
                    match1.setDeaths(deaths);
                    match1.setAssists(assists);
                    match1.setKDA();
                    match1.setMatchID(matchIDasInt);
                    match1.setCreepScore(creepScore);
                    listOfMatches[i] = match1;
                } catch (JSONException e) {
                    Log.e("JSON ERROR", e.getMessage(), e);
                }
            }
            return listOfMatches;
            }

            //This simply sends the data from the thread to the final function of this class.
            protected void onPostExecute(Match[] response)
            {
                progressBar.setVisibility(View.GONE);
                //get response and pass to finishStartSceen method to send to Stats.java
                StartScreen.this.finishStartScreen(response);
            }
        }

    //This is the final method of the class. It's job is to receive the Match[] array
    //and bundle both the array and the summoner name to send to the Stats activity.
    private void finishStartScreen(Match[] matchesToSend)
    {
        Intent intent = new Intent(getBaseContext(), Stats.class);
        System.out.println(sumName);
        intent.putExtra("sumName", sumName);
        intent.putExtra("matchList",matchesToSend);
        startActivity(intent);
        finish();
    }

    //This method is used to hide the keyboard when the first button is pressed.
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}