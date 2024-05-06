package com.example.myb



//import android.text.InputType
//import android.view.ViewGroup
//import com.example.myb.dao.ExpenseCategoryDao
//import com.example.myb.dao.SavingsDao
//import com.example.myb.dao.UserDao
//import com.example.myb.database.AppDatabase


//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import com.example.myb.model.Savings
import ExpenseAdapter
import ExpenseCategoryAdapter
import ExpenseNetworkManager
import IncomeAdapter
import IncomeNetworkManager
import SavingsAdapter
import SavingsNetworkManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.interfaces.UIUpdater
import com.example.myb.model.Savings
import com.example.myb.requests.ExpenseCategoryNetworkManager
import com.example.myb.utils.ApiConfig
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

class MainActivity : AppCompatActivity(), UIUpdater {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_FILE = ApiConfig.PREF_FILE

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

        expenseCategoryNetworkManager = ExpenseCategoryNetworkManager(this)
        expenseNetworkManager = ExpenseNetworkManager(this)
        savingsNetworkManager = SavingsNetworkManager(this)
        incomeNetworkManager = IncomeNetworkManager(this)

        sharedPreferences = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        if (getUserLanguage() == "uk") {
            setContentView(R.layout.activity_main_uk)
        }
        else {
            setContentView(R.layout.activity_main)
        }

        setupRecyclerViews()
        setupButtons()

        fetchAndDisplayIncome()
        fetchAndDisplaySavings()
        fetchAndDisplayExpenseCategories()

//        val backButton = findViewById<LinearLayout>(R.id.backButtonLayoutStat)
//
//        backButton.setOnClickListener {
//            // Створюємо інтент для переходу на MainActivity
//            val intent = Intent(this, MainActivity::class.java)
//            // Запускаємо MainActivity
//            startActivity(intent)
//            // Завершуємо поточну активність
//            finish()
//        }

        val spinner = findViewById<Spinner>(R.id.dropdownSpinner)

        // Створіть пустий адаптер для Spinner
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item_header)

        // Встановіть макет для випадаючого списку
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Додайте текст-підказку до адаптера (це перший елемент у списку)

        adapter.add("")
        adapter.add("Statistics")
        adapter.add("Home")
        adapter.add("Settings")


        // Встановіть адаптер для Spinner
        spinner.adapter = adapter

        // Встановіть підказку
        spinner.prompt = "Choose an option"

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    1 -> {
                        // Створюємо інтент для переходу на SettingsActivity
                        val intent = Intent(this@MainActivity, StatisticsActivity::class.java)
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }

                    2 -> {
                        // Створюємо інтент для переходу на SettingsActivity
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }

                    3 -> {
                        // Створюємо інтент для переходу на StatisticsActivity
                        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                        // Запускаємо StatisticsActivity
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }
                    // Додайте інші варіанти, якщо потрібно
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Нічого не робимо, якщо нічого не вибрано
            }
        }
    }

    private fun getUserId(): Int {
        return sharedPreferences.getInt("USER_ID", -1) // -1 as default if not found
    }

    private fun getUserLanguage(): String? {
        return sharedPreferences.getString("LANG", "en") // -1 as default if not found
    }

    private fun setupRecyclerViews() {
        val incomeRecyclerView = findViewById<RecyclerView>(R.id.incomeRecyclerView)
        incomeAdapter = IncomeAdapter(mutableListOf(), this)
        incomeRecyclerView.adapter = incomeAdapter
        incomeRecyclerView.layoutManager = LinearLayoutManager(this)

        val savingsRecyclerView = findViewById<RecyclerView>(R.id.savingsRecyclerView)
        savingsAdapter = SavingsAdapter(mutableListOf(), this)
        savingsRecyclerView.adapter = savingsAdapter
        savingsRecyclerView.layoutManager = LinearLayoutManager(this)

        val categoryRecyclerView = findViewById<RecyclerView>(R.id.expenseCategoryRecyclerView)
        categoryAdapter = ExpenseCategoryAdapter(mutableListOf(), this) { categoryId, expenseAdapter ->
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
                val userId = getUserId()
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
                        var user_id = userId
                        var id = jobject.getInt("id")
                        var income_name = jobject.getString("income_name")
                        var amount = jobject.getInt("amount").toFloat()
                        var date = jobject.getString("date")
                        Log.d("Amount", amount.toString())
                        incomes.add(Income(
                            IncomeName = income_name,
                            Amount = amount,
                            id = id,
                            Date = date,
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
                val userId = getUserId()
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
                        var user_id = userId
                        var id = jobject.getInt("id")
                        var savings_name = jobject.getString("savings_name")
                        var amount = jobject.getInt("amount").toFloat()
                        var date = jobject.getString("date")
                        savings.add(
                            Savings(
                                SavingsName = savings_name,
                                Amount = amount,
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
                val userId = getUserId()
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
                        var user_id = userId
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
                // Ensure the endpoint is correct and that it uses `category_id` as the query parameter
                val url = URL("$baseUrl/expenses/expenses?category_id=$categoryId")
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
                        val amount = jsonObject.getDouble("amount").toFloat()  // Ensure parsing as float if needed
                        val date = jsonObject.getString("date")
                        expenses.add(Expense(id = id,
                            CategoryId = categoryId,
                            ExpenseName = expenseName,
                            Amount = amount,
                            Date = date))
                    }
                    runOnUiThread {
                        // This should refer to a method to update the specific expense adapter with new data
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
        val dialogView = if (getUserLanguage() == "uk") {
            LayoutInflater.from(this).inflate(R.layout.item_income_edit_uk, null)
        } else {
            LayoutInflater.from(this).inflate(R.layout.item_income_edit, null)
        }

        val nameInput = dialogView.findViewById<EditText>(R.id.income_name_text_view)
        val amountInput = dialogView.findViewById<EditText>(R.id.amount_text_view)
        val currentDate = LocalDate.now().toString()  // Get current date in ISO format

        income?.let {
            nameInput.setText(it.IncomeName)
            amountInput.setText(it.Amount.toString())
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString()
                val amount = amountInput.text.toString().toFloatOrNull() ?: 0f
                if (income == null) {
                    incomeNetworkManager.createIncome(name, amount, currentDate, getUserId()) { result ->
                        result.fold(
                            onSuccess = { incomeId ->
                                val newIncome = Income(
                                    id = incomeId,
                                    IncomeName = name,
                                    Amount = amount,
                                    Date = currentDate,
                                    UserId = getUserId()
                                )
                                runOnUiThread {
                                    incomeAdapter.addIncome(newIncome)
                                }
                            },
                            onFailure = { exception ->
                                runOnUiThread {
                                    Toast.makeText(this, "Error creating income: ${exception.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                } else {
                    incomeNetworkManager.updateIncome(income.id, name, amount)
                    val updatedIncome = Income(
                        id = income.id,
                        IncomeName = name,
                        Amount = amount,
                        Date = income.Date,
                        UserId = income.UserId
                    )
                    runOnUiThread {
                        incomeAdapter.updateIncome(updatedIncome)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }


    fun showSavingsDialog(savings: Savings?) {
        val dialogView = if (getUserLanguage() == "uk") {
            LayoutInflater.from(this).inflate(R.layout.item_savings_edit_uk, null)
        } else {
            LayoutInflater.from(this).inflate(R.layout.item_savings_edit, null)
        }
        val nameInput = dialogView.findViewById<EditText>(R.id.textViewSavingsName)
        val amountInput = dialogView.findViewById<EditText>(R.id.textViewAmount)
        val currentDate = LocalDate.now().toString()  // Get current date in ISO format

        savings?.let {
            nameInput.setText(it.SavingsName)
            amountInput.setText(it.Amount.toString())
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString()
                val amount = amountInput.text.toString().toFloatOrNull() ?: 0f
                if (savings == null) {
                    // Asynchronously create a new savings entry with the current date
                    savingsNetworkManager.createSavings(name, amount, getUserId(), currentDate) { result ->
                        result.fold(
                            onSuccess = { savingsId ->
                                val newSavings = Savings(
                                    id = savingsId,
                                    SavingsName = name,
                                    Amount = amount,
                                    Date = currentDate,
                                    UserId = getUserId()
                                )
                                runOnUiThread {
                                    savingsAdapter.addSavings(newSavings)
                                }
                            },
                            onFailure = { exception ->
                                runOnUiThread {
                                    Toast.makeText(this, "Error creating savings: ${exception.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                } else {
                    // Update existing savings entry with the current date
                    savingsNetworkManager.updateSavings(savings.id, name, amount)
                    val updatedSavings = Savings(
                        id = savings.id,
                        SavingsName = name,
                        Amount = amount,
                        Date = savings.Date,
                        UserId = savings.UserId
                    )
                    runOnUiThread {
                        savingsAdapter.updateSavings(updatedSavings)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }



    fun showCategoryDialog(category: ExpenseCategory?) {
        val dialogView = if (getUserLanguage() == "uk") {
            LayoutInflater.from(this).inflate(R.layout.item_expense_category_edit_uk, null)
        } else {
            LayoutInflater.from(this).inflate(R.layout.item_expense_category_edit, null)
        }
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
                    // Asynchronously create a new category
                    expenseCategoryNetworkManager.createExpenseCategory(name, budget, getUserId()) { result ->
                        result.fold(
                            onSuccess = { categoryId ->
                                val newCategory = ExpenseCategory(
                                    id = categoryId,
                                    CategoryName = name,
                                    Amount = budget,
                                    UserId = getUserId()
                                )
                                runOnUiThread {
                                    categoryAdapter.addCategory(newCategory)
                                }
                            },
                            onFailure = { exception ->
                                runOnUiThread {
                                    Toast.makeText(this, "Error creating category: ${exception.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                } else {
                    // Update existing category
                    category.CategoryName = name
                    category.Amount = budget
                    expenseCategoryNetworkManager.updateExpenseCategory(category.id, name, budget)
                    runOnUiThread {
                        categoryAdapter.updateCategory(category)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    fun showExpenseDialog(expense: Expense?, categoryId: Int) {
        val dialogView = if (getUserLanguage() == "uk") {
            LayoutInflater.from(this).inflate(R.layout.item_expense_edit_uk, null)
        } else {
            LayoutInflater.from(this).inflate(R.layout.item_expense_edit, null)
        }
        val nameInput = dialogView.findViewById<EditText>(R.id.textViewExpenseName)
        val amountInput = dialogView.findViewById<EditText>(R.id.textViewExpenseAmount)
        val currentDate = LocalDate.now().toString()

        expense?.let {
            nameInput.setText(it.ExpenseName)
            amountInput.setText(it.Amount.toString())
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(if (expense == null) "Add Expense" else "Edit Expense")
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString()
                val amount = amountInput.text.toString().toFloatOrNull() ?: 0f

                if (expense == null) {
                    // Asynchronously create a new Expense with the specific categoryId
                    expenseNetworkManager.createExpense(name, amount, currentDate, categoryId) { result ->
                        result.fold(
                            onSuccess = { expenseId ->
                                val newExpense = Expense(
                                    id = expenseId,
                                    ExpenseName = name,
                                    Amount = amount,
                                    Date = currentDate,
                                    CategoryId = categoryId
                                )
                                runOnUiThread {
                                    expenseAdapter.addExpense(newExpense)
                                }
                            },
                            onFailure = { exception ->
                                runOnUiThread {
                                    Toast.makeText(this, "Error creating expense: ${exception.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                } else {
                    // Update the existing Expense with the new details
                    expenseNetworkManager.updateExpense(expense.id, name, amount, currentDate)
                    runOnUiThread {
                        expenseAdapter.updateExpense(
                            Expense(id = expense.id,
                                ExpenseName = name,
                                Amount = amount,
                                Date = currentDate,
                                CategoryId = expense.CategoryId))
                    }
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