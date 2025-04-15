import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uca.tflitetest.Model.HistoryData
import com.uca.tflitetest.R
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private val onItemLongClick: (String) -> Unit // Pass the document ID
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyList = mutableListOf<HistoryData>()
    private val dateFormat = SimpleDateFormat("E, dd-MM-yy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val data = historyList[position]
        holder.bind(data)
        holder.itemView.setOnLongClickListener {
            onItemLongClick(data.documentId) // Pass the document ID
            true
        }
    }

    override fun getItemCount(): Int = historyList.size

    fun setData(data: List<HistoryData>) {
        historyList.clear()
        historyList.addAll(data)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val classTextView: TextView = itemView.findViewById(R.id.textViewClassValue)
        private val confidenceTextView: TextView = itemView.findViewById(R.id.textViewConfidenceValue)
        private val timestampTextView: TextView = itemView.findViewById(R.id.textViewTimestamp)
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewCancer)

        fun bind(data: HistoryData) {
            classTextView.text = data.classValue
            confidenceTextView.text = String.format("%.1f%%", data.confidenceValue)
            timestampTextView.text = data.timestamp?.toDate()?.let { dateFormat.format(it) }
//            Glide.with(itemView.context).load(data.imageUrl).into(imageView)
            Glide.with(itemView.context)
                .load(data.imageUrl)
                .circleCrop()  // Apply circular cropping
                .placeholder(R.drawable.gallery_minimalistic) // Set your placeholder image here
                .error(R.drawable.gallery_minimalistic)
                .into(imageView)
        }
    }
}
