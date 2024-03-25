package com.example.myb
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.myb.model.User


@Entity(tableName = "Income",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("UserId"),
        onDelete = ForeignKey.CASCADE)]
)
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val IncomeName: String,
    val Amount: Float,
    val UserId: Int
)
