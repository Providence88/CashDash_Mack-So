package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private ItemAdapter adapter;
    private Context context;

    public SwipeToDeleteCallback(ItemAdapter adapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        // Show confirmation dialog
        new AlertDialog.Builder(context)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    adapter.deleteItem(position);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    adapter.notifyItemChanged(position); // Cancel deletion
                })
                .setCancelable(false)
                .show();
    }
}
