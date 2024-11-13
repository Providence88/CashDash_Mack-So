package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;


public class ViewBillActivity extends AppCompatActivity {

    private TextView titleTextView, amountTextView, descriptionTextView, dateReceivedTextView, dateDueTextView, categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        // Initialize TextViews
        titleTextView = findViewById(R.id.billTitleTextView);
        amountTextView = findViewById(R.id.billAmountTextView);
        descriptionTextView = findViewById(R.id.billDescriptionTextView);
        dateReceivedTextView = findViewById(R.id.billDateReceivedTextView);
        dateDueTextView = findViewById(R.id.billDateDueTextView);
        categoryTextView = findViewById(R.id.billCategoryTextView);

        // Retrieve the data passed from the ItemAdapter
        Intent intent = getIntent();
        String title = intent.getStringExtra("item_title");
        double amount = intent.getDoubleExtra("item_amount", 0.0);
        String description = intent.getStringExtra("item_description");
        String dateReceived = intent.getStringExtra("item_dateReceived");
        String dateDue = intent.getStringExtra("item_dateDue");
        String category = intent.getStringExtra("item_category");

        // Set the data in the TextViews
        titleTextView.setText(title);
        amountTextView.setText(String.format("₱%.2f", amount));
        descriptionTextView.setText(description);
        dateReceivedTextView.setText(dateReceived);
        dateDueTextView.setText(dateDue);
        categoryTextView.setText(category);
    }
}
