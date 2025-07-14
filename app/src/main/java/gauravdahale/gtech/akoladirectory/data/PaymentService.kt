package gauravdahale.gtech.akoladirectory.data

import android.content.Context
import android.util.Log
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentIntent
import com.stripe.android.model.PaymentMethodCreateParams
import gauravdahale.gtech.akoladirectory.BuildConfig
import gauravdahale.gtech.akoladirectory.models.PaymentDetails
import gauravdahale.gtech.akoladirectory.models.Transaction
import gauravdahale.gtech.akoladirectory.models.TransactionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Service class for handling payment processing with Stripe
 */
class PaymentService(private val context: Context) {
    
    private val stripe: Stripe by lazy {
        Stripe(context, getPublishableKey())
    }
    
    companion object {
        private const val TAG = "PaymentService"
        private const val BACKEND_URL = "https://your-backend-url.com" // Replace with actual backend URL
        
        // Test keys - replace with your actual keys
        private const val TEST_PUBLISHABLE_KEY = "pk_test_51234567890abcdefghijklmnopqrstuvwxyz"
        private const val TEST_SECRET_KEY = "sk_test_51234567890abcdefghijklmnopqrstuvwxyz"
    }
    
    init {
        PaymentConfiguration.init(context, getPublishableKey())
    }
    
    /**
     * Create payment intent on your backend
     */
    suspend fun createPaymentIntent(paymentDetails: PaymentDetails): String? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BACKEND_URL/create-payment-intent")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            
            val requestBody = JSONObject().apply {
                put("amount", (paymentDetails.amount * 100).toLong()) // Convert to paise
                put("currency", paymentDetails.currency.lowercase())
                put("package_id", paymentDetails.packageId)
                put("user_id", paymentDetails.userId)
                put("description", paymentDetails.description)
                put("receipt_email", paymentDetails.receiptEmail)
            }
            
            connection.outputStream.use { output ->
                output.write(requestBody.toString().toByteArray())
            }
            
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                jsonResponse.getString("client_secret")
            } else {
                Log.e(TAG, "Failed to create payment intent: ${connection.responseCode}")
                null
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error creating payment intent", e)
            // For demo purposes, return a mock client secret
            createMockPaymentIntent(paymentDetails)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating payment intent", e)
            null
        }
    }
    
    /**
     * Confirm payment with Stripe
     */
    suspend fun confirmPayment(
        clientSecret: String,
        paymentMethodCreateParams: PaymentMethodCreateParams
    ): PaymentIntent? = withContext(Dispatchers.Main) {
        try {
            val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                paymentMethodCreateParams,
                clientSecret
            )
            
            val result = stripe.confirmPayment(context as androidx.fragment.app.FragmentActivity, confirmParams)
            result.intent
        } catch (e: Exception) {
            Log.e(TAG, "Error confirming payment", e)
            null
        }
    }
    
    /**
     * Validate payment intent status
     */
    fun getTransactionStatusFromPaymentIntent(paymentIntent: PaymentIntent?): TransactionStatus {
        return when (paymentIntent?.status) {
            PaymentIntent.Status.Succeeded -> TransactionStatus.COMPLETED
            PaymentIntent.Status.Processing -> TransactionStatus.PROCESSING
            PaymentIntent.Status.RequiresPaymentMethod -> TransactionStatus.FAILED
            PaymentIntent.Status.RequiresConfirmation -> TransactionStatus.PENDING
            PaymentIntent.Status.RequiresAction -> TransactionStatus.PENDING
            PaymentIntent.Status.Canceled -> TransactionStatus.CANCELLED
            else -> TransactionStatus.FAILED
        }
    }
    
    /**
     * Get publishable key based on build variant
     */
    private fun getPublishableKey(): String {
        return if (BuildConfig.DEBUG) {
            TEST_PUBLISHABLE_KEY
        } else {
            // Replace with your live publishable key
            TEST_PUBLISHABLE_KEY
        }
    }
    
    /**
     * Create a mock payment intent for testing when backend is not available
     */
    private fun createMockPaymentIntent(paymentDetails: PaymentDetails): String {
        // This is a mock client secret for testing purposes
        // In a real implementation, this would come from your backend
        return "pi_mock_${System.currentTimeMillis()}_secret_mock"
    }
    
    /**
     * Format amount for display
     */
    fun formatAmount(amount: Double, currency: String): String {
        return when (currency.uppercase()) {
            "INR" -> "₹${String.format("%.2f", amount)}"
            "USD" -> "$${String.format("%.2f", amount)}"
            else -> "${currency.uppercase()} ${String.format("%.2f", amount)}"
        }
    }
}