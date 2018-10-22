package com.example.jeneska.scoreboard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import com.example.jeneska.scoreboard.R;
import com.example.jeneska.scoreboard.RosterActivity;
import com.example.jeneska.scoreboard.RosterEvent;
import com.example.jeneska.scoreboard.data.PlayerContract.PlayerEntry;

import java.util.ArrayList;
import java.util.List;

public class PlayerDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "roster.db";

    public PlayerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create table string
    private static final String SQL_CREATE_PLAYER_TABLE = "CREATE TABLE " + PlayerEntry.PLAYER_TABLE + " (" +
            PlayerEntry.COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY, " +
            PlayerEntry.COLUMN_PLAYER_NAME + " TEXT)";

    //Drop table string
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PlayerEntry.PLAYER_TABLE;

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PLAYER_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //cache for online data
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addPlayer(RosterEvent roster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_ID, roster.getPlayerId());
        values.put(PlayerEntry.COLUMN_PLAYER_NAME, roster.getName());

        db.insert(PlayerEntry.PLAYER_TABLE, null, values);
        db.close();

    }

    RosterEvent getPlayer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String [] projection = {
          PlayerEntry.COLUMN_PLAYER_ID,
          PlayerEntry.COLUMN_PLAYER_NAME
        };

        String selection = PlayerEntry.COLUMN_PLAYER_ID + "=?";

        Cursor cursor = db.query(PlayerEntry.PLAYER_TABLE, projection, selection, new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        RosterEvent roster = new RosterEvent(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

        return roster;
    }

    public List<RosterEvent> getAllPlayers() {
        List<RosterEvent> rosterList = new ArrayList<RosterEvent>();

        String selectQuery = "SELECT * FROM " + PlayerEntry.PLAYER_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                RosterEvent roster = new RosterEvent();
                roster.setID(Integer.parseInt(cursor.getString(0)));
                roster.setName(cursor.getString(1));

                rosterList.add(roster);
            } while(cursor.moveToNext());
        }

        return rosterList;
    }

    public int getPlayerCount() {

        String countQuery = "SELECT * FROM " + PlayerEntry.PLAYER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }









}
