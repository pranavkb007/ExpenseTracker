# Expense Tracker App

A **beginner-friendly Android app** built with Java and Room Database to help you track your daily expenses and income.  
All data is saved locally, with features for adding, editing, and deleting transactions, plus smart summary cards for totals.  
Built on Android Studio, fully open-source, ready for learning and enhancement!

---

## Features

- **Add transactions:** Enter amount, type (Income/Expense), category, notes, date & time
- **Edit / Delete transactions:** Tap to edit, long-press to confirm deletion
- **Persistent storage:** Your data is saved securely using Room Database
- **Summary cards:** See Income, Expenses, and Balance at a glance, updated instantly
- **Custom list display:** Transactions shown in colored cards for quick overview
- **Beginner-friendly code:** Helpful comments and modular files

---
## Project Structure

- `MainActivity.java` — summary, transaction list, add/edit/delete logic
- `AddTransactionActivity.java` — simple form for entering transactions
- `Transaction.java` — database entity for one transaction
- `TransactionDao.java` — Room DAO interface for database operations
- `AppDatabase.java` — Room database holder
- `TransactionAdapter.java` — custom adapter for colored transaction cards
- `activity_main.xml`, `activity_add_transaction.xml`, `item_transaction.xml` — UI layouts

---

## Dependencies

- **Room Database:** AndroidX Room
- **AppCompat & Core:** Jetpack libraries

---

## Author

Made by Pranav Bhalerao  
Inspired by learning Android fundamentals!

---

**Feel free to star, fork, or open issues for improvement. Happy tracking!**
