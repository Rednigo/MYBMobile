import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myb.MainActivity
import com.example.myb.R
import com.example.myb.model.Savings // Replace with your actual Savings model import

class SavingsAdapter(private var savingsList: List<Savings>, private val activity: MainActivity) : RecyclerView.Adapter<SavingsAdapter.SavingsViewHolder>() {

    class SavingsViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        val savingsNameTextView: TextView = view.findViewById(R.id.textViewSavingsName)
        val amountTextView: TextView = view.findViewById(R.id.textViewAmount)
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
        holder.amountTextView.text = activity.getString(R.string.amount_format, savings.Amount)
        holder.editButton.setOnClickListener {
            activity.showSavingsDialog(savings)
        }
        holder.deleteButton.setOnClickListener {
            activity.savingsNetworkManager.deleteSavings(savings.id)
        }
    }

    override fun getItemCount(): Int = savingsList.size

    fun updateData(newSavings: List<Savings>) {
        savingsList = newSavings
        notifyDataSetChanged()
    }
}
