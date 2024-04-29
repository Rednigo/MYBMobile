import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myb.R


class StatisticsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Задаємо макет фрагмента
        val rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Отримуємо рідну ViewGroup, в яку будемо додавати рядки
        val tableLayout = rootView.findViewById<LinearLayout>(R.id.statistics_table_id)

        // Перелік даних для кожного рядка (наприклад, замість цього можна використовувати дані з бази даних)
        val months = arrayOf("January", "February", "March", "April")
        val incomes = intArrayOf(1000, 1500, 1200, 2000)
        val expenses = intArrayOf(500, 600, 800, 1000)
        val saved = intArrayOf(500, 900, 400, 1000)

        // Проходимо по кожному елементу масиву даних і додаємо їх у таблицю
        for (i in months.indices) {
            // Створюємо новий рядок, використовуючи макет row_item.xml
            val rowView = inflater.inflate(R.layout.row_item, null)

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

        // Повертаємо кореневий макет фрагмента
        return rootView
    }
}
