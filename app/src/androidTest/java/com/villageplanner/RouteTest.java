package com.villageplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import com.google.android.gms.maps.model.LatLng;

import org.checkerframework.checker.guieffect.qual.UI;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;

@RunWith(AndroidJUnit4.class)
public class RouteTest {
    UiDevice device = UiDevice.getInstance(getInstrumentation());

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testRoute() {
        String name = "CAVA";
        try {
            UiObject marker = device.findObject(new UiSelector().descriptionContains(name));
            marker.click();
            onView(withId(R.id.cancelButton)).check(matches(isDisplayed()));
            wait(5000);
//            for (String s : AllStores.stores.keySet()) {
//                if (s != name) {
//                    System.out.println(s);
//                    assertFalse(device.hasObject(By.descContains(s)));
//                }
//            }
            onView(withId(R.id.cancelButton)).perform(click());
            wait(500);
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
            for (String s : AllStores.stores.keySet()) {
                assertTrue(device.hasObject(By.descContains(s)));
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
}
