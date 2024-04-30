package com.example.myb
import ExpenseCategoryAdapter
import ExpenseNetworkManager
import IncomeAdapter
import IncomeNetworkManager
import SavingsAdapter
import SavingsNetworkManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.interfaces.UIUpdater
import com.example.myb.model.Savings
import com.example.myb.requests.ExpenseCategoryNetworkManager

class MainActivity : AppCompatActivity(), UIUpdater {
    lateinit var expenseCategoryNetworkManager: ExpenseCategoryNetworkManager
    private lateinit var expenseNetworkManager: ExpenseNetworkManager
    lateinit var savingsNetworkManager: SavingsNetworkManager
    lateinit var incomeNetworkManager: IncomeNetworkManager

    private lateinit var categoryAdapter: ExpenseCategoryAdapter
    private lateinit var incomeAdapter: IncomeAdapter
    private lateinit var savingsAdapter: SavingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenseCategoryNetworkManager = ExpenseCategoryNetworkManager(this)
        expenseNetworkManager = ExpenseNetworkManager(this)
        savingsNetworkManager = SavingsNetworkManager(this)
        incomeNetworkManager = IncomeNetworkManager(this)

        setupRecyclerViews()
        setupButtons()
    }

    private fun setupRecyclerViews() {
        val incomeRecyclerView = findViewById<RecyclerView>(R.id.incomeRecyclerView)
        incomeAdapter = IncomeAdapter(listOf(), this)
        incomeRecyclerView.adapter = incomeAdapter
        incomeRecyclerView.layoutManager = LinearLayoutManager(this)

        val savingsRecyclerView = findViewById<RecyclerView>(R.id.savingsRecyclerView)
        savingsAdapter = SavingsAdapter(listOf(), this)
        savingsRecyclerView.adapter = savingsAdapter
        savingsRecyclerView.layoutManager = LinearLayoutManager(this)

        val categoryRecyclerView = findViewById<RecyclerView>(R.id.expenseCategoryRecyclerView)
        categoryAdapter = ExpenseCategoryAdapter(listOf(), this)
        categoryRecyclerView.adapter = categoryAdapter
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun setupButtons() {
        findViewById<Button>(R.id.addIncomeButton).setOnClickListener {
            showIncomeDialog(null)
        }
        findViewById<Button>(R.id.addSavingButton).setOnClickListener {
            showSavingsDialog(null)
        }
        findViewById<Button>(R.id.addExpenseCategoryButton).setOnClickListener {
            showCategoryDialog(null)
        }
    }

    fun showIncomeDialog(income: Income?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.item_income, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.income_name_text_view)
        val amountInput = dialogView.findViewById<EditText>(R.id.amount_text_view)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString()
                val amount = amountInput.text.toString().toFloatOrNull() ?: 0f
                if (income == null) {
                    incomeNetworkManager.createIncome(name, amount, 1) // Assuming a static user ID for demonstration
                } else {
                    incomeNetworkManager.updateIncome(income.id, name, amount)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    fun showSavingsDialog(savings: Savings?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.item_savings, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.textViewSavingsName)
        val amountInput = dialogView.findViewById<EditText>(R.id.textViewAmount)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString()
                val amount = amountInput.text.toString().toFloatOrNull() ?: 0f
                if (savings == null) {
                    savingsNetworkManager.createSavings(name, amount, 1) // Assuming a static user ID for demonstration
                } else {
                    savingsNetworkManager.updateSavings(savings.id, name, amount)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    fun showCategoryDialog(category: ExpenseCategory?) {
        // Inflate layout and setup dialog for adding or editing category
    }

    override fun runOnUIThread(action: () -> Unit) {
        runOnUiThread(action)
    }

    override fun getContext(): Context {
        return this
    }

//    private fun showIncomeDialog(income: Income? = null) {
//        val dialogView = LinearLayout(this).apply {
//            orientation = LinearLayout.VERTICAL
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            setPadding(50, 40, 50, 0)
//        }
//
//        val incomeNameEditText = EditText(this).apply {
//            hint = "Name of Income"
//            setText(income?.IncomeName ?: "")
//        }
//        val predictedIncomeEditText = EditText(this).apply {
//            hint = "Predicted Income"
//            inputType = InputType.TYPE_CLASS_NUMBER
//            setText(income?.Amount?.toString() ?: "")
//        }
//
//        dialogView.addView(incomeNameEditText)
//        dialogView.addView(predictedIncomeEditText)
//
//        AlertDialog.Builder(this)
//            .setTitle(if (income == null) "Add Income" else "Edit Income")
//            .setView(dialogView)
//            .setPositiveButton("Save") { _, _ ->
//                val name = incomeNameEditText.text.toString()
//                val predictedIncome = predictedIncomeEditText.text.toString().toFloatOrNull()
//                if (name.length in 5..100 && predictedIncome != null) {
//                    if (income == null) {
//                        // Add new income
//                        val newIncome = Income(IncomeName = name, Amount = predictedIncome, UserId = 1) // Use actual user id
//                        incomesDao.insertAll(newIncome)
//                    } else {
//                        // Update existing income
//                        val updatedIncome = income.copy(IncomeName = name, Amount = predictedIncome)
//                        incomesDao.update(updatedIncome)
//                    }
//                    // Refresh list or UI here
//                } else {
//                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//    // Method to confirm and delete income
//    private fun confirmDeleteIncome(income: Income) {
//        AlertDialog.Builder(this)
//            .setMessage("Are you sure you want to delete ${income.IncomeName}?")
//            .setPositiveButton("Delete") { _, _ ->
//                incomesDao.delete(income)
//                // Refresh list or UI here
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//    // Function to show dialog for adding or editing savings
//    private fun showSavingsDialog(saving: Savings? = null) {
//        val layout = LinearLayout(this).apply {
//            orientation = LinearLayout.VERTICAL
//            setPadding(50, 40, 50, 0)
//        }
//
//        val nameEditText = EditText(this).apply {
//            hint = "Назва заощадження"
//            setText(saving?.SavingsName ?: "")
//        }
//
//        val amountEditText = EditText(this).apply {
//            hint = "Кінцева сума заощадження"
//            setText(saving?.Amount?.toString() ?: "")
//            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
//        }
//
//        layout.addView(nameEditText)
//        layout.addView(amountEditText)
//
//        AlertDialog.Builder(this)
//            .setTitle(if (saving == null) "Add Savings" else "Edit Savings")
//            .setView(layout)
//            .setPositiveButton("Save") { _, _ ->
//                val name = nameEditText.text.toString()
//                val amount = amountEditText.text.toString().toFloat()
//                if (saving == null) {
//                    val newSaving = Savings(SavingsName = name, Amount = amount, Date = System.currentTimeMillis(), UserId = 1)  // Use actual UserId
//                    savingsDao.insertAll(newSaving)
//                } else {
//                    val updatedSaving = saving.copy(SavingsName = name, Amount = amount)
//                    savingsDao.update(updatedSaving)
//                }
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//    // Function to delete a savings
//    private fun deleteSavings(saving: Savings) {
//        AlertDialog.Builder(this)
//            .setMessage("Are you sure you want to delete ${saving.SavingsName}?")
//            .setPositiveButton("Delete") { _, _ ->
//                savingsDao.delete(saving)
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
}