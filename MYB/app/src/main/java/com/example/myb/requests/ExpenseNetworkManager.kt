
import android.widget.Toast
import com.example.myb.Expense
import com.example.myb.interfaces.UIUpdater
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ExpenseNetworkManager(private val uiUpdater: UIUpdater) {
    private val baseUrl = "http://192.168.0.76:8080/api/v1/expenses"

    interface ExpenseFetchListener {
        fun onExpensesFetched(expenses: List<Expense>)
        fun onError(message: String)
    }

    fun fetchAllExpenses() {
        Thread {
            try {
                val url = URL(baseUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                val response = connection.inputStream.bufferedReader().use { it.readText() }

                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Parse response and update UI
                    } else {
                        Toast.makeText(context, "Failed to fetch expenses: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to fetch expenses: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun fetchExpensesByCategoryId(categoryId: Int) {
        Thread {
            try {
                val url = URL("$baseUrl/category?category_id=$categoryId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                val response = connection.inputStream.bufferedReader().use { it.readText() }  // Assuming the response is JSON

                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Parse response and update UI
                        // Here you would typically convert the JSON response to model objects
                        // For example, using Gson or Moshi to parse the JSON into a list of Expense objects
                    } else {
                        Toast.makeText(context, "Failed to fetch expenses for category: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to fetch expenses for category: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun createExpense(expenseName: String, amount: Float, date: Long, categoryId: Int) {
        Thread {
            try {
                val url = URL("$baseUrl/expenses")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val postData = """
                    {
                        "expense_name": "$expenseName",
                        "amount": $amount,
                        "date": $date,
                        "category_id": $categoryId
                    }
                """.trimIndent()

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(postData)
                    writer.flush()
                }

                val responseCode = connection.responseCode
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(context, "Expense created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to create expense: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to create expense: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun updateExpense(expenseId: Int, newName: String?, newAmount: Float?, newDate: Long?) {
        Thread {
            try {
                val url = URL("$baseUrl/expenses?expense_id$expenseId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val updatedData = """
                    {
                        "expense_name": "${newName ?: ""}",
                        "amount": ${newAmount ?: "null"},
                        "date": ${newDate ?: "null"}
                    }
                """.trimIndent()

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(updatedData)
                    writer.flush()
                }

                val responseCode = connection.responseCode
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(context, "Expense updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update expense: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to update expense: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun deleteExpense(expenseId: Int) {
        Thread {
            try {
                val url = URL("$baseUrl/expenses?expense_id=$expenseId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"

                val responseCode = connection.responseCode
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(context, "Expense deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete expense: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to delete expense: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
