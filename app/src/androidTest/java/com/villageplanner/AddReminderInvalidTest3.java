package com.villageplanner;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

// Test no reminder name provided
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddReminderInvalidTest3 {
    public static final String EMAIL = "a@a.com";
    public static final String PASSWORD = "aaaaaa";

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end) ;
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
        onView(withId(R.id.addReminder))
                .perform(click());
        wait(3000);
        onView(withId(R.id.nameText))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.storeNameText))
                .perform(typeText("CAVA"), closeSoftKeyboard());
        onView(withId(R.id.hourAndMinuteText))
                .perform(typeText("23:59"), closeSoftKeyboard());
        onView(withId(R.id.addReminder2))
                .perform(click());
        wait(3000);
        onView(withId(R.id.nameText)).check(matches(hasErrorText("Please input a reminder name!")));
    }
}