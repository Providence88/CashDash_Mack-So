package com.mobdeve.s19.mack.jan.cashdash_mackso;

public abstract class Item {  // Mark Item as abstract if it's not intended to be instantiated
    private int id;
    private String title;
    private double amount;  // Using double to store the amount, to match Bill and Expense

    public Item(int id, String title, double amount) {
        this.id = id;
        this.title = title;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public abstract String getDueDate();  // Abstract method for due date to be implemented by subclasses
}
