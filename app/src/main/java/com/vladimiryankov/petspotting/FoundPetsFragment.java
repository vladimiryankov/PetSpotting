package com.vladimiryankov.petspotting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vladimiryankov.petspotting.data.Pet;
import com.vladimiryankov.petspotting.data.RegisteredPets;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoundPetsFragment extends Fragment implements RegisteredPets.AsyncResponse {
    RegisteredPets<Pet> foundPets;
    ArrayAdapter foundPetsAdapter;
    ListView foundPetsListView;
    List<String> registeredPetsList;


    public FoundPetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_found_pets, null, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foundPetsListView = (ListView) view.findViewById(R.id.foundPetsListView);

        registeredPetsList = new ArrayList<>();
        foundPets = new RegisteredPets<>("foundPets", this);


        foundPetsAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, registeredPetsList);
//        foundPetsListView.setAdapter(foundPetsAdapter);
        Pet testPet = new Pet("tuka", 43.23, 32.43, "user123", "gosho");
        Log.i("TestPet", testPet.toString());

        Log.i("InitalizationSize:", Integer.toString(foundPets.getPets().size()));

        foundPets.retrievePets();

//        for (Pet pet : foundPets.getPets()) {
//            Log.i("Test", "hello");
//            System.out.println(pet.toString());
//            registeredPetsList.add(pet.toString());
//        }

    }

    @Override
    public void processFinish() {
        Log.i("InitalizationSize:", Integer.toString(foundPets.getPets().size()));
        for (Pet pet: foundPets.getPets()) {
            Log.i("Pet:", pet.toString());
            registeredPetsList.add(pet.toString());
        }
        foundPetsListView.setAdapter(foundPetsAdapter);
    }
}
