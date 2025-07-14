package gauravdahale.gtech.akoladirectory.models

import java.util.*

/**
 * Data model representing a transaction/purchase record
 */
data class Transaction(
    val id: String = "",
    val userId: String = "",
    val packageId: String = "",
    val packageName: String = "",
    val amount: Double = 0.0,
    val currency: String = "INR",
    val status: TransactionStatus = TransactionStatus.PENDING,
    val paymentMethod: PaymentMethod = PaymentMethod.UNKNOWN,
    val paymentIntentId: String = "", // Stripe payment intent ID
    val paymentMethodId: String = "", // Stripe payment method ID
    val receiptUrl: String = "",
    val description: String = "",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val expiresAt: Date = Date(),
    val refundedAmount: Double = 0.0,
    val refundReason: String = ""
)

enum class TransactionStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED
}

enum class PaymentMethod {
    UNKNOWN,
    CARD,
    UPI,
    NET_BANKING,
    WALLET
}

/**
 * Payment details for processing payments
 */
data class PaymentDetails(
    val amount: Double,
    val currency: String = "INR",
    val packageId: String,
    val userId: String,
    val description: String = "",
    val receiptEmail: String = ""
)

/**
 * Purchase summary for user display
 */
data class PurchaseSummary(
    val transaction: Transaction,
    val packageDetails: PricingPackage,
    val isActive: Boolean,
    val daysRemaining: Int
)