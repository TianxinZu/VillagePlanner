package com.villageplanner;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class AllStores {

    static Map<String, Store> stores;

    public static void initiate() {
        stores = new HashMap<>();
        LatLng l = new LatLng(34.02509105209718, -118.28452043192465);
        Store s = new Store("CAVA", l);
        s.setWalking_time(5);
        stores.put("CAVA", s);

        LatLng l1 = new LatLng(34.025503176232014, -118.28535818258696);
        Store s1 = new Store("Dulce", l1);
        s.setWalking_time(10);
        stores.put("Dulce", s1);

        LatLng l2 = new LatLng(34.02502880426814, -118.28534289035802);
        Store s2 = new Store("Insomnia Cookies", l2);
        s.setWalking_time(0);
        stores.put("Insomnia Cookies", s2);

        LatLng l3 = new LatLng(34.02466780225075, -118.28569566468875);
        Store s3 = new Store("SUPAMU", l3);
        s.setWalking_time(7);
        stores.put("SUPAMU", s3);

        LatLng l4 = new LatLng(34.02455900049386, -118.28538935242644);
        Store s4 = new Store("Sunlife Organics", l4);
        s.setWalking_time(4);
        stores.put("Sunlife Organics", s4);

        LatLng l5 = new LatLng(34.0242612207765, -118.28420760939493);
        Store s5 = new Store("Rock & Reilly's", l5);
        s.setWalking_time(2);
        stores.put("Rock & Reilly's", s5);

        LatLng l6 = new LatLng(34.02460072922757, -118.2840309620744);
        Store s6 = new Store("Chinese Street Food", l6);
        s.setWalking_time(1);
        stores.put("Chinese Street Food", s6);

        LatLng l7 = new LatLng(34.024786259294224, -118.28470557210025);
        Store s7 = new Store("Stout Burgers & Beers", l7);
        s.setWalking_time(2);
        stores.put("Stout Burgers & Beers", s7);

        LatLng l8 = new LatLng(34.024738299179795, -118.2852476966357);
        Store s8 = new Store("Greenleaf Kitchen & Cocktails", l8);
        s.setWalking_time(1);
        stores.put("Greenleaf Kitchen & Cocktails", s8);
    }
}
