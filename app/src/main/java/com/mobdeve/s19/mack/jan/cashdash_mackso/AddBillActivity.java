package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBillActivity extends AppCompatActivity {

    private EditText etBillTitle,etBillDescription, etBillAmount, etDateReceived, etDateDue;
    private Spinner spinnerCategory;
    private Button btnSaveBill, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill_activity_layout);

        // Initialize views
        etBillTitle = findViewById(R.id.etBillTitle);
        etBillDescription = findViewById(R.id.etBillDescription);
        etBillAmount = findViewById(R.id.etBillAmount);
        etDateReceived = findViewById(R.id.etDateReceived);
        etDateDue = findViewById(R.id.etDateDue);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveBill = findViewById(R.id.btnSaveBill);
        btnDelete = findViewById(R.id.btnDelete);

        // Set save button functionality
        btnSaveBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement saving the bill here
                Toast.makeText(AddBillActivity.this, "Bill successfully added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Set delete button functionality
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement deleting/cancelling the bill
                finish();  // Returns to the main activity
            }
        });
    }
}
