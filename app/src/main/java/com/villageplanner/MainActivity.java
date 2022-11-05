package com.villageplanner;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.villageplanner.*;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    public ArrayList<Store> allStore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        while(true){
            Notification ntf = new Notification();
            ntf.runTimer();
            String content = ntf.getContent();
            ConstraintLayout notification = findViewById(R.id.NotificationID);
            TextView textView = new TextView(MainActivity.this);
            textView.setText(content);
            notification.addView(textView);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapAPI = googleMap;
        setStoreInformation();
        for(int i = 0;i<allStore.size();i++){
            mapAPI.addMarker(new MarkerOptions().position(allStore.get(i).getLatLng()).title(allStore.get(i).getName()));
        }
        LatLng USCVillage = new LatLng(34.0256, -118.2850);
        mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(USCVillage, 17.0f));

        getLocationPermission();
        getDeviceLocation();
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        System.out.println(locationPermissionGranted == true);
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    System.out.println(task.isSuccessful() == false);
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        Location currentLocation = task.getResult();
//                        assert currentLocation == null : "null";
                        System.out.println(currentLocation == null);
                        if (currentLocation != null) {
                            LatLng current = new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude());
                            mapAPI.addMarker(new MarkerOptions().position(current).title("Current Location"));
                            mapAPI.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    current, 15));
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    private void setStoreInformation(){
        LatLng l = new LatLng(34.031689,-118.283974);
        Store s = new Store("CAVA", l);
        allStore.add(s);

        LatLng l1 = new LatLng(34.026550,-118.285301);
        Store s1 = new Store("Dulce", l1);
        allStore.add(s1);

        LatLng l2 = new LatLng(34.02412414550781,-118.28485107421875);
        Store s2 = new Store("Insomnia Cookies", l2);
        allStore.add(s2);

        LatLng l3 = new LatLng(34.024551,-118.285728);
        Store s3 = new Store("SUPAMU", l3);
        allStore.add(s3);

        LatLng l4 = new LatLng(34.024551,-118.285728);
        Store s4 = new Store("Sunlife Organics", l4);
        allStore.add(s4);

        LatLng l5 = new LatLng(34.0248908996582,-118.28385925292969);
        Store s5 = new Store("Rock & Reilly's", l5);
        allStore.add(s5);

        LatLng l6 = new LatLng(34.025513,-118.283875);
        Store s6 = new Store("Chinese Street Food", l6);
        allStore.add(s6);

        LatLng l7 = new LatLng(34.02412414550781,-118.28485107421875);
        Store s7 = new Store("Stout Burgers & Beers", l7);
        allStore.add(s7);

        LatLng l8 = new LatLng(34.024539,-118.285729);
        Store s8 = new Store("Greenleaf Kitchen & Cocktails", l8);
        allStore.add(s8);
    }

//    private void getStoreInformation(){
//    // Define a Place ID.
//        ArrayList<String> allStore = null;
//        allStore.add("ChIJpQUiEOXHwoARYdQHdFZNSkE");//Insomina Cookie && sunflower balabala
//        allStore.add("ChIJXeE1rOXHwoARFlhR9K3q2mE");//CAVA
//        allStore.add("ChIJTUtPz1bHwoARxfhZXhvzisU");//Rock & Reilly's USC Village
//        allStore.add("EiwzMjAxIFMgSG9vdmVyIFN0LCBMb3MgQW5nZWxlcywgQ0EgOTAwMDcsIFVTQSIxEi8KFAoSCfv5DrPlx8KAEftWkI4W8sI1EIEZKhQKEgll8gdwmsfCgBFM2Usb5-twVw");//Starbucks
//        allStore.add("EjgzMDk2IE1jQ2xpbnRvY2sgQXZlIHN0ZSAxNDIwLCBMb3MgQW5nZWxlcywgQ0EgOTAwMDcsIFVTQSIkGiIKFgoUChIJm2f_hO_HwoARvUQX0clU1GkSCHN0ZSAxNDIw");//Dulce
//        final List<List<Place.Field>> storeInPlaceClass = null;
//        for(int i = 0;i<allStore.size();i++){
//            final String placeId =allStore.get(i);
//            // Specify the fields to return.
//            final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
//
//            // Construct a request object, passing the place ID and fields array.
//            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
//
//            PlacesClient placesClient = null;
//            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
//                Place place = response.getPlace();
//                Log.i(TAG, "Place found: " + place.getName());
//            }).addOnFailureListener((exception) -> {
//                if (exception instanceof ApiException) {
//                    final ApiException apiException = (ApiException) exception;
//                    Log.e(TAG, "Place not found: " + exception.getMessage());
//                    final int statusCode = apiException.getStatusCode();
//                    // TODO: Handle error with given status code.
//                }
//            });
//            storeInPlaceClass.add(placeFields);
//        }
//
//        for(int i = 0;i<storeInPlaceClass.size();i++){
//            if(i == 0){
//               Store s = new Store(storeInPlaceClass.get(i).get(1));
////                Store s(storeInPlaceClass.get(i).get(1).name());
//            }
//        }
//    }

}