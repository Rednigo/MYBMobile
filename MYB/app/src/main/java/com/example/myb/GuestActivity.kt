package com.example.myb

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Make window background white / black
        window.decorView.apply {
            setBackgroundColor(Color.WHITE)
        }

        setContentView(R.layout.guest_layout)
        setContentView(R.layout.guest_layout)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.guest)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spannable = SpannableString(getString(R.string.manage_your_budget))
        val blackSpan = ForegroundColorSpan(Color.BLACK)
        val startIndexOfYour = spannable.indexOf("Your")
        if (startIndexOfYour != -1) {
            spannable.setSpan(blackSpan, startIndexOfYour, startIndexOfYour + "Your".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val textView = findViewById<TextView>(R.id.ManageYourBudget)
        textView.text = spannable


        // Find the buttons by their IDs
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        // Set the click listener for the login button
        btnLogin.setOnClickListener {
            // Start LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        // Set the click listener for the sign up button
        btnSignUp.setOnClickListener {
            val signUpIntent = Intent(this, SignupActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}