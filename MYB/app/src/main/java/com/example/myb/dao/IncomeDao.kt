package com.example.myb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IncomeDao {
    @Insert
    fun insertAll(vararg incomes: Income)

    @Query("SELECT * FROM Income")
    fun getAll(): List<Income>

    @Update
    fun update(income: Income)

    @Delete
    fun delete(income: Income)
}
