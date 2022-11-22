package com.villageplanner;

import static org.junit.Assert.assertEquals;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.Test;

public class GetUrlTest {

    public double getRandomNumber(int min, int max) {
        return ((Math.random() * (max - min)) + min);
    }

//    @Rule
//    public ActivityScenarioRule<MainActivity> activityScenarioRule
//            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testGetUrl() {
//        MainActivity activity = new MainActivity();
        LatLng a = new LatLng(getRandomNumber(-90, 90), getRandomNumber(-180, 180));
        LatLng b = new LatLng(getRandomNumber(-90, 90), getRandomNumber(-180, 180));
        String expected = "https://maps.googleapis.com/maps/api/directions/json?origin=" + a.latitude + "," + a.longitude + "&destination=" + b.latitude + "," + b.longitude + "&sensor=false&mode=walking&&key=AIzaSyDW0IEE05MOrjZxx0ya_RTXXW0hWDGmUR4";
        assertEquals(expected, MainActivity.getDirectionsUrl(a, b));
    }
}
