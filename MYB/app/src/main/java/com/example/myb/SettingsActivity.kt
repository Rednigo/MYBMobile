package com.example.myb

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import com.example.myb.utils.ApiConfig
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_FILE = ApiConfig.PREF_FILE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

        if (getUserLanguage() == "uk") {
            setContentView(R.layout.activity_settings_uk)
        }
        else {
            setContentView(R.layout.activity_settings)
        }

        val backButton = findViewById<LinearLayout>(R.id.backButtonLayout)
        setupLanguageAndThemeSpinners()

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val spinner = findViewById<Spinner>(R.id.dropdownSpinner)
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item_header)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter.add("Statistics")
        adapter.add("Home")
        adapter.add("Settings")

        spinner.adapter = adapter
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

    private fun setupLanguageAndThemeSpinners() {
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)
        val themeSpinner = findViewById<Spinner>(R.id.themeSpinner)

        // Set up the language spinner
        val languageAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item)
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = languageAdapter
        languageSpinner.setSelection(getLanguagePosition())

        // Set up the theme spinner
        val themeAdapter = ArrayAdapter.createFromResource(this, R.array.theme_values, android.R.layout.simple_spinner_item)
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.adapter = themeAdapter
        themeSpinner.setSelection(if (sharedPreferences.getBoolean("IS_LIGHT_THEME", true)) 0 else 1)

        // Button to apply changes
        findViewById<Button>(R.id.applySettingsButton).setOnClickListener {
            applySettings()
        }
    }

    private fun applySettings() {
        val selectedLanguage = findViewById<Spinner>(R.id.languageSpinner).selectedItem.toString()
        val isLightTheme = findViewById<Spinner>(R.id.themeSpinner).selectedItemPosition == 1

        val userId = getUserId() // Or however you obtain the user's ID
        val settingsJson = JSONObject()
        settingsJson.put("id", userId)

        val newLang = if (selectedLanguage == "Ukrainian") "uk" else "en"
        settingsJson.put("language", newLang)

        // Update shared preferences safely
        sharedPreferences.edit().apply {
            putString("LANG", newLang)
            apply() // Consider using commit() if apply() doesn't give expected results
        }

        settingsJson.put("is_light_theme", isLightTheme)

        // Send settings to the server
        sendSettingsToServer(settingsJson)
    }

    private fun sendSettingsToServer(settingsJson: JSONObject) {
        Thread {
            try {
                val url = URL("http://192.168.0.163:8080/api/v1/users/settings")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "PUT"
                httpURLConnection.doOutput = true
                httpURLConnection.setRequestProperty("Content-Type", "application/json")
                httpURLConnection.outputStream.write(settingsJson.toString().toByteArray())

                val responseCode = httpURLConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread {
                        Toast.makeText(this, "Settings updated successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to update settings", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun getLanguagePosition(): Int {
        val languages = resources.getStringArray(R.array.languages)
        val currentLanguage = sharedPreferences.getString("LANG", "en")
        return languages.indexOf(currentLanguage ?: "en")
    }


    private fun getUserId(): Int {
        return sharedPreferences.getInt("USER_ID", -1) // -1 as default if not found
    }

    private fun getUserLanguage(): String? {
        return sharedPreferences.getString("LANG", "en") // -1 as default if not found
    }
}