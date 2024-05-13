
import android.widget.Toast
import com.example.myb.interfaces.UIUpdater
import com.example.myb.utils.ApiConfig
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class IncomeNetworkManager(private val uiUpdater: UIUpdater) {
    private val baseUrl = ApiConfig.BASE_URL + "/incomes"

    fun createIncome(incomeName: String, amount: Float, date: String, userId: Int, callback: (Result<Int>) -> Unit) {
        Thread {
            try {
                val url = URL("$baseUrl/incomes")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val postData = """
                {
                    "income_name": "$incomeName",
                    "amount": $amount,
                    "date": "$date",
                    "user_id": $userId
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
                    val incomeId = JSONObject(response).getInt("id")  // Assumes 'id' is the key for the income ID in the response JSON
                    uiUpdater.runOnUIThread {
                        callback(Result.success(incomeId))
                    }
                } else {
                    uiUpdater.runOnUIThread {
                        callback(Result.failure(RuntimeException("Failed to create income: $responseCode")))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    callback(Result.failure(RuntimeException("Failed to create income: ${e.localizedMessage}")))
                }
            }
        }.start()
    }

    fun updateIncome(incomeId: Int, newName: String?, newAmount: Float?) {
        Thread {
            try {
                val url = URL("$baseUrl/incomes?income_id=$incomeId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val updatedData = """
                    {
                        "income_name": "${newName ?: ""}",
                        "amount": ${newAmount ?: "null"}
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
                        Toast.makeText(context, "Income updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update income: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to update income: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun deleteIncome(incomeId: Int) {
        Thread {
            try {
                val url = URL("$baseUrl/incomes?income_id=$incomeId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"

                val responseCode = connection.responseCode
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(context, "Income deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete income: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to delete income: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
