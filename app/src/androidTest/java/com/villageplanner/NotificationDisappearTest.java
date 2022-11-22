package com.villageplanner;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.app.Activity;
import android.widget.TextView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotificationDisappearTest {
    public static final String EMAIL = "l@l.com";
    public static final String PASSWORD = "llllll";
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

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end);
    }

    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule
            = new IntentsTestRule<>(LoginActivity.class);

    @Test
    public void testRunTimer(){
        onView(withId(R.id.emailTextLogin))
                .perform(typeText(EMAIL), closeSoftKeyboard());
        onView(withId(R.id.passwordTextLogin))
                .perform(typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.login))
                .perform(click());
        wait(3000);
        onView(withId(R.id.goToReminders))
                .perform(click());
        wait(3000);
        onView(withId(R.id.addReminder))
                .perform(click());
        wait(3000);
        Date a=new Date();
        a.setTime(System.currentTimeMillis()+(60*11*1000));
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        String strDate = dateFormat.format(a);
        onView(withId(R.id.nameText))
                .perform(typeText("Eat"), closeSoftKeyboard());
        onView(withId(R.id.storeNameText))
                .perform(typeText("CAVA"), closeSoftKeyboard());
        onView(withId(R.id.hourAndMinuteText))
                .perform(typeText(strDate), closeSoftKeyboard());
        onView(withId(R.id.addReminder2)).perform(click());
        onView(withId(R.id.returnToHome)).perform(click());
        wait(12000);
        TextView tv = getActivityInstance().findViewById(R.id.NotificationID);
        assertEquals(0, tv.getText().length());
    }
}
