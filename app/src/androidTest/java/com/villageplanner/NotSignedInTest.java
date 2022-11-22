package com.villageplanner;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;

import static org.junit.Assert.assertEquals;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Collection;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotSignedInTest {

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end);
    }

    static Activity currentActivity;

    public static Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivities =
                    ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
            if (resumedActivities.iterator().hasNext()){
                currentActivity = resumedActivities.iterator().next();
            }
        });
        return currentActivity;
    }

    // Initialize the activity to main without logging in
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
            }
        }

    };

    // Expect to redirect to LandPage
    @Test
    public void testRegisterInvalid() {
        assertEquals(getActivityInstance().getClass().getSimpleName(), "LandPageActivity");
    }
}