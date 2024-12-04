package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.os.Build;
import android.provider.Settings;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView userGreeting, budgetText;
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
    private boolean isViewingBills = true;
    private DatabaseHelper db;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the notification channel
        NotificationUtils.createNotificationChannel(this);

        db = new DatabaseHelper(this);

        // Initialize AlarmManager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Initialize views
        userGreeting = findViewById(R.id.userGreeting);
        budgetText = findViewById(R.id.budgetText);
        btnBills = findViewById(R.id.btnViewBills);
        btnExpenses = findViewById(R.id.btnViewExpenses);
        btnEditBudget = findViewById(R.id.btnEditBudget);
        btnAddNew = findViewById(R.id.btnAddNew);
        recyclerView = findViewById(R.id.recyclerView);
        overlay = findViewById(R.id.overlay);
        addPanel = findViewById(R.id.addPanel);
        btnAddBill = findViewById(R.id.btnAddBill);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Set user greeting
        String userName = sharedPreferences.getString("userName", "User");
        userGreeting.setText("Hello, " + userName);

        updateBudgetDisplay();  // Initially set budget display

        // Initialize RecyclerView and ItemAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);

        // Load data into RecyclerView
        loadBillsData();
        setButtonColors(true);

        // Pass SharedPreferences to SwipeToDeleteCallback
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(itemAdapter, this, sharedPreferences));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Set onClick listeners for the buttons
        btnBills.setOnClickListener(v -> {
            isViewingBills = true;
            toggleView(true);
            loadBillsData();
        });

        btnExpenses.setOnClickListener(v -> {
            isViewingBills = false;
            toggleView(false);
            loadExpensesData();
        });

        btnAddNew.setOnClickListener(v -> toggleAddPanel(true));
        btnAddBill.setOnClickListener(v -> {
            Intent addBillIntent = new Intent(MainActivity.this, AddBillActivity.class);
            startActivityForResult(addBillIntent, 1);
            toggleAddPanel(false);
        });

        btnAddExpense.setOnClickListener(v -> {
            Intent addExpenseIntent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(addExpenseIntent, 1);
            toggleAddPanel(false);
        });

        overlay.setOnClickListener(v -> toggleAddPanel(false));

        btnEditBudget.setOnClickListener(v -> {
            Intent editBudgetIntent = new Intent(MainActivity.this, EditBudgetActivity.class);
            startActivityForResult(editBudgetIntent, 2);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isViewingBills) {
            loadBillsData();
        } else {
            loadExpensesData();
        }
        updateBudgetDisplay();  // Update display when resuming
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (isViewingBills) {
                loadBillsData();
            } else {
                loadExpensesData();
            }
            updateBudgetDisplay();  // Update after activity result
        }
    }

    private void loadBillsData() {
        ArrayList<Bill> bills = db.getAllBills();
        itemList.clear();
        for (Bill bill : bills) {
            itemList.add(bill);
            scheduleNotification(bill.getTitle(), String.valueOf(bill.getAmount()), bill.getCategory(), bill.getDueDate());
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void loadExpensesData() {
        ArrayList<Expense> expenses = db.getAllExpenses();
        itemList.clear();
        for (Expense expense : expenses) {
            itemList.add(expense);
            scheduleNotification(expense.getTitle(), String.valueOf(expense.getAmount()), expense.getCategory(), expense.getDueDate());
        }
        itemAdapter.notifyDataSetChanged();
    }

    public void updateBudgetDisplay() {
        // Fetch the updated budget from SharedPreferences
        String budget = sharedPreferences.getString("budget", "₱0.00");
        try {
            double parsedBudget = Double.parseDouble(budget.replace("₱", "").replace(",", ""));
            budgetText.setText(String.format("₱%,.2f", parsedBudget));
        } catch (NumberFormatException e) {
            budgetText.setText("₱0.00");
        }
    }

    private void toggleView(boolean isBillsSelected) {
        btnBills.setSelected(isBillsSelected);
        btnExpenses.setSelected(!isBillsSelected);
        setButtonColors(isBillsSelected);
    }

    private void setButtonColors(boolean isBillsSelected) {
        btnBills.setBackgroundColor(isBillsSelected ?
                ContextCompat.getColor(this, R.color.primaryDarkColor) :
                ContextCompat.getColor(this, R.color.overlay_gray));

        btnExpenses.setBackgroundColor(isBillsSelected ?
                ContextCompat.getColor(this, R.color.overlay_gray) :
                ContextCompat.getColor(this, R.color.primaryDarkColor));
    }

    private void toggleAddPanel(boolean isVisible) {
        addPanel.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        overlay.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void scheduleNotification(String title, String amount, String category, String dueDate) {
        long triggerTime = System.currentTimeMillis() + 1000; // Trigger the alarm 1 second after the entry is created
        if (triggerTime == -1 || triggerTime <= System.currentTimeMillis()) {
            return;
        }

        // Check if exact alarm permission is granted
        if (alarmManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            requestExactAlarmPermission();
            return;
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("amount", amount);
        intent.putExtra("category", category);
        intent.putExtra("dueDate", dueDate);

        int requestCode = (title + dueDate).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE // Use FLAG_IMMUTABLE since you don't need to modify the PendingIntent
        );

        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
        );
    }

    private void requestExactAlarmPermission() {
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        startActivity(intent);
    }

    private long parseDueDateToMillis(String dueDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(dueDate);
            return date != null ? date.getTime() : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
