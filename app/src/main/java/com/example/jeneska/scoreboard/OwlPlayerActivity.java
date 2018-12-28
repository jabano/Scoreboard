package com.example.jeneska.scoreboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class OwlPlayerActivity extends AppCompatActivity {


    String firstName = null;
    String lastName = null;
    String hometown = null;
    String nationality = null;
    String role = null;
    private TextView fullNameView = null;
    private TextView birthPlaceView = null;
    private TextView nationalityView = null;
    private TextView roleView = null;
    private TextView heroesView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            firstName = extras.getString("firstName");
            lastName = extras.getString("lastName");
            hometown = extras.getString("hometown");
            nationality = extras.getString("nationality");
            role = extras.getString("role");
        }
        fullNameView = findViewById(R.id.fullNameDetail_text);
        fullNameView.setText(firstName + " " + lastName);

        birthPlaceView = findViewById(R.id.birthPlaceDetail_text);
        birthPlaceView.setText(hometown);

        nationalityView = findViewById(R.id.nationalityDetail_text);
        nationalityView.setText(nationality);

        roleView = findViewById(R.id.roleDetail_text);
        roleView.setText(role);

        heroesView = findViewById(R.id.heroesDetail_text);



        //Format
        //Full Name
        //Birth Place
        //Nationality
        //Role
        //Heroes
        //Need to grab heroes played for player info and headshot



    }
}
