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


    public RosterEvent(int player_id, String name, int player_number, String role, String givenName, String familyName, String hometown, String nationality, int team_id) {
        this.name = name;
        this.number = player_number;
        this.hometown = hometown;
        this.nationality = nationality;
        this.givenName = givenName;
        this.familyName = familyName;
        this.role = role;
        this.player_id = player_id;
        this.team_id = team_id;

    }
    public RosterEvent() {}
    public RosterEvent(int player_id, String name) {
        this.player_id = player_id;
        this.name = name;
    }
    //Getters
    public int getPlayerId() {
        return player_id;
    }
    public String getName() {
        return name;
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
            return "dps";
        }
        return role;
    }
    public int getTeamId() {
        return team_id;
    }


    //Setters
    public void setPlayerId(int player_id) {
        this.player_id = player_id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNumber(int player_number) {this.number = player_number;}
    public void setHometown(String hometown) {this.hometown = hometown;}
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setGivenName(String givenName) {this.givenName = givenName;}
    public void setFamilyName(String familyName) { this.familyName = familyName;    }
    public void setRole(String role) { this.role = role;    }
    public void setTeamId(int team_id) {
        this.team_id = team_id;
    }



}
