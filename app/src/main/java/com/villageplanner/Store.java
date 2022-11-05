package com.villageplanner;

import com.google.android.gms.maps.model.LatLng;

public class Store {
    private String name;
    LatLng latlng;
    int unixTimestamp = -1;
    int wait_time;

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

    public void setUnixTimestamp(int time){
        unixTimestamp = time;
    }

    public int getWaiting_time() {
        return wait_time;
    }

    public int getUnixTimestamp(){return unixTimestamp;}

}
