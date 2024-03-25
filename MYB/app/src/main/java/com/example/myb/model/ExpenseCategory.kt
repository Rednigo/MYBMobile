package com.example.myb
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.myb.model.User

@Entity(tableName = "ExpenseCategory",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("UserId"),
        onDelete = ForeignKey.CASCADE)]
)
data class ExpenseCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val CategoryName: String,
    val Amount: Float,
    val UserId: Int
)
