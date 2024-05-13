import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.MainActivity
import com.example.myb.R
import com.example.myb.model.Savings // Make sure this model includes a date property

class SavingsAdapter(private var savingsList: MutableList<Savings>, private val activity: MainActivity) : RecyclerView.Adapter<SavingsAdapter.SavingsViewHolder>() {

    class SavingsViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        val savingsNameTextView: TextView = view.findViewById(R.id.textViewSavingsName)
        val amountTextView: TextView = view.findViewById(R.id.textViewAmount)
        val dateTextView: TextView = view.findViewById(R.id.textViewDate) // TextView for displaying the date
        val editButton: ImageButton = view.findViewById(R.id.buttonEditSavings)
        val deleteButton: ImageButton = view.findViewById(R.id.buttonDeleteSavings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_savings, parent, false) as ViewGroup
        return SavingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavingsViewHolder, position: Int) {
        val savings = savingsList[position]
        holder.savingsNameTextView.text = savings.SavingsName
        holder.amountTextView.text = String.format("$%.2f", savings.Amount)
        val dateParts = savings.Date.split("T")
        holder.dateTextView.text = dateParts[0] // This will display only the date part "YYYY-MM-DD"

        holder.editButton.setOnClickListener {
            // Assuming the dialog or some method now accepts a date to handle updates
            activity.showSavingsDialog(savings)
        }

        holder.deleteButton.setOnClickListener {
            val position = holder.adapterPosition
            removeSavings(position)
            activity.savingsNetworkManager.deleteSavings(savings.id)
            Log.d("Income id", savings.id.toString())
        }
    }

    override fun getItemCount(): Int = savingsList.size

    fun updateData(newSavings: List<Savings>) {
        savingsList.clear()
        savingsList.addAll(newSavings)
        notifyDataSetChanged()
    }

    fun addSavings(savings: Savings) {
        savingsList.add(savings)
        notifyItemInserted(savingsList.size - 1)
    }

    fun updateSavings(savings: Savings) {
        val index = savingsList.indexOfFirst { it.id == savings.id }
        if (index != -1) {
            savingsList[index] = savings
            notifyItemChanged(index)
        }
    }

    fun removeSavings(position: Int) {
        savingsList.removeAt(position)
        notifyItemRemoved(position)
    }
}
