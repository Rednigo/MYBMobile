package com.example.myb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myb.utils.ApiConfig
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    private val baseUrl = ApiConfig.BASE_URL
    private val PREF_FILE = ApiConfig.PREF_FILE

    override fun onCreate(savedInstanceState: Bundle?) {

        //val sharedPreferenceManager = SharedPreferenceManager(this)
        //AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[sharedPreferenceManager.theme])

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                performLogin(username, password)
            } else {
                Toast.makeText(this, "Username and password cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(username: String, password: String) {
        Thread {
            try {
                val url = URL("$baseUrl/users/login")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.doOutput = true
                httpURLConnection.setRequestProperty("Content-Type", "application/json")

                val postData = "{\"username\":\"$username\",\"password\":\"$password\"}"

                OutputStreamWriter(httpURLConnection.outputStream).use { writer ->
                    writer.write(postData)
                    writer.flush()
                }

                val responseCode = httpURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = httpURLConnection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    val userId = jsonResponse.getInt("id")

                    val sharedPrefs = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
                    sharedPrefs.edit().apply {
                        putInt("USER_ID", userId)
                        apply()
                    }

                    val mainPageIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainPageIntent)
                    finish()
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Login failed with response code $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Login failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
