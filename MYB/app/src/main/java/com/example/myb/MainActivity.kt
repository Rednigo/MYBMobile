package com.example.myb

import com.example.myb.utils.ApiConfig
import ExpenseAdapter
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.interfaces.UIUpdater
import com.example.myb.model.Savings
import com.example.myb.requests.ExpenseCategoryNetworkManager
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), UIUpdater {
    lateinit var expenseCategoryNetworkManager: ExpenseCategoryNetworkManager
    lateinit var expenseNetworkManager: ExpenseNetworkManager
    lateinit var savingsNetworkManager: SavingsNetworkManager
    lateinit var incomeNetworkManager: IncomeNetworkManager

    private lateinit var categoryAdapter: ExpenseCategoryAdapter
    private lateinit var incomeAdapter: IncomeAdapter
    private lateinit var savingsAdapter: SavingsAdapter
    private lateinit var expenseAdapter: ExpenseAdapter

    private val baseUrl = ApiConfig.BASE_URL

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
        categoryAdapter = ExpenseCategoryAdapter(listOf(), this) { categoryId, expenseAdapter ->
            // This lambda now correctly fetches and updates expenses for the given category ID
            fetchExpensesForCategory(categoryId, expenseAdapter)
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
        Thread {
            try {
                //val userId = 1 // Assuming a static user ID for demonstration
                val userId = intent.getIntExtra("USER_ID", -1)  // -1 as default if not found
                //val url = URL("http://192.168.0.163:8080/api/v1/incomes/incomes?user_id=$userId")
                val url = URL("$baseUrl/incomes/incomes?user_id=$userId")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "GET"

                val responseCode = httpURLConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = httpURLConnection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jArray = JSONArray(response.toString())
                    val incomes = mutableListOf<Income>()
                    for (i in 0..jArray.length()-1) {
                        var jobject = jArray.getJSONObject(i)
                        var user_id = jobject.getInt("user_id")
                        var id = jobject.getInt("id")
                        var income_name = jobject.getString("income_name")
                        var amount = jobject.getInt("amount")
                        incomes.add(Income(
                            IncomeName = income_name,
                            Amount = amount.toFloat(),
                            id = id,
                            UserId = user_id
                        ))
                    }
                    // Update the RecyclerView on the UI thread
                    runOnUiThread {
                        incomeAdapter.updateData(incomes)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to fetch incomes", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun fetchAndDisplaySavings() {
        Thread {
            try {
                val userId = 1 // Assuming a static user ID for demonstration
                //val url = URL("http://192.168.0.163:8080/api/v1/incomes/savings?user_id=$userId")
                val url = URL("$baseUrl/savings/savings?user_id=$userId")

                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "GET"

                val responseCode = httpURLConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = httpURLConnection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jArray = JSONArray(response.toString())
                    val savings = mutableListOf<Savings>()
                    for (i in 0..jArray.length()-1) {
                        var jobject = jArray.getJSONObject(i)
                        var user_id = jobject.getInt("user_id")
                        var id = jobject.getInt("id")
                        var savings_name = jobject.getString("savings_name")
                        var amount = jobject.getInt("amount")
                        var date = jobject.getString("date")
                        savings.add(
                            Savings(
                                SavingsName = savings_name,
                            Amount = amount.toFloat(),
                            id = id,
                            UserId = user_id,
                                Date = date
                            ))
                    }
                    // Update the RecyclerView on the UI thread
                    runOnUiThread {
                        savingsAdapter.updateData(savings)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to fetch savings", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun fetchAndDisplayExpenseCategories() {
        Thread {
            try {
                val userId = 1 // Assuming a static user ID for demonstration
                //val url = URL("http://192.168.0.163:8080/api/v1/expenses/categories?user_id=$userId")
                val url = URL("$baseUrl/categories/expense-categories?user_id=$userId")

                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "GET"

                val responseCode = httpURLConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = httpURLConnection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jArray = JSONArray(response.toString())
                    val categories = mutableListOf<ExpenseCategory>()
                    for (i in 0 until jArray.length()) {
                        val jsonObject = jArray.getJSONObject(i)
                        val id = jsonObject.getInt("id")
                        var user_id = jsonObject.getInt("user_id")
                        val categoryName = jsonObject.getString("category_name")
                        val amount = jsonObject.getDouble("amount").toFloat()
                        categories.add(ExpenseCategory(id = id,
                            CategoryName = categoryName,
                            UserId = user_id,
                            Amount = amount))
                    }
                    runOnUiThread {
                        categoryAdapter.updateCategories(categories)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }


    fun fetchExpensesForCategory(categoryId: Int, expenseAdapter: ExpenseAdapter) {
        Thread {
            try {
                val url = URL("http://192.168.0.163:8080/api/v1/expenses?category_id=$categoryId")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "GET"

                val responseCode = httpURLConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = httpURLConnection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jArray = JSONArray(response)
                    val expenses = mutableListOf<Expense>()
                    for (i in 0 until jArray.length()) {
                        val jsonObject = jArray.getJSONObject(i)
                        val id = jsonObject.getInt("id")
                        val expenseName = jsonObject.getString("expense_name")
                        val amount = jsonObject.getDouble("amount").toFloat()
                        val date = jsonObject.getString("date")
                        expenses.add(Expense(id = id,
                            CategoryId = categoryId,
                            ExpenseName = expenseName,
                            Amount = amount,
                            Date = date))
                    }
                    runOnUiThread {
                        // Assuming there's a method to update expenses in your ExpenseCategoryAdapter
                        expenseAdapter.updateExpenses(expenses)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to fetch expenses for category", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
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
                    fetchAndDisplayIncome()
                } else {
                    incomeNetworkManager.updateIncome(income.id, name, amount)
                    fetchAndDisplayIncome()
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
                    savingsNetworkManager.createSavings(name, amount, 1)
                    fetchAndDisplaySavings()
                // Assuming a static user ID for demonstration
                } else {
                    savingsNetworkManager.updateSavings(savings.id, name, amount)
                    fetchAndDisplaySavings()
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
                    fetchAndDisplayExpenseCategories()
                } else {
                    category.CategoryName = name
                    category.Amount = budget
                    expenseCategoryNetworkManager.updateExpenseCategory(category.id, name, budget)
                    fetchAndDisplayExpenseCategories()
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
                    expenseNetworkManager.createExpense(name, amount, System.currentTimeMillis(), 1)
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

    override fun Gson(): Any {
        TODO("Not yet implemented")
    }
}