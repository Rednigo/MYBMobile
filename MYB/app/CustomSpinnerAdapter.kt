import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
package com.example.myb

class CustomSpinnerAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        // Налаштовуємо обробник кліків для кожного елемента
        view.setOnClickListener {
            when (position) {
                0 -> {
                    // Створюємо інтент для переходу на StatisticsActivity
                    val intent = Intent(context, StatisticsAcivity::class.java)
                    context.startActivity(intent)
                }
                1 -> {
                    // Створюємо інтент для переходу на MainActivity
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    // Створюємо інтент для переходу на SettingsActivity
                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

        return view
    }
}
