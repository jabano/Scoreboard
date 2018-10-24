package com.example.jeneska.scoreboard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jeneska.scoreboard.OwlRosterEvent;
import com.example.jeneska.scoreboard.data.PlayerContract.PlayerEntry;

import java.util.ArrayList;
import java.util.List;

public class PlayerDbHelper extends SQLiteOpenHelper {
    public static String LOG_TAG = "PlayerDbHelper: ";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "roster.db";

    public PlayerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create table string
    private static final String SQL_CREATE_PLAYER_TABLE = "CREATE TABLE " + PlayerEntry.PLAYER_TABLE + " (" +
            PlayerEntry.COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY, " +
            PlayerEntry.COLUMN_PLAYER_NAME + " TEXT, " +
            PlayerEntry.COLUMN_PLAYER_NUMBER + " INTEGER, " +
            PlayerEntry.COLUMN_ROLE + " TEXT, " +
            PlayerEntry.COLUMN_GIVEN_NAME + " TEXT, " +
            PlayerEntry.COLUMN_FAMILY_NAME + " TEXT, " +
            PlayerEntry.COLUMN_HOMETOWN + " TEXT, " +
            PlayerEntry.COLUMN_NATIONALITY + " TEXT, " +
            PlayerEntry.COLUMN_TEAM_ID + " INTEGER)";



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

    public boolean dbEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + PlayerEntry.PLAYER_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 0) {
            return true;
        }


        return false;

    }

    public void addPlayer(OwlRosterEvent roster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_ID, roster.getPlayerId());
        values.put(PlayerEntry.COLUMN_PLAYER_NAME, roster.getName());
        values.put(PlayerEntry.COLUMN_PLAYER_NUMBER, roster.getNumber());
        values.put(PlayerEntry.COLUMN_ROLE, roster.getRole());
        values.put(PlayerEntry.COLUMN_GIVEN_NAME, roster.getGivenName());
        values.put(PlayerEntry.COLUMN_FAMILY_NAME, roster.getFamilyName());
        values.put(PlayerEntry.COLUMN_HOMETOWN, roster.getHometown());
        values.put(PlayerEntry.COLUMN_NATIONALITY, roster.getNationality());
        values.put(PlayerEntry.COLUMN_TEAM_ID, roster.getTeamId());

        db.insert(PlayerEntry.PLAYER_TABLE, null, values);
        db.close();

    }

    public OwlRosterEvent getPlayer(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String [] projection = {
                PlayerEntry.COLUMN_PLAYER_ID, //0, int
                PlayerEntry.COLUMN_PLAYER_NAME, //1
                PlayerEntry.COLUMN_PLAYER_NUMBER, //2, int
                PlayerEntry.COLUMN_ROLE, //3
                PlayerEntry.COLUMN_GIVEN_NAME, //4
                PlayerEntry.COLUMN_FAMILY_NAME, //5
                PlayerEntry.COLUMN_HOMETOWN, //6
                PlayerEntry.COLUMN_NATIONALITY, //7
                PlayerEntry.COLUMN_TEAM_ID //8, int
        };

        String selection = PlayerEntry.COLUMN_PLAYER_ID + "=?";

        Cursor cursor = db.query(PlayerEntry.PLAYER_TABLE, projection, selection, new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        OwlRosterEvent roster = new OwlRosterEvent(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7),Integer.parseInt(cursor.getString(8)));

        cursor.close();


        return roster;
    }

    public List<OwlRosterEvent> getAllPlayers() {
        List<OwlRosterEvent> rosterList = new ArrayList<OwlRosterEvent>();

        String selectQuery = "SELECT * FROM " + PlayerEntry.PLAYER_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                OwlRosterEvent roster = new OwlRosterEvent();
                roster.setPlayerId(Integer.parseInt(cursor.getString(0)));
                roster.setName(cursor.getString(1));
                roster.setNumber(Integer.parseInt(cursor.getString(2)));
                roster.setRole(cursor.getString(3));
                roster.setGivenName(cursor.getString(4));
                roster.setFamilyName(cursor.getString(5));
                roster.setHometown(cursor.getString(6));
                roster.setNationality(cursor.getString(7));
                roster.setTeamId(Integer.parseInt(cursor.getString(8)));

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
