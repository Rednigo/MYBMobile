package com.example.myb

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Використайте змінений XML-файл з ресурсів для завантаження налаштувань
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}