package com.vladimiryankov.petspotting.data;


import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Pet {
    private String address;
    private double latitude;
    private double longitude;
    private String user;
    private String userName;
    private long datetime;

    public Pet(String address, double latitude, double longitude, String user, String userName) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.userName = userName;
        this.datetime = System.currentTimeMillis()/1000;
    }

    public Pet(LatLng place, String address, ParseUser user){
        this.address = address;
        this.latitude = place.latitude;
        this.longitude = place.longitude;
        this.user = user.toString();
        this.userName = user.getUsername();
        this.datetime = System.currentTimeMillis()/1000;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getUserName() {
        return userName;
    }

    public long getDatetime() {
        return datetime;
    }

    public String getAddress() {

        return address;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault());
        return "Pet lost/found on:" + this.address +
                " latitude:" + this.latitude +
                " longitude:" + this.longitude +
                " by " + this.userName +
                " at " + sdf.format(this.datetime);
    }
}
