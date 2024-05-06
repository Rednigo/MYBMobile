package com.example.myb.requests

import android.widget.Toast
import com.example.myb.interfaces.UIUpdater
import com.example.myb.utils.ApiConfig
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ExpenseCategoryNetworkManager(private val uiUpdater: UIUpdater) {
    private val baseUrl = ApiConfig.BASE_URL + "/categories"

    fun createExpenseCategory(categoryName: String, amount: Float, userId: Int, callback: (Result<Int>) -> Unit) {
        Thread {
            try {
                val url = URL("$baseUrl/expense-categories")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val postData = """
                {
                    "category_name": "$categoryName",
                    "amount": $amount,
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
                    val categoryId = JSONObject(response).getInt("id")  // Assumes 'id' is the key for the category ID in the response JSON
                    uiUpdater.runOnUIThread {
                        callback(Result.success(categoryId))
                    }
                } else {
                    uiUpdater.runOnUIThread {
                        callback(Result.failure(RuntimeException("Failed to create category: $responseCode")))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    callback(Result.failure(RuntimeException("Failed to create category: ${e.localizedMessage}")))
                }
            }
        }.start()
    }


    fun updateExpenseCategory(categoryId: Int, newName: String?, newAmount: Float?) {
        Thread {
            try {
                val url = URL("$baseUrl/expense-categories?category_id=$categoryId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val updatedData = """
                    {
                        "category_name": "${newName ?: ""}",
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
                        Toast.makeText(context, "Category updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update category: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to update category: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun deleteExpenseCategory(categoryId: Int) {
        Thread {
            try {
                val url = URL("$baseUrl/expense-categories?category_id=$categoryId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"

                val responseCode = connection.responseCode
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(context, "Category deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete category: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiUpdater.runOnUIThread {
                    val context = uiUpdater.getContext()
                    Toast.makeText(context, "Failed to delete category: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}

