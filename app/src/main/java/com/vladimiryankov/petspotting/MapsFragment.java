package com.vladimiryankov.petspotting;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.Address;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.vladimiryankov.petspotting.data.DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    GoogleMap gMap;

    public LocationManager locationManager;

    public LocationListener locationListener;


    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public Location getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                startListening();
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.petMap);
        mapFragment.getMapAsync(this);
    }

    public void centerMapOnLocation(Location location, String title) {
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        gMap.clear();
        gMap.addMarker(new MarkerOptions().position(currentPosition).title(title));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void initializeMapMarkers(GoogleMap gMap, LatLng location, String address) {
        gMap.addMarker(new MarkerOptions()
                .position(location)
                .title(address)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.dog_40px)));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        Location location = getCurrentLocation();
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        //Load the pet locations
        ParseQuery<ParseObject> query = new ParseQuery<>("LostPet");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject pet : objects) {
                        initializeMapMarkers(gMap, new LatLng(pet.getDouble("latitude"), pet.getDouble("longitude")), pet.getString("address") );
                    }
                } else {
                    Log.i("Callback error", e.getMessage());
                }
            }

        });

        Intent intent = getActivity().getIntent();

        if (intent.getExtras() != null) {
            Log.i("Intent", "Received intent");
            LatLng receivedLocation = new LatLng(intent.getDoubleExtra("latitude", 23), intent.getDoubleExtra("longitude", 42));
            gMap.addMarker(new MarkerOptions().position(currentPosition).title("You are here"));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(receivedLocation));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        } else {


            if (location != null) {
                centerMapOnLocation(location, "You are here");
            } else {
                Toast.makeText(getActivity(), "Unable to extract location", Toast.LENGTH_SHORT).show();
            }
        }

        gMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng place) {

        Geocoder geocoder = new Geocoder(getActivity().getApplication(), Locale.getDefault());
        String address = "";

        try {
            List<Address> addressesList = geocoder.getFromLocation(place.latitude, place.longitude, 1);

            if (addressesList != null && addressesList.size() > 0) {

                Log.i("geo adress: ", addressesList.get(0).toString());
                address += addressesList.get(0).getThoroughfare() + " " + addressesList.get(0).getFeatureName();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gMap.addMarker(new MarkerOptions()
                .position(place)
                .title(address)
                .icon(BitmapDescriptorFactory
                .fromResource(R.drawable.dog_40px)));

        //add place to parse server
        DAO.addLostPet(place, address, ParseUser.getCurrentUser());

        Log.i("place:",place.toString());

    }
}
