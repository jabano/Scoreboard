package com.example.jeneska.scoreboard;

public class RosterEvent {

    //Name of player
    private String name;

    //PlayerNumber of player
    private int number;

    //Hometown of player
    private String hometown;

    //Nationality of player
    private String nationality;

    //GivenName of player
    private String givenName;

    //FamilyName of player
    private String familyName;

    //role
    private String role;

    //player_id
    private int player_id;

    //team_id
    private int team_id;


    public RosterEvent(int player_id, String name, int number, String role, String givenName, String familyName, String hometown, String nationality,  int team_id) {
        this.name = name;
        this.number = number;
        this.hometown = hometown;
        this.nationality = nationality;
        this.givenName = givenName;
        this.familyName = familyName;
        this.role = role;
        this.player_id = player_id;
        this.team_id = team_id;

    }

    public RosterEvent(int player_id, String name) {
        this.player_id = player_id;
        this.name = name;
    }

    public RosterEvent() {}

    //Getters

    public int getPlayerId() {
        return player_id;
    }

    public void setID(int player_id) {
        this.player_id = player_id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getNumber() {
        return number;
    }

    public String getHometown() {
        return hometown;
    }

    public String getNationality() {
        return nationality;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getRole() {
        if(role.equals("offense")) {
            return "DPS";
        }
        return role;
    }





    public int getTeamId() {
        return team_id;
    }





}
