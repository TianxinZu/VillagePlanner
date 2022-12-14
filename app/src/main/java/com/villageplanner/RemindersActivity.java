package com.villageplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        LinearLayout reminders = findViewById(R.id.reminders);

        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(RemindersActivity.this, LandPageActivity.class);
            startActivity(intent);
        }
        else {
            userid = auth.getCurrentUser().getUid();
        }

        // Display current reminders
        root.getReference(USER_TABLE).child(userid).child("reminders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reminders.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reminder reminder = snapshot.getValue(Reminder.class);
                    TextView textView = new TextView(RemindersActivity.this);
                    textView.setText(String.format("%s at %s, at time %s", reminder.getName(),
                            reminder.getStore().getName(), reminder.toDateTimeString()));
                    Button button = new Button(RemindersActivity.this);
                    button.setBackgroundColor(Color.parseColor("#FF0000"));
                    button.setText("X");
                    button.setTextColor(Color.parseColor("#FFFFFF"));
                    RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 90);
                    buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    button.setLayoutParams(buttonLayoutParams);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteReminder(snapshot.getKey());
                        }
                    });
                    RelativeLayout relativeLayout = new RelativeLayout(RemindersActivity.this);
                    relativeLayout.addView(textView);
                    relativeLayout.addView(button);
                    reminders.addView(relativeLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    public void addReminder(View view) {
        Intent intent = new Intent(RemindersActivity.this, AddReminderActivity.class);
        startActivity(intent);
    }

    public void deleteReminder(String key) {
        root.getReference(USER_TABLE).child(userid).child("reminders").child(key).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RemindersActivity.this, "Deleted successfully!", Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println(task.getException().getMessage());
                    Toast.makeText(RemindersActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void returnToHome(View view) {
        Intent intent = new Intent(RemindersActivity.this, MainActivity.class);
        startActivity(intent);
    }
}