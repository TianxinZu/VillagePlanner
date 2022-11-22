package com.villageplanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class RouteInfoTest {

    @Test
    public void testRoute() {
//        MainActivity activity = new MainActivity();
        LatLng HollyWood = new LatLng(34.13426247549595, -118.32155248069621);
        LatLng Leavey = new LatLng(34.02190525742033, -118.28275803298074);
        String result;
        try {
            result = MainActivity.downloadUrl(MainActivity.getDirectionsUrl(HollyWood, Leavey));
            assertTrue(result.contains("\"status\" : \"OK\""));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
