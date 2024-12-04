package com.mobdeve.s19.mack.jan.cashdash_mackso;

import java.io.Serializable;

public class Expense extends Item implements Serializable {
    private String description;
    private String date;
    private String category;

    // New field for notifications
    private boolean notificationScheduled;

    public Expense(int id, String title, String description, double amount, String date, String category) {
        super(id, title, amount);  // Call to Item constructor
        this.description = description;
        this.date = date;
        this.category = category;
        this.notificationScheduled = false; // Default: notification not scheduled
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public boolean isNotificationScheduled() {
        return notificationScheduled;
    }

    public void setNotificationScheduled(boolean notificationScheduled) {
        this.notificationScheduled = notificationScheduled;
    }

    @Override
    public String getDueDate() {
        return date;  // Return due date specific to Expense
    }
}
