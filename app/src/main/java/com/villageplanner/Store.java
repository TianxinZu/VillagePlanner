package com.villageplanner;

import com.google.android.gms.maps.model.LatLng;

public class Store {
    private String name;
    LatLng latlng;
    int walking_time = 8;
    int wait_time = 5;

    public Store() {

    }
    public Store(String name) {
        this.name = name;
    }
    public Store(String name,LatLng latlng) {
        this.name = name;
        this.latlng = latlng;
    }

    public String getName() {
        return this.name;
    }

    public LatLng getLatLng(){ return this.latlng; }

    public void setWalking_time(int time){
        walking_time = time;
    }

    public int getWaiting_time() {
        return wait_time;
    }

    public int getWalking_time(){return walking_time;}

}
