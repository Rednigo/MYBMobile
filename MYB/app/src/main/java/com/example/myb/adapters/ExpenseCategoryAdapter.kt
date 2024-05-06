import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.Expense
import com.example.myb.ExpenseCategory
import com.example.myb.MainActivity
import com.example.myb.R

class ExpenseCategoryAdapter(
    private var categories: MutableList<ExpenseCategory>,
    private val activity: MainActivity,
    private val fetchExpensesForCategory: (Int, ExpenseAdapter) -> Unit
) : RecyclerView.Adapter<ExpenseCategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(view: View, val adapter: ExpenseAdapter) : RecyclerView.ViewHolder(view) {
        val categoryNameTextView: TextView = view.findViewById(R.id.textViewCategoryName)
        val categoryBudgetTextView: TextView = view.findViewById(R.id.textViewCategoryBudget)
        val expensesRecyclerView: RecyclerView = view.findViewById(R.id.expensesRecyclerView)
        val editButton: ImageButton = view.findViewById(R.id.buttonEditCategory)
        val deleteButton: ImageButton = view.findViewById(R.id.buttonDeleteCategory)
        val addButton: Button = view.findViewById(R.id.buttonAddExpense)

        init {
            expensesRecyclerView.adapter = adapter
            expensesRecyclerView.layoutManager = LinearLayoutManager(view.context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_category, parent, false)
        val adapter = ExpenseAdapter(mutableListOf(), object : ExpenseAdapter.ExpenseItemListener {
            override fun onEditExpense(expense: Expense) {
                activity.showExpenseDialog(expense)
            }

            override fun onDeleteExpense(expenseId: Int) {
                activity.expenseNetworkManager.deleteExpense(expenseId)
            }
        })
        return CategoryViewHolder(view, adapter)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryNameTextView.text = category.CategoryName
        holder.categoryBudgetTextView.text = String.format("Budget: $%.2f", category.Amount)

        fetchExpensesForCategory(category.id, holder.adapter)

        holder.editButton.setOnClickListener { activity.showCategoryDialog(category) }
        holder.deleteButton.setOnClickListener {
            val position = holder.adapterPosition
            categories.removeAt(position)
            notifyItemRemoved(position)
            activity.expenseCategoryNetworkManager.deleteExpenseCategory(category.id)
        }
        holder.addButton.setOnClickListener {
            activity.showExpenseDialog(null) // Pass `null` or a specific constructor if required
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<ExpenseCategory>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged()
    }


    fun addCategory(category: ExpenseCategory) {
        categories.add(category)
        notifyItemInserted(categories.size - 1)
    }

    fun updateCategory(category: ExpenseCategory, position: Int) {
        categories[position] = category
        notifyItemChanged(position)
    }

    fun removeCategory(position: Int) {
        categories.removeAt(position)
        notifyItemRemoved(position)
    }
}
