package com.example.jeneska.scoreboard;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.jeneska.scoreboard.data.PlayerContract;
import com.example.jeneska.scoreboard.data.PlayerContract.PlayerEntry;
import com.example.jeneska.scoreboard.data.PlayerDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class RosterActivity extends AppCompatActivity {

    public static final String LOG_TAG = "Roster Activity: ";
    //URL for gladiators roster
    private static final String GLADS_ROSTER_URL = "https://api.overwatchleague.com/teams/4406";
    private PlayerDbHelper mDbHelper;

    //Brings up Roster xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        mDbHelper = new PlayerDbHelper(this);
        GladsAsyncTask task = new GladsAsyncTask();
        task.execute();



    }




    private void updateUi(RosterEvent roster) {
        //Display the player name in the UI
        TextView rosterTextView = findViewById(R.id.playerInfo_text);
        rosterTextView.setText("Name: " + roster.getName() + "\n"
                + "Player Number: " + roster.getNumber() + "\n"
                + "Role: " + roster.getRole() + "\n"
                + "Full Name: " + roster.getGivenName()+ " " + roster.getFamilyName() + "\n"
                + "Hometown: " + roster.getHometown() + ", " + roster.getNationality() + "\n"
                + "Team ID: " + roster.getTeamId() + "\n"
                + "Player ID: " + roster.getPlayerId());

        mDbHelper.addPlayer(new RosterEvent(roster.getPlayerId(), roster.getName()));
        List<RosterEvent> players = mDbHelper.getAllPlayers();


        TextView display = findViewById(R.id.tableCount_text);

        for(RosterEvent r:players) {
            String log = "ID: " + r.getPlayerId() + ", Name: " + r.getName();
            display.setText(log + "\n");
        }




    }

    private class GladsAsyncTask extends AsyncTask<URL, Void, RosterEvent> {

        @Override
        protected RosterEvent doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(GLADS_ROSTER_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            RosterEvent roster = extractFeatureFromJson(jsonResponse);





            // Return the {@link Event} object as the result fo the {@link GladsAsyncTask}
            return roster;
        }

        /**
         * Update the screen with the given roster (which was the result of the
         * {@link GladsAsyncTask}).
         */
        @Override
        protected void onPostExecute(RosterEvent roster) {
            if (roster == null) {
                return;
            }

            updateUi(roster);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a JSON String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null)
                return jsonResponse;

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            int httpResponse = 0;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                httpResponse = urlConnection.getResponseCode();
                if (httpResponse == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
                else {
                    Log.e(LOG_TAG, "Error response code: " + httpResponse);
                }
            } catch (IOException e) {

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {

                    inputStream.close();
                }
            }
            return jsonResponse;
        }


        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link RosterEvent} object by parsing out information
         * about the first player from the input rosterJSON string.
         */
        public RosterEvent extractFeatureFromJson(String rosterJSON) {
            try {
                JSONObject baseJsonResponse = new JSONObject(rosterJSON);
                int team_id = baseJsonResponse.getInt("id");

                JSONArray playersArray = baseJsonResponse.getJSONArray("players");

                // If there are results in the players array
                if (playersArray.length() > 0) {
                    // Extract out the first feature
                    JSONObject playerObject = playersArray.getJSONObject(0);
                    JSONObject attributes = playerObject.getJSONObject("attributes");

                    // Extract out player info from player object
                    String name = playerObject.getString("name");
                    String nationality = playerObject.getString("nationality");
                    String familyName = playerObject.getString("familyName");
                    String givenName = playerObject.getString("givenName");
                    int player_id = playerObject.getInt("id");

                    //Extract out player info from attribute object from player object
                    int player_number = attributes.getInt("player_number");
                    String role = attributes.getString("role");
                    String hometown = attributes.getString("hometown");

                    // Create a new {@link Event} object
                    return new RosterEvent(player_id, name, player_number, role, givenName, familyName, hometown, nationality, team_id);

                }
            } catch (JSONException e) {
                Log.d(LOG_TAG, "Problem parsing the roster JSON results");
            }
            return null;
        }
    }


}
