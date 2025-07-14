package gauravdahale.gtech.akoladirectory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.Transaction
import gauravdahale.gtech.akoladirectory.models.TransactionStatus
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying transaction history in RecyclerView
 */
class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    
    private var transactions = listOf<Transaction>()
    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    
    fun updateTransactions(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }
    
    override fun getItemCount(): Int = transactions.size
    
    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPackageName: TextView = itemView.findViewById(R.id.tvPackageName)
        private val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvExpiryDate: TextView = itemView.findViewById(R.id.tvExpiryDate)
        
        fun bind(transaction: Transaction) {
            tvPackageName.text = transaction.packageName
            tvTransactionId.text = "ID: ${transaction.id.takeLast(8)}"
            tvAmount.text = "${transaction.currency} ${String.format("%.2f", transaction.amount)}"
            tvDate.text = dateFormatter.format(transaction.createdAt)
            
            // Set status with appropriate color
            tvStatus.text = getStatusDisplayText(transaction.status)
            tvStatus.setTextColor(getStatusColor(transaction.status))
            
            // Show expiry date if transaction is completed
            if (transaction.status == TransactionStatus.COMPLETED) {
                tvExpiryDate.visibility = View.VISIBLE
                tvExpiryDate.text = "Expires: ${dateFormatter.format(transaction.expiresAt)}"
            } else {
                tvExpiryDate.visibility = View.GONE
            }
            
            // Set background based on status
            val backgroundRes = when (transaction.status) {
                TransactionStatus.COMPLETED -> R.drawable.transaction_item_success
                TransactionStatus.FAILED, TransactionStatus.CANCELLED -> R.drawable.transaction_item_error
                TransactionStatus.REFUNDED, TransactionStatus.PARTIALLY_REFUNDED -> R.drawable.transaction_item_refunded
                else -> R.drawable.transaction_item_pending
            }
            
            try {
                itemView.setBackgroundResource(backgroundRes)
            } catch (e: Exception) {
                // Fallback to default background
                itemView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
            }
        }
        
        private fun getStatusDisplayText(status: TransactionStatus): String {
            return when (status) {
                TransactionStatus.PENDING -> "Pending"
                TransactionStatus.PROCESSING -> "Processing"
                TransactionStatus.COMPLETED -> "Active"
                TransactionStatus.FAILED -> "Failed"
                TransactionStatus.CANCELLED -> "Cancelled"
                TransactionStatus.REFUNDED -> "Refunded"
                TransactionStatus.PARTIALLY_REFUNDED -> "Partially Refunded"
            }
        }
        
        private fun getStatusColor(status: TransactionStatus): Int {
            val context = itemView.context
            return when (status) {
                TransactionStatus.COMPLETED -> androidx.core.content.ContextCompat.getColor(context, R.color.green_600)
                TransactionStatus.FAILED, TransactionStatus.CANCELLED -> androidx.core.content.ContextCompat.getColor(context, R.color.red_600)
                TransactionStatus.REFUNDED, TransactionStatus.PARTIALLY_REFUNDED -> androidx.core.content.ContextCompat.getColor(context, R.color.orange_600)
                else -> androidx.core.content.ContextCompat.getColor(context, R.color.amber_600)
            }
        }
    }
}