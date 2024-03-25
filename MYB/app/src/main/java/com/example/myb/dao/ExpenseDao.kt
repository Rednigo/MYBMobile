package com.example.myb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    fun insertAll(vararg expenses: Expense)

    @Query("SELECT * FROM Expense")
    fun getAll(): List<Expense>
}