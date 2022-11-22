package com.villageplanner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static junit.framework.TestCase.assertFalse;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QueueTimeDisplayTest {
    UiDevice device = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testQueueTimeDisplay() {
        UiObject marker = device.findObject(new UiSelector().descriptionContains("CAVA"));
        try {
            marker.click();
            UiObject snippet = device.findObject(new UiSelector().descriptionContains("Queue Time: "));
            assertFalse(snippet == null);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }
}
