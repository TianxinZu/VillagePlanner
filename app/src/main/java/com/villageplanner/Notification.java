package com.villageplanner;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        if (auth.getCurrentUser() == null) {
            //should not come in
            Log.d("Run multiple time", "should not comit in with auth issue");
        }
        else {
            userid = auth.getCurrentUser().getUid();
        }
        root.getReference(USER_TABLE).child(userid).child("reminders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Run multiple time", "This is a message by lla in runtimer in Notification");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reminder reminder = snapshot.getValue(Reminder.class);
                    if(reminder.shouldSentOut(reminder.getStore().getWalking_time(), reminder.getStore().getWaiting_time())&& !reminder.sented){
                        reminder.sented = true;
                        content = "If you want to arrive "+reminder.getStore().getName()+" on time, you should leave now.";
                        content +="There is "+ reminder.getStore().getWaiting_time() +" min to wait and "+reminder.getStore().getWalking_time() + " min to walk there\n";
                        running = true;
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        return running;
    }

}
