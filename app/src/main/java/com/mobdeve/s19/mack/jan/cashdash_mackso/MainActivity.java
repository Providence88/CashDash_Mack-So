package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.res.ColorStateList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView userGreeting;
    private TextView budgetText;
    private Button btnBills, btnExpenses, btnEditBudget;
    private FloatingActionButton btnAddNew;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private ArrayList<Item> itemList;

    private View overlay;
    private LinearLayout addPanel;
    private Button btnAddBill, btnAddExpense;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FinanceAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userGreeting = findViewById(R.id.userGreeting);
        budgetText = findViewById(R.id.budgetText);
        btnBills = findViewById(R.id.btnViewBills);
        btnExpenses = findViewById(R.id.btnViewExpenses);
        btnEditBudget = findViewById(R.id.btnEditBudget); // Edit Budget Button
        btnAddNew = findViewById(R.id.btnAddNew);
        recyclerView = findViewById(R.id.recyclerView);

        overlay = findViewById(R.id.overlay);
        addPanel = findViewById(R.id.addPanel);
        btnAddBill = findViewById(R.id.btnAddBill);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        // Set up SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load user greeting and budget from SharedPreferences
        String userName = sharedPreferences.getString("userName", "User");
        String budget = sharedPreferences.getString("budget", "₱0.00");

        userGreeting.setText("Hello, " + userName);
        budgetText.setText(String.format("₱%,.2f", Double.parseDouble(budget.replace("₱", "").replace(",", ""))));

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        // Load initial data (default to showing bills)
        loadDummyData();

        // Set default button states and colors
        setButtonColors(true);  // Bills are selected by default

        btnBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(true);
                setButtonColors(true);  // Bills selected
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(false);
                setButtonColors(false);  // Expenses selected
            }
        });

        // Floating Action Button (Add New)
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAddPanel(true);
            }
        });

        // Add Bill Button
        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AddBillActivity
                Intent addBillIntent = new Intent(MainActivity.this, AddBillActivity.class);
                startActivity(addBillIntent);  // Start the AddBillActivity
                toggleAddPanel(false);  // Close the add panel after launching the activity
            }
        });

        // Add Expense Button
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AddExpenseActivity
                Intent addExpenseIntent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(addExpenseIntent);  // Start the AddExpenseActivity
                toggleAddPanel(false);  // Close the add panel after launching the activity
            }
        });

        // Clicking the overlay hides the panel
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAddPanel(false);
            }
        });

        // Edit Budget Button - Launch EditBudgetActivity
        btnEditBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editBudgetIntent = new Intent(MainActivity.this, EditBudgetActivity.class);
                startActivityForResult(editBudgetIntent, 1);  // Start for result with a request code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {  // Check the request code
            if (resultCode == RESULT_OK) {
                // Get the updated budget from SharedPreferences
                String updatedBudget = sharedPreferences.getString("budget", "₱0.00");

                // Update the budget display in MainActivity
                budgetText.setText(String.format("₱%,.2f", Double.parseDouble(updatedBudget.replace("₱", "").replace(",", ""))));
            }
        }
    }

    private void loadDummyData() {
        itemList.add(new Item("Electricity Bill", "₱4,500.00", "Due: 10/26/2023"));
        itemList.add(new Item("Water Bill", "₱1,200.00", "Due: 10/30/2023"));
        itemAdapter.notifyDataSetChanged();
    }

    private void toggleView(boolean viewBills) {
        itemList.clear();
        if (viewBills) {
            loadDummyData();
        } else {
            itemList.add(new Item("Groceries", "₱2,500.00", "Bought: 10/10/2023"));
            itemList.add(new Item("Fuel", "₱3,000.00", "Bought: 10/12/2023"));
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void setButtonColors(boolean isViewingBills) {
        if (isViewingBills) {
            btnBills.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primaryDarkColor)));
            btnExpenses.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.darker_gray)));
        } else {
            btnBills.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.darker_gray)));
            btnExpenses.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primaryDarkColor)));
        }
    }

    private void toggleAddPanel(boolean show) {
        if (show) {
            overlay.setVisibility(View.VISIBLE);
            addPanel.setVisibility(View.VISIBLE);
        } else {
            overlay.setVisibility(View.GONE);
            addPanel.setVisibility(View.GONE);
        }
    }
}
