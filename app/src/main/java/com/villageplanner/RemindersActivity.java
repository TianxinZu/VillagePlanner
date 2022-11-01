package com.villageplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RemindersActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase root;
    private String userid;
    final String USER_TABLE = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(RemindersActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            userid = auth.getCurrentUser().getUid();
        }

        // Display current reminders
        root.getReference(USER_TABLE).child(userid).child("reminders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reminder reminder = snapshot.getValue(Reminder.class);
                    System.out.println(reminder.getUnixTimestamp());
                    System.out.println(reminder.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

}