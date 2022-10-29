package com.villageplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase root;
    final String USER_TABLE = "Users";
    final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\."
            + "[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
            + "A-Z]{2,7}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
    }


    public void register(View view) {
        // Fetch user input
        EditText emailText = findViewById(R.id.emailText);
        String email = emailText.getText().toString().trim();

        EditText usernameText = findViewById(R.id.usernameText);
        String username = usernameText.getText().toString().trim();

        EditText passwordText = findViewById(R.id.passwordText);
        String password = passwordText.getText().toString().trim();

        EditText imageUrlText = findViewById(R.id.imageUrlText);
        String imageUrl = imageUrlText.getText().toString().trim();

        // Check validity of input
        if (email.isEmpty()) {
            emailText.setError("Email address can not be empty!");
            emailText.requestFocus();
            return;
        }
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        if (!emailPattern.matcher(email).matches()) {
            emailText.setError("Email address is not valid!");
            emailText.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            usernameText.setError("Username can not be empty!");
            usernameText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Password can not be empty!");
            passwordText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordText.setError("Password is too short!");
            passwordText.requestFocus();
            return;
        }
        // Add user to Firebase
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // After adding user to authentication, also add to user table
                            User user = new User(email, username, imageUrl);
                            root.getReference(USER_TABLE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_LONG).show();
                                        // Redirect to login
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        System.out.println(task.getException().getMessage());
                                        Toast.makeText(RegisterActivity.this, "Failed to register! Please try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            System.out.println(task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Failed to register! Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
