
import android.util.Log
import android.widget.Toast
import com.example.myb.interfaces.UIUpdater
import com.example.myb.utils.ApiConfig
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ExpenseNetworkManager(private val uiUpdater: UIUpdater) {
    private val baseUrl = ApiConfig.BASE_URL + "/expenses"


    fun createExpense(expenseName: String, amount: Float, date: String, categoryId: Int, callback: (Result<Int>) -> Unit) {
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
                    "date": "$date",
                    "category_id": $categoryId
                }
            """.trimIndent()

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(postData)
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val expenseId = JSONObject(response).getInt("id")  // Assumes 'id' is the key for the expense ID in the response JSON
                    uiUpdater.runOnUIThread {
                        callback(Result.success(expenseId))
                    }
                } else {
                    uiUpdater.runOnUIThread {
                        callback(Result.failure(RuntimeException("Failed to create expense: $responseCode")))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    callback(Result.failure(RuntimeException("Failed to create expense: ${e.localizedMessage}")))
                }
            }
        }.start()
    }


    fun updateExpense(expenseId: Int, newName: String?, newAmount: Float?) {
        Thread {
            try {
                val url = URL("$baseUrl/expenses?expense_id=$expenseId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                Log.d("Update expense", newName + newAmount.toString())
                val updatedData = """
                    {
                        "expense_name": "${newName ?: ""}",
                        "amount": ${newAmount ?: "null"},
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
