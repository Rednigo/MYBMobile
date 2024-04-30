import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.Expense
import com.example.myb.R

class ExpenseAdapter(
    private var expenses: List<Expense>,
    private val listener: ExpenseItemListener  // Listener for handling actions
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    interface ExpenseItemListener {
        fun onEditExpense(expense: Expense)
        fun onDeleteExpense(expenseId: Int)
    }

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val expenseNameTextView: TextView = view.findViewById(R.id.textViewExpenseName)
        val expenseAmountTextView: TextView = view.findViewById(R.id.textViewExpenseAmount)
        val editExpenseButton: ImageButton = view.findViewById(R.id.buttonEditExpense)
        val deleteExpenseButton: ImageButton = view.findViewById(R.id.buttonDeleteExpense)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expenseNameTextView.text = expense.ExpenseName
        holder.expenseAmountTextView.text = String.format("$%.2f", expense.Amount)
        holder.editExpenseButton.setOnClickListener { listener.onEditExpense(expense) }
        holder.deleteExpenseButton.setOnClickListener { listener.onDeleteExpense(expense.id) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false) as ViewGroup
        return ExpenseViewHolder(view)
    }

    override fun getItemCount(): Int = expenses.size

    fun updateExpenses(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}
