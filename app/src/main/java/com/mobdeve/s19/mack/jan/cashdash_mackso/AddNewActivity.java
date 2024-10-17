package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewActivity extends AppCompatActivity {

    private EditText titleInput, amountInput, dueDateInput;
    private Button btnSaveNewEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        // Initialize inputs
        titleInput = findViewById(R.id.newTitle);
        amountInput = findViewById(R.id.newAmount);
        dueDateInput = findViewById(R.id.newDueDate);
        btnSaveNewEntry = findViewById(R.id.btnSaveEntry);

        btnSaveNewEntry.setOnClickListener(v -> {
            // Save new bill or expense logic can be added here
            // For now, this is a placeholder for adding the backend later.

            // After saving, return to MainActivity
            finish();
        });
    }
}

