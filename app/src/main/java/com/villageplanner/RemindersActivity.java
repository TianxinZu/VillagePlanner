package com.villageplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        LinearLayout reminders = findViewById(R.id.reminders);

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
                    TextView textView = new TextView(RemindersActivity.this);
                    textView.setText(String.format("%s at %s, at time %s", reminder.getName(),
                            reminder.getStore().getName(), reminder.getDateTimeString()));
                    reminders.addView(textView);
                }
                Button button = new Button(RemindersActivity.this);
                button.setText("ADD A REMINDER");
                reminders.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addReminder();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    public void addReminder() {
        Intent intent = new Intent(RemindersActivity.this, AddReminderActivity.class);
        startActivity(intent);
    }

}