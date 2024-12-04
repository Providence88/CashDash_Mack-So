package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddBillActivity extends AppCompatActivity {

    private EditText etBillTitle, etBillDescription, etBillAmount, etDateReceived, etDateDue;
    private Button btnSaveBill, btnDeleteBill;
    private Spinner spinnerCategory;  // Spinner for category selection
    private SharedPreferences sharedPreferences;
    private int year, month, day;
    private Calendar calendar;

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
        btnSaveBill = findViewById(R.id.btnSaveBill);
        btnDeleteBill = findViewById(R.id.btnDelete);
        spinnerCategory = findViewById(R.id.spinnerCategory);  // Initialize Spinner for category

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("BudgetPrefs", MODE_PRIVATE);

        // Set up the current date for DatePickerDialog
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set DatePickerDialog for Date Received
        etDateReceived.setOnClickListener(v -> showDatePickerDialog(etDateReceived));

        // Set DatePickerDialog for Date Due
        etDateDue.setOnClickListener(v -> showDatePickerDialog(etDateDue));

        // Set up category spinner (dropdown)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bill_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set save button functionality
        btnSaveBill.setOnClickListener(v -> saveBill());

        // Set delete button functionality
        btnDeleteBill.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog(EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddBillActivity.this,
                R.style.CustomDatePickerTheme, // Apply custom theme
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Format the selected date as MM-DD-YYYY
                    editText.setText(String.format("%02d-%02d-%04d", (monthOfYear + 1), dayOfMonth, year));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveBill() {
        String title = etBillTitle.getText().toString().trim();
        String description = etBillDescription.getText().toString().trim();
        String amountStr = etBillAmount.getText().toString().trim();
        String dateReceived = etDateReceived.getText().toString().trim();
        String dateDue = etDateDue.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();  // Get selected category

        if (title.isEmpty() || description.isEmpty() || amountStr.isEmpty() || dateReceived.isEmpty() || dateDue.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Create a new Bill object with the selected category
        Bill newBill = new Bill(0, title, description, amount, dateReceived, dateDue, category);

        // Save to database
        DatabaseHelper db = new DatabaseHelper(this);
        long billId = db.addBill(newBill);

        if (billId != -1) { // Check if saving was successful
            deductFromBudget(amount); // Deduct from budget only after successful save

            // Trigger alarm/notification after bill is saved
            AlarmScheduler.scheduleAlarm(this, (int) billId, title, dateDue, category, String.valueOf(amount));

            Toast.makeText(this, "Bill added successfully!", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("bill_id", billId);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Failed to add bill", Toast.LENGTH_SHORT).show();
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
