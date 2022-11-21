package com.villageplanner;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.EditText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterFailTest {
    public static Matcher<View> hasNoErrorText() {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has no error text: ");
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                return view.getError() == null;
            }
        };
    }
    public static final String EMAIL = "a@a.com";
    public static final String USERNAME = "asdadhlajdsn";
    public static final String PASSWORD = "aaaaaa";

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end);
    }

    @Rule
    public IntentsTestRule<RegisterActivity> intentsTestRule
            = new IntentsTestRule<>(RegisterActivity.class);

    @Test
    public void testRegisterFail() {
        onView(withId(R.id.emailText))
                .perform(typeText(EMAIL), closeSoftKeyboard());
        onView(withId(R.id.usernameText))
                .perform(typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordText))
                .perform(typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.register))
                .perform(click());
        wait(3000);
        // No intent (can still find emailText) and no error, can not register because of duplicate email
        onView(withId(R.id.emailText)).check(matches(hasNoErrorText()));
    }
}