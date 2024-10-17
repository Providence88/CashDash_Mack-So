package com.mobdeve.s19.mack.jan.cashdash_mackso;

public class Item {
    private String title;
    private String amount;
    private String dueDate;

    public Item(String title, String amount, String dueDate) {
        this.title = title;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public String getDueDate() {
        return dueDate;
    }
}

