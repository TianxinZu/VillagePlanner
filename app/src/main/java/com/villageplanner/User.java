package com.villageplanner;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String username;
    private String imageUrl;
    private List<Reminder> reminders;

    public User() {

    }

    public User(String email, String username, String imageUrl) {
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
        reminders = new ArrayList<Reminder>();
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public List<Reminder> getReminders() {
        return this.reminders;
    }
}
