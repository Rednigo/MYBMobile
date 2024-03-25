package com.example.myb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myb.model.User


@Dao
interface UserDao {
    @Insert
    fun insertAll(vararg users: User)

    @Query("SELECT * FROM user")
    fun getAll(): List<User>
}