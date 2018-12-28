package com.example.jeneska.scoreboard;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jeneska.scoreboard.data.OwlRosterAdapter;
import com.example.jeneska.scoreboard.http.httpRequest;
import com.example.jeneska.scoreboard.data.DbHelper;

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
    private DbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        mAsyncTask = new OwlAsyncTask();
        mDbHelper = new DbHelper(this);
        long currentDate = System.currentTimeMillis()/1000;
        long dbDate = mDbHelper.getUpdatedDate();

        //If database is empty, call asynctask to update database
        if(mDbHelper.dbEmpty()) {
            mAsyncTask.execute();
            dbDate = mDbHelper.getUpdatedDate();
        }

        if(dbDate != 0) {
            currentDate -= 604800; //604800 is 1 week in seconds
            if(currentDate >= dbDate) {
                //update current date field in db
                mAsyncTask.execute();
                //mAsyncTask, check if data exists, if so, drop table
            }
            else {
                updateUi(mDbHelper);
            }
        }
    }

    private void updateUi(DbHelper dbHelper) {

      final ArrayList<OwlRosterEvent> roster = dbHelper.getAllPlayers();
      OwlRosterAdapter adapter = new OwlRosterAdapter(this, roster);
      ListView listView = findViewById(R.id.roster_activity);
      listView.setAdapter(adapter);

      //Call PlayerActivity screen
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //Create bundle object for extras
              Bundle extras = new Bundle();
              //Full Name, firstName, lastName
              extras.putString("firstName", roster.get(position).getGivenName());
              extras.putString("lastName", roster.get(position).getFamilyName());
              //Birth Place
              extras.putString("hometown", roster.get(position).getHometown());
              //Nationality
              extras.putString("nationality", roster.get(position).getNationality());
              //Role
              extras.putString("role", roster.get(position).getRole());

              Intent playerIntent = new Intent(OwlRosterActivity.this, OwlPlayerActivity.class);
              playerIntent.putExtras(extras);
              startActivity(playerIntent);
          }
      });
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
                    //primary key
                    int player_id = playerObject.getInt("id");

                    //Check for table attributes
                    String name = null;
                    if(playerObject.has("name")) {
                        name = playerObject.getString("name");
                    }

                    String hometown = null;
                    if(playerObject.has("homeLocation")) {
                        hometown = playerObject.getString("homeLocation");
                    }

                    String nationality = null;
                    if(playerObject.has("nationality")) {
                        nationality = playerObject.getString("nationality");
                    }

                    String familyName = null;
                    if(playerObject.has("familyName")) {
                        familyName = playerObject.getString("familyName");
                    }

                    String givenName = null;
                    if(playerObject.has("givenName")) {
                        givenName = playerObject.getString("givenName");
                    }

                    //Extract out player info from attribute object from player object
                    int player_number = 0;
                    if(attributes.has("player_number")) {
                        player_number = attributes.getInt("player_number");
                    }

                    String role = null;
                    if(attributes.has("role")) {
                        role = attributes.getString("role");
                    }



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
