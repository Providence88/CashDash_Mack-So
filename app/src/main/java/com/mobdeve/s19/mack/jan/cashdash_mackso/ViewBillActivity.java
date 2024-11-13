package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class ViewBillActivity extends AppCompatActivity {

    private EditText titleEditText, amountEditText, descriptionEditText, dateReceivedEditText, dateDueEditText, categoryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        // Initialize EditTexts (from the new XML layout)
        titleEditText = findViewById(R.id.etBillTitle);
        amountEditText = findViewById(R.id.etBillAmount);
        descriptionEditText = findViewById(R.id.etBillDescription);
        dateReceivedEditText = findViewById(R.id.etDateReceived);
        dateDueEditText = findViewById(R.id.etDateDue);
        categoryEditText = findViewById(R.id.etCategory);

        // Retrieve the data passed from the ItemAdapter
        Intent intent = getIntent();
        String title = intent.getStringExtra("item_title");
        double amount = intent.getDoubleExtra("item_amount", 0.0);
        String description = intent.getStringExtra("item_description");
        String dateReceived = intent.getStringExtra("item_dateReceived");
        String dateDue = intent.getStringExtra("item_dateDue");
        String category = intent.getStringExtra("item_category");

        // Set the data in the EditTexts (the text will be displayed, but not editable)
        titleEditText.setText(title);
        amountEditText.setText(String.format("₱%.2f", amount));
        descriptionEditText.setText(description);
        dateReceivedEditText.setText(dateReceived);
        dateDueEditText.setText(dateDue);
        categoryEditText.setText(category);
    }
}
