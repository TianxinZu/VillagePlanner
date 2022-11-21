package com.villageplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class AddReminderActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase root;
    private String userid;
    final String USER_TABLE = "Users";
    EditText nameText;
    Boolean sented = false;
    EditText storeNameText;
    EditText frequencyText;
    EditText hourAndMinuteText;
    CalendarView calendarView;
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime dateTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);

    private static Boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        userid = auth.getCurrentUser().getUid();

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
            }
        });
    }

    public void addReminder(View view) {
        nameText = findViewById(R.id.nameText);
        storeNameText = findViewById(R.id.storeNameText);
        frequencyText = findViewById(R.id.frequencyText);
        hourAndMinuteText = findViewById(R.id.hourAndMinuteText);
        String name = nameText.getText().toString();
        String storeName = storeNameText.getText().toString();
        String frequencyString = frequencyText.getText().toString();
        String hourAndMinute = hourAndMinuteText.getText().toString();
        Integer frequency = frequencyString.isEmpty() ? 0 : Integer.valueOf(frequencyString);
        if (isValid(name, storeName, frequencyString, hourAndMinute)) {
            Reminder reminder = new Reminder(name, dateTime.toInstant(OffsetDateTime.now().getOffset()).getEpochSecond(),
                    AllStores.stores.get(storeName), frequency, false);
            root.getReference(USER_TABLE).child(userid).child("reminders").push().setValue(reminder);
//            try {
//                Thread.sleep(5000);
//            }
//            catch (Exception e) {}
            Intent intent = new Intent(AddReminderActivity.this, RemindersActivity.class);
            startActivity(intent);
        }
    }

    // Check validity of input
    public Boolean isValid(String name, String storeName, String frequencyString, String hourAndMinute ) {
        if (name.isEmpty()) {
            nameText.setError("Please input a reminder name!");
            nameText.requestFocus();
            return false;
        }
        if (storeName.isEmpty()) {
            storeNameText.setError("Please specify the store!");
            storeNameText.requestFocus();
            return false;
        }

        if (hourAndMinute.isEmpty()) {
            hourAndMinuteText.setError("Please specify the exact time!");
            hourAndMinuteText.requestFocus();
            return false;
        }
        if (!frequencyString.isEmpty() && !isInteger(frequencyString)) {
            frequencyText.setError("Frequency must be an integer!");
            frequencyText.requestFocus();
            return false;
        }
        Integer colonIndex = hourAndMinute.indexOf(":");
        if (colonIndex == -1) {
            hourAndMinuteText.setError("Incorrect format! Colon not found!");
            hourAndMinuteText.requestFocus();
            return false;
        }
        String hour = hourAndMinute.substring(0, colonIndex);
        String minute = hourAndMinute.substring(colonIndex + 1, hourAndMinute.length());
        if (hour.isEmpty() || minute.isEmpty() || hour.length() > 2 || minute.length() > 2 ||
                !isInteger(hour) || !isInteger(minute) || Integer.valueOf(hour) < 0 ||
                Integer.valueOf(hour) > 23 || Integer.valueOf(minute) < 0 || Integer.valueOf(minute) > 59) {
            hourAndMinuteText.setError("Time is badly formatted!");
            hourAndMinuteText.requestFocus();
            return false;
        }
        System.out.println(dateTime);
        dateTime = dateTime.plusHours(Integer.valueOf(hour));
        dateTime = dateTime.plusMinutes(Integer.valueOf(minute));
        if (Duration.between(LocalDateTime.now(), dateTime).toMinutes() < 10) {
            hourAndMinuteText.setError("You have to set a reminder at least 10 minutes later!");
            hourAndMinuteText.requestFocus();
            dateTime = dateTime.minusHours(Integer.valueOf(hour));
            dateTime = dateTime.minusMinutes(Integer.valueOf(minute));
            return false;
        }
        return true;
    }
}