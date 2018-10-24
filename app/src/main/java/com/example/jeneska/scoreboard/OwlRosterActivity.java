package com.example.jeneska.scoreboard;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.jeneska.scoreboard.http.httpRequest;
import com.example.jeneska.scoreboard.data.PlayerDbHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class OwlRosterActivity extends AppCompatActivity {
    private static final String GLADS_ROSTER_URL = "https://api.overwatchleague.com/teams/4406";
    public static final String LOG_TAG = "Owl Roster Activity: ";
    private OwlAsyncTask mAsyncTask;
    private PlayerDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        mAsyncTask = new OwlAsyncTask();
        mAsyncTask.execute();
        mDbHelper = new PlayerDbHelper(this);


    }


    private void updateUi(ArrayList<OwlRosterEvent> roster) {
        //Display the player name in the UI
        TextView rosterTextView = findViewById(R.id.playerInfo_text);

        rosterTextView.setText("Name: " + roster.get(0).getName() + "\n" + "Size of roster table: " + roster.size());

        for (int i = 0; i < roster.size(); i++) {
            mDbHelper.addPlayer(new OwlRosterEvent(roster.get(i).getPlayerId(), roster.get(i).getName(), roster.get(i).getNumber(), roster.get(i).getRole(), roster.get(i).getGivenName(), roster.get(i).getFamilyName(),
                    roster.get(i).getHometown(), roster.get(i).getNationality(), roster.get(i).getTeamId()));


        }


        List<OwlRosterEvent> players = mDbHelper.getAllPlayers();


        TextView display = findViewById(R.id.tableCount_text);


        for (OwlRosterEvent r : players) {
            String log = "Name: " + r.getName() + "\n"
                    + "Player Number: " + r.getNumber() + "\n"
                    + "Role: " + r.getRole() + "\n"
                    + "Full Name: " + r.getGivenName() + " " + r.getFamilyName() + "\n"
                    + "Hometown: " + r.getHometown() + ", " + r.getNationality() + "\n"
                    + "Team ID: " + r.getTeamId() + "\n"
                    + "Player ID: " + r.getPlayerId();
            display.setText(log + "\n\n");
        }
    }





    public class OwlAsyncTask extends AsyncTask<URL, Void, ArrayList<OwlRosterEvent>> {



        @Override
        protected ArrayList<OwlRosterEvent> doInBackground(URL... urls) {
            // Create URL object
            URL url = httpRequest.createUrl(GLADS_ROSTER_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = httpRequest.makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
                Log.d(LOG_TAG, "ERROR in reading JSON");
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            ArrayList<OwlRosterEvent> roster = extractPlayersFromJson(jsonResponse);



            return roster;
        }

        @Override
        protected void onPostExecute(ArrayList<OwlRosterEvent> roster) {
            if (roster == null) {
                return;
            }

            updateUi(roster);
        }



        /**
         * Return an {@link OwlRosterEvent} object by parsing out information
         * about the first player from the input rosterJSON string.
         */
        public ArrayList<OwlRosterEvent> extractPlayersFromJson(String rosterJSON) {

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
                    roster.add(player);
                }

                return roster;

            } catch (JSONException e) {
                Log.d(LOG_TAG, "Problem parsing the roster JSON results", e);

            }
            return null;
        }
    }





}
