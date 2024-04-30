
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.Expense
import com.example.myb.ExpenseCategory
import com.example.myb.MainActivity
import com.example.myb.R

class ExpenseCategoryAdapter(
    private var categories: List<ExpenseCategory>,
    private val activity: MainActivity,
    private val fetchExpensesForCategory: (Int) -> List<Expense>  // Lambda to dynamically fetch expenses for a given category ID
) : RecyclerView.Adapter<ExpenseCategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryNameTextView: TextView = view.findViewById(R.id.textViewCategoryName)
        val categoryBudgetTextView: TextView = view.findViewById(R.id.textViewCategoryBudget)
        val expensesRecyclerView: RecyclerView = view.findViewById(R.id.expensesRecyclerView)
        val editButton: ImageButton = view.findViewById(R.id.buttonEditCategory)
        val deleteButton: ImageButton = view.findViewById(R.id.buttonDeleteCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryNameTextView.text = category.CategoryName
        holder.categoryBudgetTextView.text = String.format("Budget: $%.2f", category.Amount)

        // Setting up the nested RecyclerView
        val expenses = fetchExpensesForCategory(category.id)
        val expenseAdapter = ExpenseAdapter(expenses, object : ExpenseAdapter.ExpenseItemListener {
            override fun onEditExpense(expense: Expense) {
                activity.showExpenseDialog(expense)  // Implement this method to handle expense edits
            }

            override fun onDeleteExpense(expenseId: Int) {
                activity.expenseNetworkManager.deleteExpense(expenseId)  // Implement this method to handle expense deletions
            }
        })
        holder.expensesRecyclerView.adapter = expenseAdapter
        holder.expensesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)

        // Edit and delete buttons for the category
        holder.editButton.setOnClickListener { activity.showCategoryDialog(category) }
        holder.deleteButton.setOnClickListener {
            activity.expenseCategoryNetworkManager.deleteExpenseCategory(category.id)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<ExpenseCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
