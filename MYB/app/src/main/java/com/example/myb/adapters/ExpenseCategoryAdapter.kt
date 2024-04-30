import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.ExpenseCategory  // Ensure this matches your actual import for ExpenseCategory model
import com.example.myb.MainActivity
import com.example.myb.R

class ExpenseCategoryAdapter(private var categories: List<ExpenseCategory>, private val activity: MainActivity) : RecyclerView.Adapter<ExpenseCategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        val categoryNameTextView: TextView = view.findViewById(R.id.textViewCategoryName)
        val categoryBudgetTextView: TextView = view.findViewById(R.id.textViewCategoryBudget)
        val editButton: ImageButton = view.findViewById(R.id.buttonEditCategory)
        val deleteButton: ImageButton = view.findViewById(R.id.buttonDeleteCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_category, parent, false) as ViewGroup
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryNameTextView.text = category.CategoryName
        holder.categoryBudgetTextView.text = "Budget: $${category.Amount}"
        holder.editButton.setOnClickListener {
            activity.showCategoryDialog(category)
        }
        holder.deleteButton.setOnClickListener {
            activity.expenseCategoryNetworkManager.deleteExpenseCategory(category.id)
        }
    }

    override fun getItemCount(): Int = categories.size
}

