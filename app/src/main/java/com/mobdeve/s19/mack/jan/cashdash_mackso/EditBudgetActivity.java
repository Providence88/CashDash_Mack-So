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
                String newBudget = editBudgetInput.getText().toString().replace(",", "");
                try {
                    double budgetValue = Double.parseDouble(newBudget);
                    String formattedBudget = String.format("₱%,.2f", budgetValue);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("budget", formattedBudget);
                    editor.apply();

                    setResult(RESULT_OK);
                    finish();
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
