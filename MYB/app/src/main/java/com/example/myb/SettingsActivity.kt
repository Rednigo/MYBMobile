package com.example.myb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<LinearLayout>(R.id.backButtonLayout)

        backButton.setOnClickListener {
            // Створюємо інтент для переходу на MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Запускаємо MainActivity
            startActivity(intent)
            // Завершуємо поточну активність
            finish()
        }

        val spinner = findViewById<Spinner>(R.id.dropdownSpinner)

// Створіть пустий адаптер для Spinner
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item_header)

// Встановіть макет для випадаючого списку
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Додайте текст-підказку до адаптера (це перший елемент у списку)


        adapter.add("Settings")
        adapter.add("Statistics")
        adapter.add("Home")



// Встановіть адаптер для Spinner
        spinner.adapter = adapter

        // Встановіть підказку
        spinner.prompt = "Choose an option"

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {

                    1 -> {
                        // Створюємо інтент для переходу на SettingsActivity
                        val intent = Intent(this@SettingsActivity, StatisticsActivity::class.java)
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }

                    2 -> {
                        // Створюємо інтент для переходу на StatisticsActivity
                        val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                        // Запускаємо StatisticsActivity
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }
                    // Додайте інші варіанти, якщо потрібно
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Нічого не робимо, якщо нічого не вибрано
            }
        }


    }
}