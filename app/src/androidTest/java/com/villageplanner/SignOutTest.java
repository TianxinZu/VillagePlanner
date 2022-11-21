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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseAuth;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignOutTest {
    public static final String EMAIL = "a@a.com";
    public static final String PASSWORD = "aaaaaa";

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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Assert the user is logged in
        assertTrue(auth.getCurrentUser() != null);
        onView(withId(R.id.signOut))
                .perform(click());
        wait(3000);
        // Assert the user is logged out
        assertTrue(auth.getCurrentUser() == null);
        // And that the user is redirected to landing page
        intended(hasComponent(LandPageActivity.class.getName()));
    }
}