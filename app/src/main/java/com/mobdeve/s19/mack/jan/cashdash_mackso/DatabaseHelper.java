package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BILLS = "bills";
    public static final String COLUMN_BILL_ID = "id";
    public static final String COLUMN_BILL_TITLE = "title";
    public static final String COLUMN_BILL_DESCRIPTION = "description";
    public static final String COLUMN_BILL_AMOUNT = "amount";
    public static final String COLUMN_BILL_DATE_RECEIVED = "date_received";
    public static final String COLUMN_BILL_DATE_DUE = "date_due";
    public static final String COLUMN_BILL_CATEGORY = "category";

    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSE_ID = "id";
    public static final String COLUMN_EXPENSE_TITLE = "title";
    public static final String COLUMN_EXPENSE_DESCRIPTION = "description";
    public static final String COLUMN_EXPENSE_AMOUNT = "amount";
    public static final String COLUMN_EXPENSE_DATE = "date";
    public static final String COLUMN_EXPENSE_CATEGORY = "category";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBillsTable = "CREATE TABLE " + TABLE_BILLS + " (" +
                COLUMN_BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BILL_TITLE + " TEXT, " +
                COLUMN_BILL_DESCRIPTION + " TEXT, " +
                COLUMN_BILL_AMOUNT + " REAL, " +
                COLUMN_BILL_DATE_RECEIVED + " TEXT, " +
                COLUMN_BILL_DATE_DUE + " TEXT, " +
                COLUMN_BILL_CATEGORY + " TEXT)";

        String createExpensesTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_TITLE + " TEXT, " +
                COLUMN_EXPENSE_DESCRIPTION + " TEXT, " +
                COLUMN_EXPENSE_AMOUNT + " REAL, " +
                COLUMN_EXPENSE_DATE + " TEXT, " +
                COLUMN_EXPENSE_CATEGORY + " TEXT)";

        db.execSQL(createBillsTable);
        db.execSQL(createExpensesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public long addBill(Bill bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_TITLE, bill.getTitle());
        values.put(COLUMN_BILL_DESCRIPTION, bill.getDescription());
        values.put(COLUMN_BILL_AMOUNT, bill.getAmount());
        values.put(COLUMN_BILL_DATE_RECEIVED, bill.getDateReceived());
        values.put(COLUMN_BILL_DATE_DUE, bill.getDateDue());
        values.put(COLUMN_BILL_CATEGORY, bill.getCategory());

        long billId = db.insert(TABLE_BILLS, null, values);
        db.close();

        if (billId != -1) {
            deductBudget(bill.getAmount()); // Deduct budget after adding bill
        }
        return billId;
    }

    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_TITLE, expense.getTitle());
        values.put(COLUMN_EXPENSE_DESCRIPTION, expense.getDescription());
        values.put(COLUMN_EXPENSE_AMOUNT, expense.getAmount());
        values.put(COLUMN_EXPENSE_DATE, expense.getDate());
        values.put(COLUMN_EXPENSE_CATEGORY, expense.getCategory());

        long expenseId = db.insert(TABLE_EXPENSES, null, values);
        db.close();

        if (expenseId != -1) {
            deductBudget(expense.getAmount()); // Deduct budget after adding expense
        }
        return expenseId;
    }

    private void deductBudget(double amount) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FinanceAppPrefs", Context.MODE_PRIVATE);
        String currentBudgetStr = sharedPreferences.getString("budget", "₱0.00");

        double currentBudget = Double.parseDouble(currentBudgetStr.replace("₱", "").replace(",", ""));
        double updatedBudget = currentBudget - amount;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("budget", String.format("₱%,.2f", updatedBudget));
        editor.apply();
    }

    public ArrayList<Bill> getAllBills() {
        ArrayList<Bill> bills = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BILLS, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_BILL_ID);
                int titleIndex = cursor.getColumnIndex(COLUMN_BILL_TITLE);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_BILL_DESCRIPTION);
                int amountIndex = cursor.getColumnIndex(COLUMN_BILL_AMOUNT);
                int dateReceivedIndex = cursor.getColumnIndex(COLUMN_BILL_DATE_RECEIVED);
                int dateDueIndex = cursor.getColumnIndex(COLUMN_BILL_DATE_DUE);
                int categoryIndex = cursor.getColumnIndex(COLUMN_BILL_CATEGORY);

                if (idIndex >= 0 && titleIndex >= 0 && descriptionIndex >= 0 && amountIndex >= 0 &&
                        dateReceivedIndex >= 0 && dateDueIndex >= 0 && categoryIndex >= 0) {

                    Bill bill = new Bill(
                            cursor.getInt(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getDouble(amountIndex),
                            cursor.getString(dateReceivedIndex),
                            cursor.getString(dateDueIndex),
                            cursor.getString(categoryIndex)
                    );
                    bills.add(bill);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bills;
    }

    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_EXPENSE_ID);
                int titleIndex = cursor.getColumnIndex(COLUMN_EXPENSE_TITLE);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_EXPENSE_DESCRIPTION);
                int amountIndex = cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT);
                int dateIndex = cursor.getColumnIndex(COLUMN_EXPENSE_DATE);
                int categoryIndex = cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY);

                if (idIndex >= 0 && titleIndex >= 0 && descriptionIndex >= 0 && amountIndex >= 0 &&
                        dateIndex >= 0 && categoryIndex >= 0) {

                    Expense expense = new Expense(
                            cursor.getInt(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getDouble(amountIndex),
                            cursor.getString(dateIndex),
                            cursor.getString(categoryIndex)
                    );
                    expenses.add(expense);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    public void deleteBill(int billId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILLS, COLUMN_BILL_ID + " = ?", new String[]{String.valueOf(billId)});
        db.close();
    }

    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, COLUMN_EXPENSE_ID + " = ?", new String[]{String.valueOf(expenseId)});
        db.close();
    }

    public boolean deleteItemById(int id, boolean isBill) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = isBill ? TABLE_BILLS : TABLE_EXPENSES;  // Correct table names for bills and expenses
        int rowsAffected = db.delete(table, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }


    public double getTotalBillsAndExpenses() {
        double total = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Calculate total from bills
        Cursor billCursor = db.rawQuery("SELECT SUM(" + COLUMN_BILL_AMOUNT + ") FROM " + TABLE_BILLS, null);
        if (billCursor.moveToFirst()) {
            total += billCursor.getDouble(0);  // Add the sum of bill amounts
        }
        billCursor.close();

        // Calculate total from expenses
        Cursor expenseCursor = db.rawQuery("SELECT SUM(" + COLUMN_EXPENSE_AMOUNT + ") FROM " + TABLE_EXPENSES, null);
        if (expenseCursor.moveToFirst()) {
            total += expenseCursor.getDouble(0);  // Add the sum of expense amounts
        }
        expenseCursor.close();

        db.close();
        return total;
    }


}
