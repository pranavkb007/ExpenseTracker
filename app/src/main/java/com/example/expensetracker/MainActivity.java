package com.example.expensetracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import androidx.room.Room;
import java.util.ArrayList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;



public class MainActivity extends AppCompatActivity {
    ArrayList<Transaction> transactionList = new ArrayList<>();
    TransactionAdapter adapter;
    TextView tvIncome, tvExpenses, tvBalance;
    AppDatabase db;
    TransactionDao transactionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Button btnLogout = findViewById(R.id.btnLogout);
//        btnLogout.setOnClickListener(v -> {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            finish();
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // Sign out of Google AND Firebase!
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });
        });



        tvIncome = findViewById(R.id.tvIncome);
        tvExpenses = findViewById(R.id.tvExpenses);
        tvBalance = findViewById(R.id.tvBalance);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "expense-db").allowMainThreadQueries().build();
        transactionDao = db.transactionDao();

        // Fetch all transactions from DB
        transactionList.clear();
        transactionList.addAll(transactionDao.getAllTransactions());

        ListView listView = findViewById(R.id.listViewTransactions);
        adapter = new TransactionAdapter(this, transactionList);
        listView.setAdapter(adapter);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }


        Button btnAdd = findViewById(R.id.btnAddTransaction);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivityForResult(intent, 1); // Add mode
            }
        });

        // Edit transaction on item click
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Transaction transaction = transactionList.get(position);
            Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
            intent.putExtra("edit", true);
            intent.putExtra("position", position);
            intent.putExtra("amount", transaction.amount);
            intent.putExtra("type", transaction.type);
            intent.putExtra("category", transaction.category);
            intent.putExtra("notes", transaction.notes);
            intent.putExtra("date", transaction.date);
            intent.putExtra("time", transaction.time);
            startActivityForResult(intent, 2); // Edit mode
        });

        // Delete transaction on long press, with confirmation
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Transaction")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Transaction transaction = transactionList.get(position);
                        transactionDao.delete(transaction);
                        transactionList.clear();
                        transactionList.addAll(transactionDao.getAllTransactions());
                        adapter.notifyDataSetChanged();
                        updateSummary();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        updateSummary();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String amount = data.getStringExtra("amount");
            String type = data.getStringExtra("type");
            String category = data.getStringExtra("category");
            String notes = data.getStringExtra("notes");
            String date = data.getStringExtra("date");
            String time = data.getStringExtra("time");

            if (requestCode == 1) {
                // Add transaction
                Transaction transaction = new Transaction(amount, type, category, notes, date, time);
                transactionDao.insert(transaction);
            } else if (requestCode == 2 && data.getBooleanExtra("edit", false)) {
                // Edit transaction
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    Transaction transaction = transactionList.get(position);
                    transaction.amount = amount;
                    transaction.type = type;
                    transaction.category = category;
                    transaction.notes = notes;
                    transaction.date = date;
                    transaction.time = time;
                    transactionDao.update(transaction);
                }
            }
            // Refresh entire transaction list from DB
            transactionList.clear();
            transactionList.addAll(transactionDao.getAllTransactions());
            adapter.notifyDataSetChanged();
            updateSummary();
        }
    }

    private void updateSummary() {
        double totalIncome = 0;
        double totalExpenses = 0;
        for (Transaction t : transactionList) {
            if (t.type.equals("Income")) {
                totalIncome += Double.parseDouble(t.amount);
            } else {
                totalExpenses += Double.parseDouble(t.amount);
            }
        }
        double balance = totalIncome - totalExpenses;
        tvIncome.setText("Income: ₹" + totalIncome);
        tvExpenses.setText("Expenses: ₹" + totalExpenses);
        tvBalance.setText("Balance: ₹" + balance);
    }
}
