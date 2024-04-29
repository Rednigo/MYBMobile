package com.example.myb
import StatisticsFragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myb.dao.ExpenseCategoryDao
import com.example.myb.dao.SavingsDao
import com.example.myb.dao.UserDao
import com.example.myb.database.AppDatabase
import com.example.myb.model.Savings
import com.example.myb.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var savingsDao: SavingsDao
    private lateinit var incomesDao: IncomeDao
    private lateinit var expenseCategoriesDao: ExpenseCategoryDao
    private lateinit var expensesDao: ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_settings)

       // super.onCreate(savedInstanceState)
       // supportFragmentManager.beginTransaction()
       //     .replace(android.R.id.content, StatisticsFragment())
       //     .commit()


        val isDeleted = applicationContext.deleteDatabase("MYB.db")
        Log.d("DatabaseDeleted", "Is database deleted? $isDeleted")

        // Building the database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "MYB.db"
        ).allowMainThreadQueries().build()

        userDao = db.userDao()
        savingsDao = db.savingsDao()
        incomesDao = db.incomeDao()
        expenseCategoriesDao = db.expenseCategoryDao()
        expensesDao = db.expenseDao()

        // Inserting entities
        for (i in 1..10) {
            userDao.insertAll(User(i, "User $i", 20 + i))
            val currentTime = System.currentTimeMillis() // Correct, currentTime is Long
            savingsDao.insertAll(Savings(i, "Savings $i", 500f + 10f * i, currentTime, i))
            incomesDao.insertAll(Income(i, "Income $i", 500f + 10f * i, i))
            expenseCategoriesDao.insertAll(ExpenseCategory(i, "Expense category $i", 500f + 10f * i, i))
            expensesDao.insertAll(Expense(i, "Expense $i", 500f + 10f * i, currentTime, i))
        }

        // Retrieving and displaying entities
        val users = userDao.getAll()
        users.forEach {
            Log.d("User", it.toString())
        }
        val savings = savingsDao.getAll()
        savings.forEach {
            Log.d("Saving", it.toString())
        }
        val incomes = incomesDao.getAll()
        incomes.forEach {
            Log.d("Income", it.toString())
        }
        val expenseCategories = expenseCategoriesDao.getAll()
        expenseCategories.forEach {
            Log.d("Expense category", it.toString())
        }
        val expenses = expensesDao.getAll()
        expenses.forEach {
            Log.d("Expense", it.toString())
        }
    }
}