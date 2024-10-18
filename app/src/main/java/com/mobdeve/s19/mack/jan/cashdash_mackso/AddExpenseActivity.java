package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etExpenseTitle, etExpenseDescription, etExpenseAmount, etExpenseDate;
    private Spinner spExpenseCategory;
    private Button btnSaveExpense, btnDeleteExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_activity_layout);

        // Initialize views
        etExpenseTitle = findViewById(R.id.etExpenseTitle); // Assuming you want to capture the title
        etExpenseDescription = findViewById(R.id.etExpenseDescription);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        etExpenseDate = findViewById(R.id.etExpenseDate);
        spExpenseCategory = findViewById(R.id.spinnerExpenseCategory); // Match the XML ID
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        // Set save button functionality
        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement saving the expense here
                Toast.makeText(AddExpenseActivity.this, "Expense successfully added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Set delete button functionality
        btnDeleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement deleting/cancelling the expense
                finish();  // Returns to the main activity
            }
        });
    }
}
