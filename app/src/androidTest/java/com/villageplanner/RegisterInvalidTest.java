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

import java.util.Random;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterInvalidTest {
    static Random random = new Random();
    static Integer rand1 = random.nextInt(100000);
    static Integer rand2 = random.nextInt(100000);
    public static final String EMAIL = String.format("%d@%d.com", rand1, rand2);
    public static final String USERNAME = "asdadhlajdsn";
    public static final String PASSWORD = "abcde";

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end);
    }

    @Rule
    public IntentsTestRule<RegisterActivity> intentsTestRule
            = new IntentsTestRule<>(RegisterActivity.class);

    @Test
    public void testRegisterInvalid() {
        onView(withId(R.id.emailText))
                .perform(typeText(EMAIL), closeSoftKeyboard());
        onView(withId(R.id.usernameText))
                .perform(typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordText))
                .perform(typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.register))
                .perform(click());
        wait(3000);
        onView(withId(R.id.passwordText)).check(matches(hasErrorText("Password is too short!")));
    }
}