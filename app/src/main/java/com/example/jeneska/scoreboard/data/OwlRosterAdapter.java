package com.example.jeneska.scoreboard.data;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jeneska.scoreboard.OwlRosterEvent;
import com.example.jeneska.scoreboard.R;

import java.util.ArrayList;

public class OwlRosterAdapter extends ArrayAdapter<OwlRosterEvent> {

    public OwlRosterAdapter(Activity context, ArrayList<OwlRosterEvent> roster) {
        super(context, 0, roster);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listRosterView = convertView;
        if(listRosterView == null) {
            listRosterView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_roster, parent, false);
        }


        OwlRosterEvent currentPlayer = getItem(position);

        // Find the TextView in the list_roster.xml layout with the player_number id
        TextView playerNumTextView = listRosterView.findViewById(R.id.player_number_text);

        // set this text on the name TextView
        playerNumTextView.setText(String.valueOf(currentPlayer.getNumber()));

        // Find the TextView in the list_roster.xml layout with the name id
        TextView nameTextView = listRosterView.findViewById(R.id.name_text);

        // set this text on the name TextView
        nameTextView.setText(currentPlayer.getName());

        // Find the TextView in the list_roster.xml layout with the role id
        TextView roleTextView = listRosterView.findViewById(R.id.role_text);

        // set this text on the name TextView
        roleTextView.setText(currentPlayer.getRole());


        // Return the whole list item layout (containing 2 TextViews)
        // so that it can be shown in the ListView
        return listRosterView;
    }



}
