package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        // Load the current budget
        String currentBudget = sharedPreferences.getString("budget", "₱0.00");
        editBudgetInput.setText(currentBudget);

        btnSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save new budget to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("budget", editBudgetInput.getText().toString());
                editor.apply();

                // Close activity and go back to MainActivity
                finish();
            }
        });
    }
}

