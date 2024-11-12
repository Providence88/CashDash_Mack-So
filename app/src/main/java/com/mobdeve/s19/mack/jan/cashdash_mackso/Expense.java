package com.mobdeve.s19.mack.jan.cashdash_mackso;

public class Expense {
    private int id; // Add the id field
    private String title;
    private String description;
    private double amount;
    private String date;
    private String category;

    public Expense(int id, String title, String description, double amount, String date, String category) {
        this.id = id; // Initialize the id
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id; // Return the id
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
