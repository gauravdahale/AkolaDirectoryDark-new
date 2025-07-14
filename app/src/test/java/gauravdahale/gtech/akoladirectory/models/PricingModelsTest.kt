package gauravdahale.gtech.akoladirectory.models

import org.junit.Test
import org.junit.Assert.*
import java.util.*

/**
 * Unit tests for pricing system models
 */
class PricingModelsTest {
    
    @Test
    fun testPricingPackageCreation() {
        val packageFeatures = listOf("Featured Listing", "Multiple Images")
        val pricingPackage = PricingPackage(
            id = "test_package_1",
            name = "Test Basic Package",
            description = "A test package for unit testing",
            price = 299.0,
            currency = "INR",
            durationDays = 30,
            features = packageFeatures,
            maxAds = 1,
            isPremium = false,
            categoryType = PackageCategory.BASIC
        )
        
        assertEquals("test_package_1", pricingPackage.id)
        assertEquals("Test Basic Package", pricingPackage.name)
        assertEquals(299.0, pricingPackage.price, 0.01)
        assertEquals("INR", pricingPackage.currency)
        assertEquals(30, pricingPackage.durationDays)
        assertEquals(2, pricingPackage.features.size)
        assertEquals(1, pricingPackage.maxAds)
        assertFalse(pricingPackage.isPremium)
        assertEquals(PackageCategory.BASIC, pricingPackage.categoryType)
    }
    
    @Test
    fun testTransactionCreation() {
        val transaction = Transaction(
            id = "txn_123",
            userId = "user_123",
            packageId = "pkg_123",
            packageName = "Test Package",
            amount = 599.0,
            currency = "INR",
            status = TransactionStatus.COMPLETED,
            paymentMethod = PaymentMethod.CARD,
            description = "Test transaction"
        )
        
        assertEquals("txn_123", transaction.id)
        assertEquals("user_123", transaction.userId)
        assertEquals("pkg_123", transaction.packageId)
        assertEquals("Test Package", transaction.packageName)
        assertEquals(599.0, transaction.amount, 0.01)
        assertEquals("INR", transaction.currency)
        assertEquals(TransactionStatus.COMPLETED, transaction.status)
        assertEquals(PaymentMethod.CARD, transaction.paymentMethod)
        assertEquals("Test transaction", transaction.description)
    }
    
    @Test
    fun testPaymentDetailsCreation() {
        val paymentDetails = PaymentDetails(
            amount = 999.0,
            currency = "INR",
            packageId = "pkg_business",
            userId = "user_456",
            description = "Business package payment",
            receiptEmail = "test@example.com"
        )
        
        assertEquals(999.0, paymentDetails.amount, 0.01)
        assertEquals("INR", paymentDetails.currency)
        assertEquals("pkg_business", paymentDetails.packageId)
        assertEquals("user_456", paymentDetails.userId)
        assertEquals("Business package payment", paymentDetails.description)
        assertEquals("test@example.com", paymentDetails.receiptEmail)
    }
    
    @Test
    fun testPackageCategoryEnum() {
        assertEquals(4, PackageCategory.values().size)
        assertTrue(PackageCategory.values().contains(PackageCategory.BASIC))
        assertTrue(PackageCategory.values().contains(PackageCategory.PREMIUM))
        assertTrue(PackageCategory.values().contains(PackageCategory.BUSINESS))
        assertTrue(PackageCategory.values().contains(PackageCategory.ENTERPRISE))
    }
    
    @Test
    fun testTransactionStatusEnum() {
        assertEquals(7, TransactionStatus.values().size)
        assertTrue(TransactionStatus.values().contains(TransactionStatus.PENDING))
        assertTrue(TransactionStatus.values().contains(TransactionStatus.COMPLETED))
        assertTrue(TransactionStatus.values().contains(TransactionStatus.FAILED))
        assertTrue(TransactionStatus.values().contains(TransactionStatus.REFUNDED))
    }
    
    @Test
    fun testPackageFeatureEnum() {
        assertEquals(8, PackageFeature.values().size)
        assertEquals("Featured Listing", PackageFeature.FEATURED_LISTING.displayName)
        assertEquals("Social Media Promotion", PackageFeature.SOCIAL_MEDIA_PROMOTION.displayName)
        assertEquals("Priority Support", PackageFeature.PRIORITY_SUPPORT.displayName)
        assertEquals("Analytics Dashboard", PackageFeature.ANALYTICS_DASHBOARD.displayName)
    }
}