package com.villageplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapAPI = googleMap;
        LatLng USCVillage = new LatLng(34.0256, -118.2850);
        mapAPI.addMarker(new MarkerOptions().position(USCVillage).title("USCVillage"));
        mapAPI.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0256, -118.2850), 16.0f));

    }
}