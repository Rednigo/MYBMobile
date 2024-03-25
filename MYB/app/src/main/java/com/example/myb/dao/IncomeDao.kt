package com.example.myb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IncomeDao {
    @Insert
    fun insertAll(vararg incomes: Income)

    @Query("SELECT * FROM Income")
    fun getAll(): List<Income>
}