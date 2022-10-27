package com.villageplanner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LandPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land_page);

    }

    public void register() {
        // navigate to register page
    }

    public void login() {
        // navigate to log in page
    }
}
