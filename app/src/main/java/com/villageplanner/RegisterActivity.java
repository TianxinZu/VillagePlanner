package com.villageplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.villageplanner.models.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase root;
    DatabaseReference reference;
    final String USER_TABLE = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
    }

    public void register(View view) {
        EditText emailText = view.findViewById(R.id.emailText);
        String email = emailText.getText().toString();
        System.out.println("ajfjah");
        if (email.isEmpty()) {
            System.out.println("Empty email");
            return;
        }
        EditText usernameText = view.findViewById(R.id.usernameText);
        String username = usernameText.getText().toString();
        if (username.isEmpty()) {
            System.out.println("Empty username");
            return;
        }
        EditText passwordText = view.findViewById(R.id.passwordText);
        String password = passwordText.getText().toString();
        if (password.isEmpty()) {
            System.out.println("Empty password");
            return;
        }
        Query useridQuery = reference.child(USER_TABLE).limitToLast(1);
        useridQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer userid = 0;
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userid = Integer.valueOf(snapshot.getKey());
                        ++userid;
                    }
                }
                User newUser = new User(userid, email, username, password);
                reference.child(USER_TABLE).child(userid.toString()).setValue(newUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("clicked adadsa dasd ");
    }
}
