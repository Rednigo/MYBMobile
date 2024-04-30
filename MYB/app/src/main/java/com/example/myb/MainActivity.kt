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
    lateinit var expenseNetworkManager: ExpenseNetworkManager
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

        fetchAndDisplayIncome()
        fetchAndDisplaySavings()
        fetchAndDisplayExpenseCategories()
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
        categoryAdapter = ExpenseCategoryAdapter(listOf(), this) { categoryId ->
            // This lambda needs to fetch and return expenses for the given category ID
            fetchExpensesForCategory(categoryId)
        }
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
    fun fetchAndDisplayIncome() {
        // Simulate fetching data
        val incomes = listOf<Income>() // Replace with actual data fetch logic
        runOnUiThread {
            incomeAdapter.updateData(incomes)
        }
    }

    fun fetchAndDisplaySavings() {
        val savings = listOf<Savings>() // Replace with actual data fetch logic
        runOnUiThread {
            savingsAdapter.updateData(savings)
        }
    }

    fun fetchAndDisplayExpenseCategories() {
        val expenseCategories = listOf<ExpenseCategory>() // Replace with actual data fetch logic
        runOnUiThread {
            categoryAdapter.updateCategories(expenseCategories)
        }
    }

    fun fetchExpensesForCategory(categoryId: Int): List<Expense> {
        // Fetch expenses for the given category ID
        // This is a placeholder; you'll need to replace it with actual fetch logic
        return listOf() // Return fetched expenses
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
        val layoutInflater = LayoutInflater.from(this)
        val dialogView = layoutInflater.inflate(R.layout.item_expense_category, null)
        val categoryNameInput = dialogView.findViewById<EditText>(R.id.textViewCategoryName)
        val categoryBudgetInput = dialogView.findViewById<EditText>(R.id.textViewCategoryBudget)

        category?.let {
            categoryNameInput.setText(it.CategoryName)
            categoryBudgetInput.setText(it.Amount.toString())
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(if (category == null) "Add Category" else "Edit Category")
            .setPositiveButton("Save") { _, _ ->
                val name = categoryNameInput.text.toString()
                val budget = categoryBudgetInput.text.toString().toFloatOrNull() ?: 0f
                if (category == null) {
                    expenseCategoryNetworkManager.createExpenseCategory(name, budget, 1) // Assume UserId is 1
                } else {
                    category.CategoryName = name
                    category.Amount = budget
                    expenseCategoryNetworkManager.updateExpenseCategory(category.id, name, budget)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun showExpenseDialog(expense: Expense?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.item_expense, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.textViewExpenseName)
        val amountInput = dialogView.findViewById<EditText>(R.id.textViewExpenseAmount)
        expense?.let {
            nameInput.setText(it.ExpenseName)
            amountInput.setText(it.Amount.toString())
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(if (expense == null) "Add Expense" else "Edit Expense")
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString()
                val amount = amountInput.text.toString().toFloat()
                if (expense == null) {
                    expenseNetworkManager.createExpense(name, amount, System.currentTimeMillis(), 1) // Assume CategoryId is 1
                } else {
                    expenseNetworkManager.updateExpense(expense.id, name, amount, System.currentTimeMillis())
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun runOnUIThread(action: () -> Unit) {
        runOnUiThread(action)
    }

    override fun getContext(): Context {
        return this
    }
}