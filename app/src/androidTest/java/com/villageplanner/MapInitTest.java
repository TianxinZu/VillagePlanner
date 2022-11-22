package com.villageplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.not;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class MapInitTest {
    UiDevice device = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCancelButton() {
        onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testMarkers() {
        ArrayList<UiObject> markers = new ArrayList<>();
        for (String name : AllStores.stores.keySet()) {
            markers.add(device.findObject(new UiSelector().descriptionContains(name)));
        }
        for (UiObject marker : markers) {
            assertNotNull(marker);
        }
        for (String name : AllStores.stores.keySet()) {
            assertTrue(device.hasObject(By.descContains(name)));
        }
        UiObject currentLocation = device.findObject(new UiSelector().descriptionContains("Current Location"));
        assertNotNull(currentLocation);
        assertTrue(device.hasObject(By.descContains("Current Location")));
    }
}
