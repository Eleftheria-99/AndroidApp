package com.example.dit.hua.android.a2ndProject.contentproviderfor2ndproject;

import java.io.Serializable;

public class DataContract2ndProject implements Serializable {
    //One of the main principles of SQL databases is the schema: a formal declaration of how the database is organized.
    // The schema is reflected in the SQL statements that I use to create my database.
    // This class is a companion class, known as a contract class, which explicitly specifies the layout of my schema in a systematic and self-documenting way.
    int id;
    String unix_timestamp;
    String lat;
    String lon ;

    public DataContract2ndProject() {    //default empty constructor
    }

    public DataContract2ndProject(String unix_timestamp, String lat, String lon) {
        this.unix_timestamp = unix_timestamp;
        this.lat = lat;
        this.lon = lon;
    }
    public DataContract2ndProject(int id, String unix_timestamp, String lat, String lon) {
        this.id = id;
        this.unix_timestamp = unix_timestamp;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnix_timestamp() {
        return unix_timestamp;
    }

    public void setUnix_timestamp(String unix_timestamp) {
        this.unix_timestamp = unix_timestamp;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
