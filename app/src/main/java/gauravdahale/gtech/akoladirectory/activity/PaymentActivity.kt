package gauravdahale.gtech.akoladirectory.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.stripe.android.model.PaymentMethodCreateParams
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.data.PaymentService
import gauravdahale.gtech.akoladirectory.data.PricingRepository
import gauravdahale.gtech.akoladirectory.models.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * Activity for handling payment processing
 */
class PaymentActivity : AppCompatActivity() {
    
    private lateinit var tvPackageName: TextView
    private lateinit var tvPackagePrice: TextView
    private lateinit var tvPackageDescription: TextView
    private lateinit var etCardNumber: EditText
    private lateinit var etExpiryDate: EditText
    private lateinit var etCvv: EditText
    private lateinit var etCardHolderName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnPayNow: Button
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var paymentFormLayout: LinearLayout
    
    private lateinit var paymentService: PaymentService
    private val pricingRepository = PricingRepository.getInstance()
    
    private var packageId: String = ""
    private var packageName: String = ""
    private var packagePrice: Double = 0.0
    private var packageCurrency: String = "INR"
    private var packageDescription: String = ""
    
    companion object {
        private const val TAG = "PaymentActivity"
        private const val REQUEST_CODE_PAYMENT = 1001
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        
        paymentService = PaymentService(this)
        
        getIntentData()
        setupUI()
        setupPaymentForm()
    }
    
    private fun getIntentData() {
        intent?.let {
            packageId = it.getStringExtra("package_id") ?: ""
            packageName = it.getStringExtra("package_name") ?: ""
            packagePrice = it.getDoubleExtra("package_price", 0.0)
            packageCurrency = it.getStringExtra("package_currency") ?: "INR"
            packageDescription = it.getStringExtra("package_description") ?: ""
        }
    }
    
    private fun setupUI() {
        // Setup toolbar
        supportActionBar?.apply {
            title = "Payment"
            setDisplayHomeAsUpEnabled(true)
        }
        
        // Initialize views
        tvPackageName = findViewById(R.id.tvPackageName)
        tvPackagePrice = findViewById(R.id.tvPackagePrice)
        tvPackageDescription = findViewById(R.id.tvPackageDescription)
        etCardNumber = findViewById(R.id.etCardNumber)
        etExpiryDate = findViewById(R.id.etExpiryDate)
        etCvv = findViewById(R.id.etCvv)
        etCardHolderName = findViewById(R.id.etCardHolderName)
        etEmail = findViewById(R.id.etEmail)
        btnPayNow = findViewById(R.id.btnPayNow)
        progressIndicator = findViewById(R.id.progressIndicator)
        paymentFormLayout = findViewById(R.id.paymentFormLayout)
        
        // Set package details
        tvPackageName.text = packageName
        tvPackagePrice.text = paymentService.formatAmount(packagePrice, packageCurrency)
        tvPackageDescription.text = packageDescription
        
        btnPayNow.setOnClickListener {
            processPayment()
        }
    }
    
    private fun setupPaymentForm() {
        // Add input formatting for card fields
        etCardNumber.setOnTextChangedListener { text ->
            // Format card number with spaces (xxxx xxxx xxxx xxxx)
            val formatted = text.toString().replace("\\s".toRegex(), "")
                .chunked(4).joinToString(" ").take(19)
            if (formatted != text.toString()) {
                etCardNumber.setText(formatted)
                etCardNumber.setSelection(formatted.length)
            }
        }
        
        etExpiryDate.setOnTextChangedListener { text ->
            // Format expiry date (MM/YY)
            val formatted = text.toString().replace("/", "")
            when {
                formatted.length >= 2 -> {
                    val month = formatted.substring(0, 2)
                    val year = if (formatted.length > 2) "/${formatted.substring(2, minOf(4, formatted.length))}" else ""
                    val result = "$month$year"
                    if (result != text.toString()) {
                        etExpiryDate.setText(result)
                        etExpiryDate.setSelection(result.length)
                    }
                }
            }
        }
    }
    
    private fun processPayment() {
        if (!validatePaymentForm()) {
            return
        }
        
        showLoading(true)
        
        lifecycleScope.launch {
            try {
                // Create payment details
                val paymentDetails = PaymentDetails(
                    amount = packagePrice,
                    currency = packageCurrency,
                    packageId = packageId,
                    userId = getCurrentUserId(),
                    description = "Payment for $packageName",
                    receiptEmail = etEmail.text.toString().trim()
                )
                
                // Create transaction record
                val transaction = Transaction(
                    userId = getCurrentUserId(),
                    packageId = packageId,
                    packageName = packageName,
                    amount = packagePrice,
                    currency = packageCurrency,
                    status = TransactionStatus.PENDING,
                    description = "Payment for $packageName",
                    createdAt = Date(),
                    expiresAt = Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000)) // 30 days
                )
                
                val transactionId = pricingRepository.createTransaction(transaction)
                
                // Create payment intent
                val clientSecret = paymentService.createPaymentIntent(paymentDetails)
                
                if (clientSecret != null) {
                    // For demonstration purposes, simulate successful payment
                    // In a real implementation, you would use Stripe's payment flow
                    simulatePaymentSuccess(transactionId)
                } else {
                    runOnUiThread {
                        showLoading(false)
                        showError("Failed to initialize payment. Please try again.")
                    }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error processing payment", e)
                runOnUiThread {
                    showLoading(false)
                    showError("Payment failed. Please try again.")
                }
            }
        }
    }
    
    private fun simulatePaymentSuccess(transactionId: String) {
        lifecycleScope.launch {
            try {
                // Update transaction status to completed
                pricingRepository.updateTransactionStatus(transactionId, TransactionStatus.COMPLETED)
                
                runOnUiThread {
                    showLoading(false)
                    showPaymentSuccess(transactionId)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating transaction", e)
                runOnUiThread {
                    showLoading(false)
                    showError("Payment completed but failed to update records.")
                }
            }
        }
    }
    
    private fun validatePaymentForm(): Boolean {
        val cardNumber = etCardNumber.text.toString().replace("\\s".toRegex(), "")
        val expiryDate = etExpiryDate.text.toString()
        val cvv = etCvv.text.toString()
        val cardHolderName = etCardHolderName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        
        when {
            cardNumber.length < 13 -> {
                showError("Please enter a valid card number")
                return false
            }
            expiryDate.length != 5 || !expiryDate.contains("/") -> {
                showError("Please enter a valid expiry date (MM/YY)")
                return false
            }
            cvv.length < 3 -> {
                showError("Please enter a valid CVV")
                return false
            }
            cardHolderName.isEmpty() -> {
                showError("Please enter card holder name")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError("Please enter a valid email address")
                return false
            }
        }
        
        return true
    }
    
    private fun showPaymentSuccess(transactionId: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Payment Successful!")
            .setMessage("Your payment has been processed successfully. Transaction ID: $transactionId")
            .setPositiveButton("View My Packages") { _, _ ->
                // Navigate to user's purchased packages
                val intent = Intent(this, TransactionHistoryActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Close") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun showLoading(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        paymentFormLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        btnPayNow.isEnabled = !isLoading
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

// Extension function for text change listener
fun EditText.setOnTextChangedListener(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : android.text.TextWatcher {
        override fun afterTextChanged(s: android.text.Editable?) {
            onTextChanged(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}