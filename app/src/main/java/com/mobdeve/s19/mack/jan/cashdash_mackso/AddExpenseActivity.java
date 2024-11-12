package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etExpenseTitle, etExpenseDescription, etExpenseAmount, etExpenseDate;
    private Spinner spExpenseCategory;
    private Button btnSaveExpense, btnDeleteExpense;
    private SharedPreferences sharedPreferences;
    private int year, month, day;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_activity_layout);

        // Initialize views
        etExpenseTitle = findViewById(R.id.etExpenseTitle);
        etExpenseDescription = findViewById(R.id.etExpenseDescription);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        etExpenseDate = findViewById(R.id.etExpenseDate);
        spExpenseCategory = findViewById(R.id.spinnerExpenseCategory);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("BudgetPrefs", MODE_PRIVATE);

        // Set up the current date for DatePickerDialog
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set DatePickerDialog for Expense Date
        etExpenseDate.setOnClickListener(v -> showDatePickerDialog(etExpenseDate));

        // Set save button functionality
        btnSaveExpense.setOnClickListener(v -> saveExpense());

        // Set delete button functionality
        btnDeleteExpense.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog(EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddExpenseActivity.this,
                R.style.CustomDatePickerTheme, // Apply custom theme
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Format the selected date as MM-DD-YYYY
                    editText.setText(String.format("%02d-%02d-%04d", (monthOfYear + 1), dayOfMonth, year));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveExpense() {
        String title = etExpenseTitle.getText().toString().trim();
        String description = etExpenseDescription.getText().toString().trim();
        String amountStr = etExpenseAmount.getText().toString().trim();
        String date = etExpenseDate.getText().toString().trim();
        String category = spExpenseCategory.getSelectedItem().toString();

        if (title.isEmpty() || description.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = 0.0;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount < 0) {
                Toast.makeText(this, "Amount must be greater than or equal to 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount entered", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Expense object with a default id of 0
        Expense newExpense = new Expense(0, title, description, amount, date, category);

        DatabaseHelper db = new DatabaseHelper(this);
        long expenseId = db.addExpense(newExpense);

        if (expenseId != -1) { // Check if saving was successful
            deductFromBudget(amount); // Deduct from budget only after successful save
            Toast.makeText(this, "Expense successfully added!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
        }
    }

    private void deductFromBudget(double amount) {
        double currentBudget = getCurrentBudget();
        double updatedBudget = currentBudget - amount;
        saveBudget(updatedBudget);
    }

    private double getCurrentBudget() {
        return Double.longBitsToDouble(sharedPreferences.getLong("currentBudget", Double.doubleToLongBits(0)));
    }

    private void saveBudget(double newBudget) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("currentBudget", Double.doubleToLongBits(newBudget));
        editor.apply();
    }


}
