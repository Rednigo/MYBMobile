package com.example.myb
import ExpenseCategoryAdapter
import ExpenseNetworkManager
import IncomeAdapter
import IncomeNetworkManager
import SavingsAdapter
import SavingsNetworkManager
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
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
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import org.json.JSONArray
import java.lang.reflect.Type
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
//    fun fetchAndDisplayIncome() {
//        Thread {
//            try {
//                val userId = 1 // Assuming a static user ID for demonstration
//                val url = URL("http://192.168.0.163:8080/api/v1/incomes/incomes?user_id=$userId")
//                val httpURLConnection = url.openConnection() as HttpURLConnection
//                httpURLConnection.requestMethod = "GET"
//
//                val responseCode = httpURLConnection.responseCode
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    val inputStream = httpURLConnection.inputStream
//                    val response = inputStream.bufferedReader().use { it.readText() }
//
//                    // Toast must be shown on the UI Thread
//                    runOnUiThread {
//                        Toast.makeText(this, response, Toast.LENGTH_LONG).show()
//                    }
////                    val inputStream = httpURLConnection.inputStream
////                    val response = inputStream.bufferedReader().use { it.readText() }
////                    Toast.makeText(this, response., Toast.LENGTH_SHORT).show()
//                    // Parse the JSON response to a list of Income objects
//                    // val incomes: List<Income> = parseJsonToIncomes(response)
//                    // Update the RecyclerView on the UI thread
////                    runOnUiThread {
////                        incomeAdapter.updateData(incomes)
////                    }
//                } else {
//                    runOnUiThread {
//                        Toast.makeText(this, "Failed to fetch incomes", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                runOnUiThread {
//                    Toast.makeText(this, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }.start()
//    }

    fun fetchAndDisplayIncome() {
        Thread {
            try {
                val userId = 1 // Assuming a static user ID for demonstration
                val url = URL("http://192.168.0.163:8080/api/v1/incomes/incomes?user_id=$userId")
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
                        Log.e("user_id", income_name.toString())
                        Log.e("user_id", income_name.toString())
                        Log.e("user_id", income_name.toString())
                        Log.e("income_name", income_name.toString())
                        Log.e("income_name", income_name.toString())
                        Log.e("user_id", income_name.toString())
                        Log.e("amount", amount.toString())
                        incomes.add(Income(
                            IncomeName = income_name,
                            Amount = amount.toFloat(),
                            id = id,
                            UserId = user_id
                        ))
                    }

//                    val incomes = parseJsonToIncomes(response)

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

//    private fun <T> fromJson(json: String, typeToken: TypeToken<T>): T {
//        val gson = Gson()
//        return gson.fromJson(json, typeToken.type) // Use Gson's fromJson directly.
//    }
//
//    private fun parseJsonToIncomes(json: String): List<Income> {
//        return fromJson(json, object : TypeToken<List<Income>>() {})
//    }

//    private fun parseJsonToIncomes(response: String): List<Income> {
//        val gson = Gson()
//        val incomeListType = object : TypeToken<List<Income>>() {}.type
//        return gson.fromJson(response, incomeListType)
//    }

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

    override fun Gson(): Any {
        TODO("Not yet implemented")
    }
}

//private fun Any.fromJson(json: String, type: Type?): List<Income> {
//
//}
