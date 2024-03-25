package com.example.myb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myb.model.Savings

@Dao
interface SavingsDao {
    @Insert
    fun insertAll(vararg savings: Savings)

    @Query("SELECT * FROM Savings")
    fun getAll(): List<Savings>
}