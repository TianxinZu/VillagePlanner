package com.villageplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ProfilePageActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase root;
    private FirebaseStorage storage;
    private Uri imageUri;
    private String imageUrl = "";
    final String USER_TABLE = "Users";
    private String userid;
    ImageView avatar;
    TextView profileEmail;
    TextView profileUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        avatar = findViewById(R.id.profileAvatar);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(ProfilePageActivity.this, LandPageActivity.class);
            startActivity(intent);
        }
        else {
            userid = auth.getCurrentUser().getUid();
        }

        root.getReference(USER_TABLE).child(userid).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrl = (String) dataSnapshot.getValue();
                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = "http://www.gravatar.com/avatar/?d=mp";
                }
                Glide.with(getApplicationContext()).load(imageUrl).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        root.getReference(USER_TABLE).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("email")) {
                        profileEmail.setText("Email: " + (String) snapshot.getValue());
                    }
                    else if (snapshot.getKey().equals("username")) {
                        profileUsername.setText("Username: " + (String) snapshot.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    public void backToMain(View view) {
        Intent intent = new Intent(ProfilePageActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void uploadPicture(View view) {
        choosePicture();
        // Update Picture
        // System.out.println(root.getReference(USER_TABLE).child(userid).child("imageUrl"));
        // root.getReference(USER_TABLE).child(userid).child("imageUrl").setValue(imageUrl);
        // System.out.println(root.getReference(USER_TABLE).child(userid).child("imageUrl"));
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
        final ProgressDialog pd = new ProgressDialog(ProfilePageActivity.this);
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
                    Toast.makeText(ProfilePageActivity.this, "Upload failed!", Toast.LENGTH_LONG).show();
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
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        root.getReference(USER_TABLE).child(userid).child("imageUrl").setValue(imageUrl);
                    }
                }
                else {
                    Toast.makeText(ProfilePageActivity.this, "Upload failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}