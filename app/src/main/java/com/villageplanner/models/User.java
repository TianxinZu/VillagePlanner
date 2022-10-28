package com.villageplanner.models;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class User {
    private Integer userid;
    private String email;
    private String username;
    private String password;
    private String imageUrl;
    private List<Reminder> reminders;

    public User() {

    }

    public User(Integer userid, String email, String username, String password) {
        this.userid = userid;
        this.email = email;
        this.username = username;
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
