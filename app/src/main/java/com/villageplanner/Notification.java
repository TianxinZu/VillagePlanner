package com.villageplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class Notification {
    private boolean running = false;
    private FirebaseAuth auth;
    private FirebaseDatabase root;
    final String USER_TABLE = "Users";
    private String userid;
    private String content;

    public Notification(){}

    public String getContent(){return content;}

    public boolean runTimer(){
//
        if (auth.getCurrentUser() == null) {
            //should not come in
        }
        else {
            userid = auth.getCurrentUser().getUid();
        }
//
//        final Handler handler = new Handler();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                // hardcode running !!!!!!!!
//                if(running){
                    root.getReference(USER_TABLE).child(userid).child("reminders").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Reminder reminder = snapshot.getValue(Reminder.class);
                                if(reminder.shouldSentOut(reminder.getStore().getUnixTimestamp(), reminder.getStore().getWaiting_time())&& !reminder.sented){
                                    reminder.sented = true;
                                    content = "If you want to arrive "+reminder.getStore().getName()+" on time, you should leave now.";
                                    content +="There is "+ reminder.getStore().getWaiting_time() +" min to wait and "+reminder.getStore().getUnixTimestamp() + " min to walk there\n";
                                    running = true;
                                    break;
                                }
                            }
                        }
//
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
//                }
//                handler.postDelayed(this,1000);
//            }
//        });
        return running;
    }

}
