package com.villageplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\."
            + "[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
            + "A-Z]{2,7}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        auth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        EditText emailText = findViewById(R.id.emailTextLogin);
        String email = emailText.getText().toString().trim();

        EditText passwordText = findViewById(R.id.passwordTextLogin);
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty()) {
            emailText.setError("Email is required!");
            emailText.requestFocus();
            return;
        }
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        if (!emailPattern.matcher(email).matches()) {
            emailText.setError("Email address is not valid!");
            emailText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Password is required!");
            passwordText.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Redirect to main page
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            System.out.println(task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
