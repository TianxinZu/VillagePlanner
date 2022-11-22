package com.villageplanner;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotSignedInTest {

    public void wait(int millis) {
        long end = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < end);
    }

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule
            = new IntentsTestRule<>(MainActivity.class, true, false);

    @Before
    public void signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Test
    public void testRegisterInvalid() {
        intentsTestRule.launchActivity(new Intent());
        intended(hasComponent(LandPageActivity.class.getName()));
    }
}