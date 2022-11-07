package com.villageplanner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private LatLng currentLocation;
    private LatLng targetLocation;
    private LatLng USCVillage;
    public ArrayList<Store> allStore;
    final Handler overallHandler = new Handler();
    private FirebaseAuth auth;
    private FirebaseDatabase root;
    private String userid;
    final String USER_TABLE = "Users";
    ImageView avatar;

    public interface GetCurrentLocation {
        void onComplete(LatLng currentLocation);
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points;
            PolylineOptions lineOptions = new PolylineOptions();
//            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            mapAPI.addPolyline(lineOptions);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLocation);
            builder.include(targetLocation);
            LatLngBounds bound = builder.build();
            mapAPI.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 300), 1500, null);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        // Get current user avatar
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        avatar = findViewById(R.id.avatar);
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            userid = auth.getCurrentUser().getUid();
        }
        root.getReference(USER_TABLE).child(userid).child("imageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrl = (String) dataSnapshot.getValue();
                if (imageUrl.isEmpty()) {
                    imageUrl = "http://www.gravatar.com/avatar/?d=mp";
                }
                Glide.with(MainActivity.this).load(imageUrl).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

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
        getDeviceLocation(current -> {
//            mapAPI.getUiSettings().setMyLocationButtonEnabled(true);
            currentLocation = current;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(USCVillage);
            builder.include(currentLocation);
            LatLngBounds bound = builder.build();
            for(int i = 0;i<allStore.size();i++){
                mapAPI.addMarker(new MarkerOptions().position(allStore.get(i).getLatLng()).title(allStore.get(i).getName()));
            }
            mapAPI.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 200), 1500, null);
        });

        mapAPI.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerName = marker.getTitle();
                if (markerName == "Current Location") {
                    return false;
                }
                targetLocation = marker.getPosition();
                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(currentLocation, targetLocation);
                System.out.println(url);
                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

                return true;
            }
        });
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
                            mapAPI.addMarker(new MarkerOptions().position(currentLocation).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";

        String key = "&key=AIzaSyDW0IEE05MOrjZxx0ya_RTXXW0hWDGmUR4";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void goToReminders(View view) {
        Intent intent = new Intent(MainActivity.this, RemindersActivity.class);
        startActivity(intent);
    }
}