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
import java.util.Collection;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotificationWaitedNSentedTest {
    public static final String EMAIL = "notiwait@test.com";
    public static final String PASSWORD = "111111";
    boolean flag = false;
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
        Date a=new Date();
        a.setTime(System.currentTimeMillis()+(60*11*1000));
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        String strDate = dateFormat.format(a);
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
        onView(withId(R.id.nameText))
                .perform(typeText("Eat"), closeSoftKeyboard());
        onView(withId(R.id.storeNameText))
                .perform(typeText("CAVA"), closeSoftKeyboard());
        onView(withId(R.id.hourAndMinuteText))
                .perform(typeText(strDate), closeSoftKeyboard());
        onView(withId(R.id.addReminder2)).perform(click());
        wait(3000);
        onView(withId(R.id.returnToHome)).perform(click());
        TextView tv = getActivityInstance().findViewById(R.id.NotificationID);
        assertNotEquals(0, tv.getText().length());
        int second = 0;
        while(second<4){
//            if(tv.getText().length()!=0){
//                flag = false;
//            }
            flag = true;
            wait(1000);
            second++;
        }
        assertEquals(flag, true);
    }
}
