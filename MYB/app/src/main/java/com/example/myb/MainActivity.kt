package com.example.myb
import StatisticsFragment
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myb.dao.ExpenseCategoryDao
import com.example.myb.dao.SavingsDao
import com.example.myb.dao.UserDao
import com.example.myb.database.AppDatabase
import com.example.myb.model.Savings
import com.example.myb.model.User
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var savingsDao: SavingsDao
    private lateinit var incomesDao: IncomeDao
    private lateinit var expenseCategoriesDao: ExpenseCategoryDao
    private lateinit var expensesDao: ExpenseDao
    private lateinit var expenseCategoriesLayout: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_settings)

       // super.onCreate(savedInstanceState)
       // supportFragmentManager.beginTransaction()
       //     .replace(android.R.id.content, StatisticsFragment())
       //     .commit()


//        val isDeleted = applicationContext.deleteDatabase("MYB.db")
//        Log.d("DatabaseDeleted", "Is database deleted? $isDeleted")
//
//        // Building the database
//        db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java,
//            "MYB.db"
//        ).allowMainThreadQueries().build()
//
//        userDao = db.userDao()
//        savingsDao = db.savingsDao()
//        incomesDao = db.incomeDao()
//        expenseCategoriesDao = db.expenseCategoryDao()
//        expensesDao = db.expenseDao()
//
//        // Inserting entities
//        for (i in 1..10) {
//            userDao.insertAll(User(i, "User $i", 20 + i))
//            val currentTime = System.currentTimeMillis() // Correct, currentTime is Long
//            savingsDao.insertAll(Savings(i, "Savings $i", 500f + 10f * i, currentTime, i))
//            incomesDao.insertAll(Income(i, "Income $i", 500f + 10f * i, i))
//            expenseCategoriesDao.insertAll(ExpenseCategory(i, "Expense category $i", 500f + 10f * i, i))
//            expensesDao.insertAll(Expense(i, "Expense $i", 500f + 10f * i, currentTime, i))
//        }
//
//        // Retrieving and displaying entities
//        val users = userDao.getAll()
//        users.forEach {
//            Log.d("User", it.toString())
//        }
//        val savings = savingsDao.getAll()
//        savings.forEach {
//            Log.d("Saving", it.toString())
//        }
//        val incomes = incomesDao.getAll()
//        incomes.forEach {
//            Log.d("Income", it.toString())
//        }
//        val expenseCategories = expenseCategoriesDao.getAll()
//        expenseCategories.forEach {
//            Log.d("Expense category", it.toString())
//        }
//        val expenses = expensesDao.getAll()
//        expenses.forEach {
//            Log.d("Expense", it.toString())
//        }

        val buttonAddIncome: Button = findViewById(R.id.addIncomeButton)
        val buttonAddSavings: Button = findViewById(R.id.addPreservationButton)

        // Set a click listener for the Add Income button
        buttonAddIncome.setOnClickListener {
            // Code to handle Add Income button click
            // You might want to show a dialog or start a new activity to add income
            showIncomeDialog()
        }

        // Set a click listener for the Add Savings button
        buttonAddSavings.setOnClickListener {
            // Code to handle Add Savings button click
            // Similar to the Add Income, show a dialog or start a new activity to add savings
            showSavingsDialog()
        }


    }
    private fun showIncomeDialog(income: Income? = null) {
        val dialogView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(50, 40, 50, 0)
        }

        val incomeNameEditText = EditText(this).apply {
            hint = "Name of Income"
            setText(income?.IncomeName ?: "")
        }
        val predictedIncomeEditText = EditText(this).apply {
            hint = "Predicted Income"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(income?.Amount?.toString() ?: "")
        }

        dialogView.addView(incomeNameEditText)
        dialogView.addView(predictedIncomeEditText)

        AlertDialog.Builder(this)
            .setTitle(if (income == null) "Add Income" else "Edit Income")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = incomeNameEditText.text.toString()
                val predictedIncome = predictedIncomeEditText.text.toString().toFloatOrNull()
                if (name.length in 5..100 && predictedIncome != null) {
                    if (income == null) {
                        // Add new income
                        val newIncome = Income(IncomeName = name, Amount = predictedIncome, UserId = 1) // Use actual user id
                        incomesDao.insertAll(newIncome)
                    } else {
                        // Update existing income
                        val updatedIncome = income.copy(IncomeName = name, Amount = predictedIncome)
                        incomesDao.update(updatedIncome)
                    }
                    // Refresh list or UI here
                } else {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Method to confirm and delete income
    private fun confirmDeleteIncome(income: Income) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete ${income.IncomeName}?")
            .setPositiveButton("Delete") { _, _ ->
                incomesDao.delete(income)
                // Refresh list or UI here
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Function to show dialog for adding or editing savings
    private fun showSavingsDialog(saving: Savings? = null) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 0)
        }

        val nameEditText = EditText(this).apply {
            hint = "Назва заощадження"
            setText(saving?.SavingsName ?: "")
        }

        val amountEditText = EditText(this).apply {
            hint = "Кінцева сума заощадження"
            setText(saving?.Amount?.toString() ?: "")
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        layout.addView(nameEditText)
        layout.addView(amountEditText)

        AlertDialog.Builder(this)
            .setTitle(if (saving == null) "Add Savings" else "Edit Savings")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val name = nameEditText.text.toString()
                val amount = amountEditText.text.toString().toFloat()
                if (saving == null) {
                    val newSaving = Savings(SavingsName = name, Amount = amount, Date = System.currentTimeMillis(), UserId = 1)  // Use actual UserId
                    savingsDao.insertAll(newSaving)
                } else {
                    val updatedSaving = saving.copy(SavingsName = name, Amount = amount)
                    savingsDao.update(updatedSaving)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Function to delete a savings
    private fun deleteSavings(saving: Savings) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete ${saving.SavingsName}?")
            .setPositiveButton("Delete") { _, _ ->
                savingsDao.delete(saving)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}