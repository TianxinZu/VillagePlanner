package com.villageplanner;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.runner.lifecycle.Stage.RESUMED;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Collection;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeleteReminderTest {
    public static final String EMAIL = "b@b.com";
    public static final String PASSWORD = "bbbbbb";
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
    public void testAddReminderSuccess() {
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
        LinearLayout reminders = getActivityInstance().findViewById(R.id.reminders);
        RelativeLayout entry = (RelativeLayout) reminders.getChildAt(0);
        Button delete = (Button) entry.getChildAt(1);
        delete.callOnClick();
        wait(1000);
        // b always has 1 reminder, after deleting the only one it becomes 0
        assertEquals(reminders.getChildCount(), 0);
        onView(withId(R.id.addReminder))
                .perform(click());
        wait(3000);
        // Add back the reminder so b could be reused
        onView(withId(R.id.nameText))
                .perform(typeText("Eat"), closeSoftKeyboard());
        onView(withId(R.id.storeNameText))
                .perform(typeText("CAVA"), closeSoftKeyboard());
        onView(withId(R.id.hourAndMinuteText))
                .perform(typeText("23:59"), closeSoftKeyboard());
        onView(withId(R.id.addReminder2))
                .perform(click());
        wait(3000);
    }
}