package com.example.myb

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
        // Since network on main thread is not allowed, starting a new thread
        Thread {
            try {
                // val url = URL("http://localhost:8080/login")
                val url = URL("http://192.168.0.76:8080/api/v1/users/login")
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
                val responseMessage = httpURLConnection.responseMessage

                runOnUiThread {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Handle success
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle error
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