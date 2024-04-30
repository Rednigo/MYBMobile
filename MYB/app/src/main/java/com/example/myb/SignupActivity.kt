package com.example.myb

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        val etUsername: EditText = findViewById(R.id.etNewUsername)
        val etPassword: EditText = findViewById(R.id.etNewPassword)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val btnSignup: Button = findViewById(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val email = etEmail.text.toString()

            if (username.isNotBlank() && password.isNotBlank() && email.isNotBlank()) {
                performSignup(email, username, password)
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSignup(email: String, username: String, password: String) {
        Thread {
            try {
                val url = URL("http://192.168.0.76:8080/api/v1/users/register")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.doOutput = true
                httpURLConnection.setRequestProperty("Content-Type", "application/json")

                val postData = """
                    {
                        "email": "$email",
                        "username": "$username",
                        "password": "$password"
                    }
                """.trimIndent()

                OutputStreamWriter(httpURLConnection.outputStream).use { writer ->
                    writer.write(postData)
                    writer.flush()
                }

                val responseCode = httpURLConnection.responseCode

                runOnUiThread {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Signup failed with response code $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Signup failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
