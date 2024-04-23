package com.example.myb
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.AdapterView
import android.widget.ArrayAdapter

class CustomHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val headerTextView: TextView
    private val dropdownSpinner: Spinner

    init {
        // Надування макету custom_header.xml
        LayoutInflater.from(context).inflate(R.layout.custom_header, this, true)

        // Отримання посилань на TextView і Spinner з макету
        headerTextView = findViewById(R.id.headerTextView)
        dropdownSpinner = findViewById(R.id.dropdownSpinner)

        // Додаємо слухач вибору елемента до Spinner
        dropdownSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Отримуємо обраний елемент зі Spinner
                val selectedItem = parent?.getItemAtPosition(position).toString()

                // Оновлюємо текст заголовку з обраним значенням
                headerTextView.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обробка в разі, якщо нічого не вибрано
            }
        }
    }

    // Функція для встановлення варіантів і значень у Spinner
    fun setDropdownOptions(options: Array<String>, values: Array<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdownSpinner.adapter = adapter
    }
}
