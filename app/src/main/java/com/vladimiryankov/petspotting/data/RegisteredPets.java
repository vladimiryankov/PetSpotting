package com.vladimiryankov.petspotting.data;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class RegisteredPets<T extends Pet> {
    private final String name;
    private List<Pet> petsList;
    private AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish();
    }

    public RegisteredPets(String name, AsyncResponse delegate) {
        this.name = name;
        this.petsList = new ArrayList<>();
        this.delegate = delegate;
    }

    public Collection<Pet> getPets() { return petsList; }

    public void retrievePets() {
        ParseQuery<ParseObject> query = new ParseQuery<>("LostPet");
        query.orderByDescending("createdAt");
        Log.i("Retrieve", "Lost Pets");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        Pet retrievedPet = new Pet(object.getString("address"),
                                object.getDouble("latitude"),
                                object.getDouble("longitude"),
                                object.getString("user"),
                                object.getString("username"));
                        petsList.add(retrievedPet);
                    }
                    delegate.processFinish();
                } else {
                    Log.i("Callback error", e.getMessage());
                }
            }

        });
    }
}
