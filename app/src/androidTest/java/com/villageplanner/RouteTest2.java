package com.villageplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import static junit.framework.TestCase.assertTrue;

import static org.hamcrest.Matchers.not;
import static java.util.regex.Pattern.matches;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RouteTest2 {
    UiDevice device = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testClickCurrentLocation() {
        UiObject current = device.findObject(new UiSelector().descriptionContains("Current Location"));
        try {
            current.click();
            for (String name : AllStores.stores.keySet()) {
                assertTrue(device.hasObject(By.descContains(name)));
            }
            onView(withId(R.id.cancelButton)).check(ViewAssertions.matches(not(isDisplayed())));
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }
}
