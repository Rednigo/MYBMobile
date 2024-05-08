package com.example.myb
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SharedPreferenceManager (context:Context){
        private val prefernce = context.getSharedPreferences(
            context.packageName,
            MODE_PRIVATE
        )

    private val editor = prefernce.edit()

    private  val  keyTheme = "theme"

    var theme
        get() = prefernce.getInt(keyTheme, 3)
        set(value){
            editor.putInt(keyTheme, value)
            editor.commit()
        }

    var themeFlag = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )


}