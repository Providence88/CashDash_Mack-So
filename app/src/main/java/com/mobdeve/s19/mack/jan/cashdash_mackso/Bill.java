package com.mobdeve.s19.mack.jan.cashdash_mackso;


public class Bill {
    private String title;
    private String description;
    private double amount;
    private String dateReceived;
    private String dateDue;
    private String category;
    private int id;

    // Constructor that takes the id, title, description, amount, dateReceived, dateDue, and category
    public Bill(int id, String title, String description, double amount, String dateReceived, String dateDue, String category) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.dateReceived = dateReceived;
        this.dateDue = dateDue;
        this.category = category;
        this.id = id;
    }

    // Getters and Setters
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

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getDateDue() {
        return dateDue;
    }

    public void setDateDue(String dateDue) {
        this.dateDue = dateDue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

}
