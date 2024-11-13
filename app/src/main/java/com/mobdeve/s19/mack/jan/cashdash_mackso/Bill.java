package com.mobdeve.s19.mack.jan.cashdash_mackso;

import java.io.Serializable;

public class Bill extends Item implements Serializable {
    private String description;
    private String dateReceived;
    private String dateDue;
    private String category;

    public Bill(int id, String title, String description, double amount, String dateReceived, String dateDue, String category) {
        super(id, title, amount);  // Call to Item constructor
        this.description = description;
        this.dateReceived = dateReceived;
        this.dateDue = dateDue;
        this.category = category;
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

    @Override
    public String getDueDate() {
        return dateDue;  // Return due date specific to Bill
    }
}
