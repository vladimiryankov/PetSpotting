package com.vladimiryankov.petspotting;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Initialization of the parse server
 */

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable local Datastore
        Parse.enableLocalDatastore(this);

        //Initialization
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("f7cc02d308aba110894bb1c42d6df13cb9baea28")
                .clientKey("1f5c89e2165212f122ffdeb4068e0ffcb45c4c78")
                .server("http://52.59.242.181:80/parse/")
                .build()
        );

        /*
        ParseObject object = new ParseObject("ExampleObject");
        object.put("sampleNumber", "3");
        object.put("sampleString", "start");

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

        ParseUser.enableAutomaticUser();
        */

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
