package com.mobdeve.s19.mack.jan.cashdash_mackso;

import java.io.Serializable;

public class Bill extends Item implements Serializable {
    private String description;
    private String dateReceived;
    private String dateDue;
    private String category;

    // New field for notifications
    private boolean notificationScheduled;

    public Bill(int id, String title, String description, double amount, String dateReceived, String dateDue, String category) {
        super(id, title, amount);  // Call to Item constructor
        this.description = description;
        this.dateReceived = dateReceived;
        this.dateDue = dateDue;
        this.category = category;
        this.notificationScheduled = false; // Default: notification not scheduled
    }

    public String getDescription() {
        return description;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public String getDateDue() {
        return dateDue;
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
        return dateDue;  // Return due date specific to Bill
    }
}
