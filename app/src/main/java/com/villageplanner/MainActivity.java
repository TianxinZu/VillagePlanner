package com.villageplanner;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private LatLng currentLocation;
    private LatLng USCVillage;
    public ArrayList<Store> allStore;
    final Handler overallHandler = new Handler();

    public interface GetCurrentLocation {
        void onComplete(LatLng currentLocation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        allStore = new ArrayList<Store>();
        setStoreInformation();
        USCVillage = new LatLng(34.02676429367275, -118.28502793334087);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapAPI = googleMap;
        getLocationPermission();
        getDeviceLocation(new GetCurrentLocation() {
            @Override
            public void onComplete(LatLng current) {
                currentLocation = current;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(USCVillage);
                builder.include(currentLocation);
                LatLngBounds bound = builder.build();
                for(int i = 0;i<allStore.size();i++){
                    mapAPI.addMarker(new MarkerOptions().position(allStore.get(i).getLatLng()).title(allStore.get(i).getName()));
                }
                mapAPI.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 25), 1000, null);
            }
        });
//        while (currentLocation == null) {
//            try {
//                Thread.sleep(100);
//                System.out.println("sleep");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        for(int i = 0;i<allStore.size();i++){
//            mapAPI.addMarker(new MarkerOptions().position(allStore.get(i).getLatLng()).title(allStore.get(i).getName()));
//        }
        System.out.println("Added stores");
//        System.out.println(currentLocation == null);
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(USCVillage);
//        builder.include(currentLocation);
//        LatLngBounds bound = builder.build();
//        mapAPI.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 25), 1000, null);
    }

    public void runMultiTime(){
        overallHandler.post(new Runnable() {
            @Override
            public void run() {
                Notification ntf = new Notification();
                while(!ntf.runTimer()){
                    overallHandler.postDelayed(this,1000);
                }
                String content = ntf.getContent();
                TextView notification = findViewById(R.id.NotificationID);
                notification.setText(content);
                overallHandler.postDelayed(this,5000);
            }
        });
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

    private void getDeviceLocation(GetCurrentLocation function) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        System.out.println("permission");
        System.out.println(locationPermissionGranted);
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }

                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                });
                locationResult.addOnCompleteListener(this, task -> {
                    System.out.println("task successful");
                    System.out.println(task.isSuccessful());
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        Location current = task.getResult();
                        if (current != null) {
                            LatLng currentLocation = new LatLng(current.getLatitude(),
                                    current.getLongitude());
                            mapAPI.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
//                            mapAPI.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                            builder.include(USCVillage);
//                            builder.include(currentLocation);
//                            LatLngBounds bound = builder.build();
//                            mapAPI.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 25), 1000, null);
                            if (function != null) {
                                function.onComplete(currentLocation);
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    private void setStoreInformation(){
        LatLng l = new LatLng(34.02509105209718, -118.28452043192465);
        Store s = new Store("CAVA", l);
        allStore.add(s);

        LatLng l1 = new LatLng(34.025503176232014, -118.28535818258696);
        Store s1 = new Store("Dulce", l1);
        allStore.add(s1);

        LatLng l2 = new LatLng(34.02502880426814, -118.28534289035802);
        Store s2 = new Store("Insomnia Cookies", l2);
        allStore.add(s2);

        LatLng l3 = new LatLng(34.02466780225075, -118.28569566468875);
        Store s3 = new Store("SUPAMU", l3);
        allStore.add(s3);

        LatLng l4 = new LatLng(34.02455900049386, -118.28538935242644);
        Store s4 = new Store("Sunlife Organics", l4);
        allStore.add(s4);

        LatLng l5 = new LatLng(34.0242612207765, -118.28420760939493);
        Store s5 = new Store("Rock & Reilly's", l5);
        allStore.add(s5);

        LatLng l6 = new LatLng(34.02460072922757, -118.2840309620744);
        Store s6 = new Store("Chinese Street Food", l6);
        allStore.add(s6);

        LatLng l7 = new LatLng(34.024786259294224, -118.28470557210025);
        Store s7 = new Store("Stout Burgers & Beers", l7);
        allStore.add(s7);

        LatLng l8 = new LatLng(34.024738299179795, -118.2852476966357);
        Store s8 = new Store("Greenleaf Kitchen & Cocktails", l8);
        allStore.add(s8);
    }


}