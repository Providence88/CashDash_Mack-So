package com.mobdeve.s19.mack.jan.cashdash_mackso;

public class Expense extends Item {
    private String description;
    private String date;
    private String category;

    public Expense(int id, String title, String description, double amount, String date, String category) {
        super(id, title, amount);  // Call to Item constructor
        this.description = description;
        this.date = date;
        this.category = category;
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

    @Override
    public String getDueDate() {
        return date;  // Return due date specific to Expense
    }
}
