package com.example.myb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import com.example.myb.utils.ApiConfig


class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_FILE = ApiConfig.PREF_FILE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (getUserLanguage() == "uk") {
//            setContentView(R.layout.activity_settings_uk)
//        }
//        else {
//            setContentView(R.layout.activity_settings)
//        }
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

        adapter.add("Statistics")
        adapter.add("Home")
        adapter.add("Settings")



// Встановіть адаптер для Spinner
        spinner.adapter = adapter

        // Встановіть підказку
        spinner.prompt = "Choose an option"

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position) {

                    1 -> {
                        // Створюємо інтент для переходу на SettingsActivity
                        val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }

                    2 -> {
                        // Створюємо інтент для переходу на StatisticsActivity
                        val intent = Intent(this@SettingsActivity, StatisticsActivity::class.java)
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

    private fun getUserId(): Int {
        return sharedPreferences.getInt("USER_ID", -1) // -1 as default if not found
    }

    private fun getUserLanguage(): String? {
        return sharedPreferences.getString("LANG", "en") // -1 as default if not found
    }
}