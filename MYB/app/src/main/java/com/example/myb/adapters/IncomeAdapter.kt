import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.Income // Replace with your actual Income model import
import com.example.myb.MainActivity
import com.example.myb.R

class IncomeAdapter(private var incomes: List<Income>, private val activity: MainActivity) : RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {

    class IncomeViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        val incomeNameTextView: TextView = view.findViewById(R.id.income_name_text_view)
        val editButton: ImageButton = view.findViewById(R.id.edit_income_button)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_income_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_income, parent, false) as ViewGroup
        return IncomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val income = incomes[position]
        holder.incomeNameTextView.text = income.IncomeName
        holder.editButton.setOnClickListener {
            activity.showIncomeDialog(income)
        }
        holder.deleteButton.setOnClickListener {
            activity.incomeNetworkManager.deleteIncome(income.id)
        }
    }

    override fun getItemCount(): Int = incomes.size

    fun updateData(newIncomes: List<Income>) {
        incomes = newIncomes
        notifyDataSetChanged()
    }
}
