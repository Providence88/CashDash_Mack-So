package com.mobdeve.s19.mack.jan.cashdash_mackso;

public class Item {
    private String title;
    private String amount;
    private String dueDate;
    private int id;

    public Item(String title, String amount, String dueDate, int id) {
        this.title = title;
        this.amount = amount;
        this.dueDate = dueDate;
        this.id = id;
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

    public int getId() {
        return id;
    }

}
