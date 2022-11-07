package com.villageplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase root;
    private FirebaseStorage storage;
    private Uri imageUri;
    final String USER_TABLE = "Users";
    final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\."
            + "[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
            + "A-Z]{2,7}$";
    private String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
//        ImageView avatar = findViewById(R.id.avatar);
//        Glide.with(RegisterActivity.this).load("https://firebasestorage.googleapis.com/v0/b/villageplanner-c7c90.appspot.com/o/images%2Fd6bb1350-04ec-455c-ada0-9afd368624f2?alt=media&token=b85ee716-4299-46e5-897d-9e04002f2271").into(avatar);
    }


    public void register(View view) {
        // Fetch user input
        EditText emailText = findViewById(R.id.emailText);
        String email = emailText.getText().toString().trim();

        EditText usernameText = findViewById(R.id.usernameText);
        String username = usernameText.getText().toString().trim();

        EditText passwordText = findViewById(R.id.passwordText);
        String password = passwordText.getText().toString().trim();

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
        // Upload image to firebase
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
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        System.out.println(task.getException().getMessage());
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            System.out.println(task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void uploadPicture(View view) {
        choosePicture();
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadToFirebase();
        }
    }

    private void uploadToFirebase() {
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setTitle("Uploading Image...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference reference = storage.getReference().child("images/" + randomKey);
        UploadTask uploadTask = reference.putFile(imageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Upload failed!", Toast.LENGTH_LONG).show();
                    throw task.getException();
                }
                pd.dismiss();
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
               if (task.isSuccessful()) {
                   imageUrl = task.getResult().toString();
               }
               else {
                   Toast.makeText(RegisterActivity.this, "Upload failed!", Toast.LENGTH_LONG).show();
               }
            }
        });
    }
}
