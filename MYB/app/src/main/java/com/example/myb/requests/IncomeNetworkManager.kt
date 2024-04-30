import android.content.Context
import android.widget.Toast
import com.example.myb.interfaces.UIUpdater
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class IncomeNetworkManager(private val uiUpdater: UIUpdater) {
    private val baseUrl = "http://your-api-url/api/incomes"

    fun fetchAllIncomes() {
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
                        Toast.makeText(context, "Failed to fetch incomes: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to fetch incomes: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun createIncome(incomeName: String, amount: Float, userId: Int) {
        Thread {
            try {
                val url = URL("$baseUrl/create")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val postData = """
                    {
                        "income_name": "$incomeName",
                        "amount": $amount,
                        "user_id": $userId
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
                        Toast.makeText(context, "Income created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to create income: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to create income: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun updateIncome(incomeId: Int, newName: String?, newAmount: Float?) {
        Thread {
            try {
                val url = URL("$baseUrl/update/$incomeId")
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
                val url = URL("$baseUrl/delete/$incomeId")
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