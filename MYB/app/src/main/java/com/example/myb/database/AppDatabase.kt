package com.example.myb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myb.Expense
import com.example.myb.ExpenseCategory
import com.example.myb.dao.ExpenseCategoryDao
import com.example.myb.ExpenseDao
import com.example.myb.Income
import com.example.myb.IncomeDao
import com.example.myb.dao.SavingsDao
import com.example.myb.dao.UserDao
import com.example.myb.model.Savings
import com.example.myb.model.User


@Database(entities = [User::class, ExpenseCategory::class, Savings::class, Expense::class, Income::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expenseCategoryDao(): ExpenseCategoryDao
    abstract fun savingsDao(): SavingsDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao
    // Add other DAOs here
}
