package com.example.myb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        // Отримуємо рідну ViewGroup, в яку будемо додавати рядки
        val tableLayout = findViewById<LinearLayout>(R.id.statistics_table_id)

        // Перелік даних для кожного рядка (наприклад, замість цього можна використовувати дані з бази даних)
        val months = arrayOf("January", "February", "March", "April")
        val incomes = intArrayOf(1000, 1500, 1200, 2000)
        val expenses = intArrayOf(500, 600, 800, 1000)
        val saved = intArrayOf(500, 900, 400, 1000)

        // Проходимо по кожному елементу масиву даних і додаємо їх у таблицю
        for (i in months.indices) {
            // Створюємо новий рядок, використовуючи макет row_item.xml
            val rowView = layoutInflater.inflate(R.layout.row_item, null)

            // Знаходимо TextView для місяця у цьому рядку
            val monthTextView = rowView.findViewById<TextView>(R.id.monthTextView)
            // Встановлюємо текст місяця
            monthTextView.text = months[i]

            // Знаходимо інші TextView для доходів, витрат і заощаджень у цьому рядку
            val incomeTextView = rowView.findViewById<TextView>(R.id.incomeTextView)
            val expenseTextView = rowView.findViewById<TextView>(R.id.expenseTextView)
            val savedTextView = rowView.findViewById<TextView>(R.id.savedTextView)

            // Встановлюємо дані для доходів, витрат і заощаджень
            incomeTextView.text = incomes[i].toString()
            expenseTextView.text = expenses[i].toString()
            savedTextView.text = saved[i].toString()

            // Додаємо рядок у загальну таблицю
            tableLayout.addView(rowView)
        }

        // Додаємо лінію в кінці таблиці
        val lineView = View(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5) // висота лінії
        params.setMargins(0, 25, 0, 0) // встановлюємо відступи вгорі
        lineView.layoutParams = params
        lineView.setBackgroundColor(ContextCompat.getColor(this, R.color.table_color)) // колір лінії
        tableLayout.addView(lineView)

        val backButton = findViewById<LinearLayout>(R.id.backButtonLayoutStat)

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

        adapter.add("")
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
                        val intent = Intent(this@StatisticsActivity, StatisticsActivity::class.java)
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }
                    2 -> {
                        // Створюємо інтент для переходу на SettingsActivity
                        val intent = Intent(this@StatisticsActivity, MainActivity::class.java)
                        startActivity(intent)
                        // Завершуємо поточну активність
                        finish()
                    }

                    3 -> {
                        // Створюємо інтент для переходу на StatisticsActivity
                        val intent = Intent(this@StatisticsActivity, SettingsActivity::class.java)
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