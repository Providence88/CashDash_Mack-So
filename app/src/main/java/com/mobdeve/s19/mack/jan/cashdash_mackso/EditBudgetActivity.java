package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditBudgetActivity extends AppCompatActivity {

    private EditText editBudgetInput;
    private Button btnSaveBudget;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper; // Assuming you have a DatabaseHelper class
    private static final String PREFS_NAME = "FinanceAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        editBudgetInput = findViewById(R.id.editBudgetInput);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper

        // Load the current budget without the currency symbol for editing
        String currentBudget = sharedPreferences.getString("budget", "₱0.00");
        editBudgetInput.setText(removeCurrencySymbol(currentBudget));

        btnSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newBudget = editBudgetInput.getText().toString().replace(",", "");
                try {
                    double budgetValue = Double.parseDouble(newBudget);

                    // Get the current total deductions (existing bills and expenses)
                    double totalDeductions = databaseHelper.getTotalBillsAndExpenses(); // Fetch total deductions from database

                    // Calculate the new budget after considering existing bills/expenses
                    double updatedBudget = budgetValue - totalDeductions;

                    // Format the updated budget
                    String formattedBudget = String.format("₱%,.2f", updatedBudget);

                    // Save the updated budget in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("budget", formattedBudget);
                    editor.apply();

                    setResult(RESULT_OK);
                    finish();

                    // Optionally, notify the user about the updated budget
                    Toast.makeText(EditBudgetActivity.this, "Budget updated successfully", Toast.LENGTH_SHORT).show();

                } catch (NumberFormatException e) {
                    Toast.makeText(EditBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String removeCurrencySymbol(String budget) {
        return budget.replace("₱", "").replace(",", "").trim();
    }
}
