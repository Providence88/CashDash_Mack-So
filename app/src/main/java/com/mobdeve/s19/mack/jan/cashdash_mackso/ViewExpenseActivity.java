package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewExpenseActivity extends AppCompatActivity {

    private TextView titleTextView, amountTextView, descriptionTextView, dateTextView, categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);

        // Initialize TextViews
        titleTextView = findViewById(R.id.expenseTitleTextView);
        amountTextView = findViewById(R.id.expenseAmountTextView);
        descriptionTextView = findViewById(R.id.expenseDescriptionTextView);
        dateTextView = findViewById(R.id.expenseDateTextView);
        categoryTextView = findViewById(R.id.expenseCategoryTextView);

        // Retrieve the data passed from the ItemAdapter
        Intent intent = getIntent();
        String title = intent.getStringExtra("item_title");
        double amount = intent.getDoubleExtra("item_amount", 0.0);
        String description = intent.getStringExtra("item_description");
        String date = intent.getStringExtra("item_date");
        String category = intent.getStringExtra("item_category");

        // Set the data in the TextViews
        titleTextView.setText(title);
        amountTextView.setText(String.format("₱%.2f", amount));
        descriptionTextView.setText(description);
        dateTextView.setText(date);
        categoryTextView.setText(category);
    }
}
