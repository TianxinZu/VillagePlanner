package com.villageplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static junit.framework.TestCase.assertTrue;

import android.content.ContentValues;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.maps.GoogleMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ZoomTest {
    UiDevice device = UiDevice.getInstance(getInstrumentation());

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testZoom() {
        float initzoom = MainActivity.initzoom;
        UiObject marker = device.findObject(new UiSelector().descriptionContains("CAVA"));
        try {
            marker.click();
            float routezoom = MainActivity.routezoom;
            assertTrue(routezoom > initzoom);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }
}
