package gauravdahale.gtech.akoladirectory.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.adapter.TransactionAdapter
import gauravdahale.gtech.akoladirectory.data.PricingRepository
import gauravdahale.gtech.akoladirectory.models.Transaction
import kotlinx.coroutines.launch

/**
 * Activity for displaying user's transaction history
 */
class TransactionHistoryActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var transactionAdapter: TransactionAdapter
    private val pricingRepository = PricingRepository.getInstance()
    
    companion object {
        private const val TAG = "TransactionHistoryActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        
        setupUI()
        loadTransactions()
    }
    
    private fun setupUI() {
        // Setup toolbar
        supportActionBar?.apply {
            title = "My Purchases"
            setDisplayHomeAsUpEnabled(true)
        }
        
        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewTransactions)
        progressIndicator = findViewById(R.id.progressIndicator)
        
        // Setup RecyclerView
        transactionAdapter = TransactionAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TransactionHistoryActivity)
            adapter = transactionAdapter
        }
    }
    
    private fun loadTransactions() {
        showLoading(true)
        
        lifecycleScope.launch {
            try {
                val userId = getCurrentUserId()
                val transactions = pricingRepository.getUserTransactions(userId)
                
                runOnUiThread {
                    showLoading(false)
                    if (transactions.isNotEmpty()) {
                        transactionAdapter.updateTransactions(transactions)
                    } else {
                        showEmptyState()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading transactions", e)
                runOnUiThread {
                    showLoading(false)
                    showError("Failed to load transaction history. Please try again.")
                }
            }
        }
    }
    
    private fun showEmptyState() {
        // Create a simple message for empty state
        Toast.makeText(this, "No purchases found. Start by buying an advertisement package!", Toast.LENGTH_LONG).show()
    }
    
    private fun showLoading(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun getCurrentUserId(): String {
        // In a real app, get this from your authentication system
        // For demo purposes, return a mock user ID
        return "user_" + System.currentTimeMillis().toString().takeLast(8)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}