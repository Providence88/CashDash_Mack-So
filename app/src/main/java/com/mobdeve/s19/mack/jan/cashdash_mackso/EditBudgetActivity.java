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
    private static final String PREFS_NAME = "FinanceAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        editBudgetInput = findViewById(R.id.editBudgetInput);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load the current budget without the currency symbol for editing
        String currentBudget = sharedPreferences.getString("budget", "₱0.00");
        editBudgetInput.setText(removeCurrencySymbol(currentBudget));

        btnSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input, parse it, and format it to Peso
                String newBudget = editBudgetInput.getText().toString().replace("₱", "").replace(",", "");
                double budgetValue = Double.parseDouble(newBudget);
                String formattedBudget = String.format("₱%,.2f", budgetValue);

                // Save the formatted budget to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("budget", formattedBudget);
                editor.apply();

                // Set the result and finish the activity
                setResult(RESULT_OK);
                finish();
            }
        });


    }



    // Utility function to remove the currency symbol
    private String removeCurrencySymbol(String budget) {
        return budget.replace("₱", "").replace(",", "").trim();
    }

    // Utility function to format the budget with currency symbol
    private String formatBudgetWithCurrency(double amount) {
        return "₱" + String.format("%,.2f", amount);
    }
}
