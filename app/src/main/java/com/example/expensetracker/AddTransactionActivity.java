package com.example.expensetracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;


public class AddTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        EditText editTextAmount = findViewById(R.id.editTextAmount);
        Spinner spinnerType = findViewById(R.id.spinnerType);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        EditText editTextNotes = findViewById(R.id.editTextNotes);
        Button btnSave = findViewById(R.id.btnSave);

        EditText editTextDate = findViewById(R.id.editTextDate);
        EditText editTextTime = findViewById(R.id.editTextTime);

// Date picker
        editTextDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                // Format as DD/MM/YYYY
                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                editTextDate.setText(selectedDate);
            }, year, month, day).show();
        });

// Time picker
        editTextTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                // Format as HH:MM
                String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                editTextTime.setText(selectedTime);
            }, hour, minute, true).show();
        });


        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("edit", false)) {
            editTextAmount.setText(intent.getStringExtra("amount"));

            // Set spinnerType
            String type = intent.getStringExtra("type");
            if (type != null) {
                int typePos = type.equals("Income") ? 1 : 0;
                spinnerType.setSelection(typePos);
            }

            // Set spinnerCategory
            String category = intent.getStringExtra("category");
            if (category != null) {
                String[] categories = getResources().getStringArray(R.array.category_array);
                for (int i = 0; i < categories.length; i++) {
                    if (categories[i].equals(category)) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }

            editTextNotes.setText(intent.getStringExtra("notes"));
        }

        btnSave.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();
            String notes = editTextNotes.getText().toString().trim();
            String date = editTextDate.getText().toString().trim();
            String time = editTextTime.getText().toString().trim();


            Toast.makeText(this,
                    "Amount: " + amount +
                            "\nType: " + type +
                            "\nCategory: " + category +
                            "\nNotes: " + notes,
                    Toast.LENGTH_LONG).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("amount", amount);
            resultIntent.putExtra("type", type);
            resultIntent.putExtra("category", category);
            resultIntent.putExtra("notes", notes);
            resultIntent.putExtra("date", date);
            resultIntent.putExtra("time", time);


            // If editing, pass edit flag and position
            if (getIntent().getBooleanExtra("edit", false)) {
                resultIntent.putExtra("edit", true);
                resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
