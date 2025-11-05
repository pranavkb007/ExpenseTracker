package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<Transaction> {
    public TransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaction, parent, false);
        }
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvNotes = convertView.findViewById(R.id.tvNotes);
        TextView tvDateTime = convertView.findViewById(R.id.tvDateTime);

        tvAmount.setText("₹" + transaction.amount);
        tvCategory.setText(transaction.category);
        tvNotes.setText(transaction.notes);
        tvDateTime.setText(transaction.date + " " + transaction.time);

        // Color based on type
        if (transaction.type.equals("Income")) {
            tvAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        return convertView;
    }
}
