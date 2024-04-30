package com.example.myb
import ExpenseNetworkManager
import IncomeNetworkManager
import SavingsNetworkManager
import StatisticsFragment
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myb.dao.SavingsDao
import com.example.myb.interfaces.UIUpdater
import com.example.myb.model.Savings
import com.example.myb.requests.ExpenseCategoryNetworkManager

class MainActivity : AppCompatActivity(), UIUpdater {
    private lateinit var expenseCategoryNetworkManager: ExpenseCategoryNetworkManager
    private lateinit var expenseNetworkManager: ExpenseNetworkManager
    private lateinit var savingsNetworkManager: SavingsNetworkManager
    private lateinit var incomeNetworkManager: IncomeNetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenseCategoryNetworkManager = ExpenseCategoryNetworkManager(this)
        expenseNetworkManager = ExpenseNetworkManager(this)
        savingsNetworkManager = SavingsNetworkManager(this)
        incomeNetworkManager = IncomeNetworkManager(this)

        setupSavings()
        setupIncome()
    }

    private fun setupSavings() {
        val btnCreateSavings: Button = findViewById(R.id.btnCreateSavings)
        btnCreateSavings.setOnClickListener {
            val savingsName = findViewById<EditText>(R.id.etSavingsName).text.toString()
            val amount = findViewById<EditText>(R.id.etSavingsAmount).text.toString().toFloat()
            val userId = 1  // Assuming a static user ID for demonstration

            savingsNetworkManager.createSavings(savingsName, amount, userId)
        }

        val btnUpdateSavings: Button = findViewById(R.id.btnUpdateSavings)
        btnUpdateSavings.setOnClickListener {
            val savingsId = findViewById<EditText>(R.id.etSavingsId).text.toString().toInt()
            val newName = findViewById<EditText>(R.id.etNewSavingsName).text.toString()
            val newAmount = findViewById<EditText>(R.id.etNewSavingsAmount).text.toString().toFloat()

            savingsNetworkManager.updateSavings(savingsId, newName, newAmount)
        }

        val btnDeleteSavings: Button = findViewById(R.id.btnDeleteSavings)
        btnDeleteSavings.setOnClickListener {
            val savingsId = findViewById<EditText>(R.id.etSavingsId).text.toString().toInt()
            savingsNetworkManager.deleteSavings(savingsId)
        }
    }

    private fun setupIncome() {
        val btnCreateIncome: Button = findViewById(R.id.btnCreateIncome)
        btnCreateIncome.setOnClickListener {
            val incomeName = findViewById<EditText>(R.id.etIncomeName).text.toString()
            val amount = findViewById<EditText>(R.id.etIncomeAmount).text.toString().toFloat()
            val userId = 1  // Assuming a static user ID for demonstration

            incomeNetworkManager.createIncome(incomeName, amount, userId)
        }

        val btnUpdateIncome: Button = findViewById(R.id.btnUpdateIncome)
        btnUpdateIncome.setOnClickListener {
            val incomeId = findViewById<EditText>(R.id.etIncomeId).text.toString().toInt()
            val newName = findViewById<EditText>(R.id.etNewIncomeName).text.toString()
            val newAmount = findViewById<EditText>(R.id.etNewIncomeAmount).text.toString().toFloat()

            incomeNetworkManager.updateIncome(incomeId, newName, newAmount)
        }

        val btnDeleteIncome: Button = findViewById(R.id.btnDeleteIncome)
        btnDeleteIncome.setOnClickListener {
            val incomeId = findViewById<EditText>(R.id.etIncomeId).text.toString().toInt()
            incomeNetworkManager.deleteIncome(incomeId)
        }
    }

    override fun runOnUIThread(action: () -> Unit) {
        runOnUiThread(action)
    }

    override fun getContext(): Context {
        return this
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