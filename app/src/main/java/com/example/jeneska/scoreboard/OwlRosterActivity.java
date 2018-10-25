package com.example.jeneska.scoreboard;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jeneska.scoreboard.data.OwlRosterAdapter;
import com.example.jeneska.scoreboard.http.httpRequest;
import com.example.jeneska.scoreboard.data.PlayerDbHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



public class OwlRosterActivity extends AppCompatActivity {
    private static final String GLADS_ROSTER_URL = "https://api.overwatchleague.com/teams/4406";
    public static final String LOG_TAG = "OwlRosterActivity: ";
    private OwlAsyncTask mAsyncTask;
    private PlayerDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        mAsyncTask = new OwlAsyncTask();
        mDbHelper = new PlayerDbHelper(this);

        //If database is empty, call Glads_Roster_Url to update database
        if(mDbHelper.dbEmpty()) {
            mAsyncTask.execute();
        }

        //otherwise, updateUi with database if players table has data
        else {

            updateUi(mDbHelper);
        }


    }


    private void updateUi(PlayerDbHelper dbHelper) {

      ArrayList<OwlRosterEvent> roster = dbHelper.getAllPlayers();
      OwlRosterAdapter adapter = new OwlRosterAdapter(this, roster);
      ListView listView = findViewById(R.id.roster_activity);
      listView.setAdapter(adapter);



    }



    private class OwlAsyncTask extends AsyncTask<URL, Void, ArrayList<OwlRosterEvent>> {



        @Override
        protected ArrayList<OwlRosterEvent> doInBackground(URL... urls) {
            // Create URL object
            URL url = httpRequest.createUrl(GLADS_ROSTER_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            // Extract relevant fields from the JSON response and create an {@link Event} object


            try {

                jsonResponse = httpRequest.makeHttpRequest(url);



            } catch (IOException e) {
                // TODO Handle the IOException
                Log.d(LOG_TAG, "ERROR in reading JSON");
            }

            ArrayList<OwlRosterEvent> roster = extractPlayersFromJson(jsonResponse);



            return roster;
        }


        @Override
        protected void onPostExecute(ArrayList<OwlRosterEvent> roster) {
            if (roster == null) {
                return;
            }

            updateUi(mDbHelper);
        }




        /**
         * Return an {@link OwlRosterEvent} object by parsing out information
         * about the players from the input rosterJSON string.
         */
        private ArrayList<OwlRosterEvent> extractPlayersFromJson(String rosterJSON) {

            ArrayList<OwlRosterEvent> roster = new ArrayList<OwlRosterEvent>();

            try {
                JSONObject baseJsonResponse = new JSONObject(rosterJSON);
                JSONArray playersArray = baseJsonResponse.getJSONArray("players");
                int team_id = baseJsonResponse.getInt("id");


                for (int i = 0; i < playersArray.length(); i++) {


                    //Go to player object
                    JSONObject playerObject = playersArray.getJSONObject(i);
                    //Go to nested attributes in player object
                    JSONObject attributes = playerObject.getJSONObject("attributes");

                    // Extract out player info from player object
                    int player_id = playerObject.getInt("id");
                    String name = playerObject.getString("name");
                    String hometown = playerObject.getString("homeLocation");
                    String nationality = playerObject.getString("nationality");
                    String familyName = playerObject.getString("familyName");
                    String givenName = playerObject.getString("givenName");


                    //Extract out player info from attribute object from player object
                    int player_number = attributes.getInt("player_number");
                    String role = attributes.getString("role");


                    OwlRosterEvent player = new OwlRosterEvent(player_id, name, player_number, role, givenName, familyName, hometown, nationality, team_id);
                    //Add player to array list
                    roster.add(player);
                    //Add player to db
                    mDbHelper.addPlayer(player);


                }

                return roster;

            } catch (JSONException e) {
                Log.d(LOG_TAG, "Problem parsing the roster JSON results", e);

            }

            return null;

        }
    }





}
