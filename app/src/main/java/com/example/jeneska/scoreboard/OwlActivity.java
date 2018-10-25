package com.example.jeneska.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class OwlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        //Roster TextView will have an onClickListener to open the roster activity
        TextView gladsRoster_textView = findViewById(R.id.roster_text);
        gladsRoster_textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent rosterIntent = new Intent(OwlActivity.this, OwlRosterActivity.class);
                startActivity(rosterIntent);
            }
        });
    }
}
