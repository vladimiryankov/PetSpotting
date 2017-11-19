package com.vladimiryankov.petspotting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.vladimiryankov.petspotting.Utils.AsyncResponse;
import com.vladimiryankov.petspotting.data.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LostPetsFragment extends Fragment{

    public LostPetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lost_pets, null, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView lostPetsListView = (ListView) getActivity().findViewById(R.id.lostPetsListView);

        final ArrayList<String> petsList = new ArrayList<>();
        final ArrayList<ParseObject> lostPets = new ArrayList<>();

        final ArrayAdapter petsAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, petsList);



        // TODO move to DAO and implement interface delegate to notify the main thread when the asynctask is ready
        //ArrayList<ParseObject> lostPets = DAO.getLostPets();
        ParseQuery<ParseObject> query = new ParseQuery<>("LostPet");
        query.orderByDescending("createdAt");
        Log.i("Retrieve", "Lost Pets");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject pet : objects) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");

                        petsList.add("Date: " + sdf.format(pet.getCreatedAt()) + "\nAddress: " + pet.get("address") + "\nUser: " + pet.get("username"));
                        lostPets.add(pet);
                    }
                    lostPetsListView.setAdapter(petsAdapter);
                } else {
                    Log.i("Callback error", e.getMessage());
                }
            }

        });

        lostPetsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // navigate to the pet on map
                Intent intent = new Intent(getActivity().getApplicationContext(), getActivity().getClass());
                intent.putExtra("latitude", lostPets.get(i).getDouble("latitude"));
                intent.putExtra("longitude", lostPets.get(i).getDouble("longitude"));
                startActivity(intent);

            }
        });

    }

}
