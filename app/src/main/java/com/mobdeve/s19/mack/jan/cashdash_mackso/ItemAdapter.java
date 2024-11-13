package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Item> itemList;  // Store the context to start the new activity
    private DatabaseHelper databaseHelper;  // Add a reference to DatabaseHelper

    public ItemAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.databaseHelper = new DatabaseHelper(context);  // Initialize DatabaseHelper
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Item item = itemList.get(position);

        holder.itemTitle.setText(item.getTitle());
        holder.itemAmount.setText(String.format("₱%.2f", item.getAmount()));
        holder.itemDueDate.setText(item.getDueDate()); // For Bill or Expense

        // Handle the click event
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if (item instanceof Bill) {
                Bill bill = (Bill) item; // Cast to Bill
                intent = new Intent(context, ViewBillActivity.class);
                // Pass all the necessary data for Bill
                intent.putExtra("item_id", bill.getId());
                intent.putExtra("item_title", bill.getTitle());
                intent.putExtra("item_amount", bill.getAmount());
                intent.putExtra("item_description", bill.getDescription());
                intent.putExtra("item_dateReceived", bill.getDateReceived());
                intent.putExtra("item_dateDue", bill.getDateDue());
                intent.putExtra("item_category", bill.getCategory());
            } else if (item instanceof Expense) {
                Expense expense = (Expense) item; // Cast to Expense
                intent = new Intent(context, ViewExpenseActivity.class);
                // Pass all the necessary data for Expense
                intent.putExtra("item_id", expense.getId());
                intent.putExtra("item_title", expense.getTitle());
                intent.putExtra("item_amount", expense.getAmount());
                intent.putExtra("item_description", expense.getDescription());
                intent.putExtra("item_date", expense.getDate());
                intent.putExtra("item_category", expense.getCategory());
            } else {
                return; // Return if it's not a Bill or Expense (fallback)
            }
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    public void deleteItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle, itemAmount, itemDueDate;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemAmount = itemView.findViewById(R.id.itemAmount);
            itemDueDate = itemView.findViewById(R.id.itemDueDate);
        }
    }
}
