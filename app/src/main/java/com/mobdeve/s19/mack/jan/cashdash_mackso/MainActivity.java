package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView userGreeting;
    private TextView budgetText;
    private Button btnBills, btnExpenses, btnEditBudget, btnAddNew;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private ArrayList<Item> itemList;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FinanceAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        userGreeting = findViewById(R.id.userGreeting);
        budgetText = findViewById(R.id.budgetText);
        btnBills = findViewById(R.id.btnViewBills);
        btnExpenses = findViewById(R.id.btnViewExpenses);
        btnEditBudget = findViewById(R.id.btnEditBudget);
        btnAddNew = findViewById(R.id.btnAddNew);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load user greeting and budget from SharedPreferences
        String userName = sharedPreferences.getString("userName", "User");
        String budget = sharedPreferences.getString("budget", "₱0.00");

        userGreeting.setText("Hello, " + userName);
        budgetText.setText(budget);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        // Load initial data (default to showing bills)
        loadDummyData();

        // Set default button states
        btnBills.setEnabled(false);  // Bills are selected by default
        btnExpenses.setEnabled(true);

        // Button listeners
        btnBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(true);
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(false);
            }
        });

        btnBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(true);
                // Change button appearance based on selection
                btnBills.setEnabled(false);
                btnExpenses.setEnabled(true);
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(false);
                // Change button appearance based on selection
                btnBills.setEnabled(true);
                btnExpenses.setEnabled(false);
            }
        });
    }

    private void loadDummyData() {
        // Add dummy bills to the list
        itemList.add(new Item("Electricity Bill", "₱4,500.00", "Due: 10/26/2023"));
        itemList.add(new Item("Water Bill", "₱1,200.00", "Due: 10/30/2023"));
        itemAdapter.notifyDataSetChanged();
    }

    private void toggleView(boolean viewBills) {
        itemList.clear(); // Clear the current list to load new data
        if (viewBills) {
            btnBills.setEnabled(false);
            btnExpenses.setEnabled(true);
            // Load bills
            loadDummyData();
        } else {
            btnBills.setEnabled(true);
            btnExpenses.setEnabled(false);
            // Load expenses
            itemList.add(new Item("Groceries", "₱2,500.00", "Bought: 10/10/2023"));
            itemList.add(new Item("Fuel", "₱3,000.00", "Bought: 10/12/2023"));
        }
        itemAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
    }
}
