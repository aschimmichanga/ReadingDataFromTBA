package com.aschimmichanga.readingjsonfromtba_;

import org.json.JSONArray;
import org.json.JSONObject;

public class Match {
    private JSONArray blueTeams;
    private JSONArray redTeams;
    private JSONObject alliance;
    private JSONObject blue;
    private JSONObject red;
    private String imageurl;
    private String key;


    public JSONArray getBlueTeams() {
        return blueTeams;
    }

    public JSONArray getRedTeams() {
        return redTeams;
    }

    public String getKey() {
        return key;
    }

    public String getImageurl() {
        return imageurl;
    }

}