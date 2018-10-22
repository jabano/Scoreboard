package com.example.jeneska.scoreboard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //To start a new activity for LiverpoolActivity
        TextView liverpool = findViewById(R.id.liverpool);
        liverpool.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent liverpoolIntent = new Intent(MainActivity.this, LiverpoolActivity.class);
                startActivity(liverpoolIntent);
            }
         });

        TextView glads = findViewById(R.id.glads);
        glads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gladsIntent = new Intent(MainActivity.this, GladsActivity.class);
                startActivity(gladsIntent);
            }
        });
    }
}
