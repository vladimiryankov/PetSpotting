package com.vladimiryankov.petspotting.data;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vladimiryankov.petspotting.Utils.AsyncResponse;

import java.util.ArrayList;
import java.util.List;


public class DAO{

    // Collections
    private static List<ParseObject> lostPets = getLostPets();

    private static List<ParseObject> foundPets = new ArrayList<>();

    private static List<ParseObject> allPets = new ArrayList<ParseObject>() {
        {
            addAll(lostPets);
            addAll(foundPets);
        }
    };

    private static List<ParseUser> users = new ArrayList<>();



    // Collection getters
    public static ArrayList<ParseObject> getLostPets(){
        final ArrayList<ParseObject> lostPets = new ArrayList<>();
        ParseQuery<ParseObject> query = new ParseQuery<>("LostPet");
        query.orderByDescending("createdAt");
        Log.i("Retrieve", "Lost Pets");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        Log.i("Query Pet", object.toString());
                        lostPets.add(object);

                    }

                } else {
                    Log.i("Callback error", e.getMessage());
                }
            }

        });

        return lostPets;

    }

    // Add objects methods
    public static void addLostPet(LatLng place, String address, ParseUser user) {
        ParseObject object = new ParseObject("LostPet");
        object.put("address", address.toString());
        object.put("latitude", place.latitude);
        object.put("longitude", place.longitude);
        object.put("user", user.toString());
        object.put("username", user.getUsername());
        object.put("datetime", String.valueOf(System.currentTimeMillis()/1000));


        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Parse Result", "Successful");
                } else {
                    Log.i("Parse Result", "Failed: " + e.getMessage());
                }
            }
        });

    }




    // Remove objects methods

    // Update objects methods
 }
