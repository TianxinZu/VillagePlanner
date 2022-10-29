package com.villageplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandPageActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land_page);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        System.out.println(user.getEmail());
//        if (user != null) {
//            mAuth.signOut();
//        }
    }

    public void register(View view) {
        // navigate to register page
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        // navigate to log in page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
