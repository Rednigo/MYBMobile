package com.example.myb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myb.ExpenseCategory

@Dao
interface ExpenseCategoryDao {
    @Insert
    fun insertAll(vararg categories: ExpenseCategory)

    @Query("SELECT * FROM ExpenseCategory")
    fun getAll(): List<ExpenseCategory>
}