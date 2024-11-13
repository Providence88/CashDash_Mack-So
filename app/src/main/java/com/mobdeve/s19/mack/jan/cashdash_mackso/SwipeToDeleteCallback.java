package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.database.sqlite.SQLiteDatabase;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private ItemAdapter adapter;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    public SwipeToDeleteCallback(ItemAdapter adapter, Context context, SharedPreferences sharedPreferences) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Item item = adapter.getItem(position); // Get the item at the swiped position

        // Parse the amount from the item
        double amountToRefund = item.getAmount();

        // Show confirmation dialog
        new AlertDialog.Builder(context)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Safely check if the item is an instance of Bill or Expense
                    if (item instanceof Bill) {
                        // It's a Bill
                        boolean isDeletedFromDb = databaseHelper.deleteItemById(item.getId(), true);
                        if (isDeletedFromDb) {
                            // Remove item from the adapter's list
                            adapter.deleteItem(position);
                            updateBudget(amountToRefund);
                        } else {
                            adapter.notifyItemChanged(position); // Re-add item to the list if database deletion failed
                        }
                    } else if (item instanceof Expense) {
                        // It's an Expense
                        boolean isDeletedFromDb = databaseHelper.deleteItemById(item.getId(), false);
                        if (isDeletedFromDb) {
                            // Remove item from the adapter's list
                            adapter.deleteItem(position);
                            updateBudget(amountToRefund);
                        } else {
                            adapter.notifyItemChanged(position); // Re-add item to the list if database deletion failed
                        }
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    adapter.notifyItemChanged(position); // Cancel deletion
                })
                .setCancelable(false)
                .show();
    }

    // This method is used to update the budget after deleting an item
    private void updateBudget(double amountToRefund) {
        double currentBudget = Double.parseDouble(sharedPreferences.getString("budget", "₱0.00").replace("₱", "").replace(",", ""));
        double newBudget = currentBudget + amountToRefund;

        // Save the updated budget
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("budget", String.format("₱%,.2f", newBudget));
        editor.apply();

        // Update the budget display in MainActivity
        if (context instanceof MainActivity) {
            ((MainActivity) context).updateBudgetDisplay(newBudget);
        }
    }
}
