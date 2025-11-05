package com.example.expensetracker;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String amount;
    public String type;
    public String category;
    public String notes;
    public String date;
    public String time;

    public Transaction(String amount, String type, String category, String notes, String date, String time) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.notes = notes;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return type + ": ₹" + amount + " | " + category + " | " + notes;
    }
}

