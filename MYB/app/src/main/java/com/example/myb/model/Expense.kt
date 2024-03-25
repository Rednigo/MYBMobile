package com.example.myb
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Expense",
    foreignKeys = [ForeignKey(entity = ExpenseCategory::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("CategoryId"),
        onDelete = ForeignKey.CASCADE)]
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ExpenseName: String,
    val Amount: Float,
    val Date: Long,
    val CategoryId: Int
)
