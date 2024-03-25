package com.example.myb.model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Savings",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("UserId"),
        onDelete = ForeignKey.CASCADE)]
)
data class Savings(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val SavingsName: String,
    val Amount: Float,
    val Date: Long,
    val UserId: Int
)
