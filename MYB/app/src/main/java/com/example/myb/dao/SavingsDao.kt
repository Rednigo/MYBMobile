package com.example.myb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myb.model.Savings

@Dao
interface SavingsDao {
    @Insert
    fun insertAll(vararg savings: Savings)

    @Query("SELECT * FROM Savings")
    fun getAll(): List<Savings>

    @Update
    fun update(savings: Savings)

    @Delete
    fun delete(savings: Savings)
}
