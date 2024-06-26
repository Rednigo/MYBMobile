import android.widget.Toast;
import com.example.myb.interfaces.UIUpdater;
import com.example.myb.utils.ApiConfig;
import org.json.JSONObject
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class SavingsNetworkManager(private val uiUpdater: UIUpdater) {
    private val baseUrl = ApiConfig.BASE_URL + "/savings"

    fun createSavings(savingsName: String, amount: Float, userId: Int, date: String, callback: (Result<Int>) -> Unit) {
        Thread {
            try {
                val url = URL("$baseUrl/savings")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val postData = """
                {
                    "savings_name": "$savingsName",
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
                    val savingsId = JSONObject(response).getInt("id")  // Assumes 'id' is the key for the savings ID in the response JSON
                    uiUpdater.runOnUIThread {
                        callback(Result.success(savingsId))
                    }
                } else {
                    uiUpdater.runOnUIThread {
                        callback(Result.failure(RuntimeException("Failed to create savings: $responseCode")))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    callback(Result.failure(RuntimeException("Failed to create savings: ${e.localizedMessage}")))
                }
            }
        }.start()
    }


    fun updateSavings(savingsId: Int, newName: String?, newAmount: Float?) {
        Thread {
            try {
                val url = URL("$baseUrl/savings?savings_id=$savingsId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val updatedData = """
                    {
                        "savings_name": "${newName ?: ""}",
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
                        Toast.makeText(context, "Savings updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update savings: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to update savings: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun deleteSavings(savingsId: Int) {
        Thread {
            try {
                val url = URL("$baseUrl/savings?savings_id=$savingsId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"

                val responseCode = connection.responseCode
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(context, "Savings deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete savings: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to delete savings: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
