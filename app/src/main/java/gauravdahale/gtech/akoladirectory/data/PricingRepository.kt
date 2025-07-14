package gauravdahale.gtech.akoladirectory.data

import com.google.firebase.database.*
import gauravdahale.gtech.akoladirectory.models.*
import kotlinx.coroutines.tasks.await
import java.util.*

/**
 * Repository class for managing pricing packages and transactions
 */
class PricingRepository {
    
    private val database = FirebaseDatabase.getInstance()
    private val packagesRef = database.getReference("pricing_packages")
    private val transactionsRef = database.getReference("transactions")
    
    companion object {
        @Volatile
        private var INSTANCE: PricingRepository? = null
        
        fun getInstance(): PricingRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PricingRepository().also { INSTANCE = it }
            }
        }
    }
    
    /**
     * Get all active pricing packages
     */
    suspend fun getActivePackages(): List<PricingPackage> {
        return try {
            val snapshot = packagesRef.orderByChild("isActive").equalTo(true).get().await()
            val packages = mutableListOf<PricingPackage>()
            
            snapshot.children.forEach { child ->
                child.getValue(PricingPackage::class.java)?.let { packages.add(it) }
            }
            
            packages.sortedBy { it.price }
        } catch (e: Exception) {
            // Return default packages if Firebase is not available
            getDefaultPackages()
        }
    }
    
    /**
     * Get package by ID
     */
    suspend fun getPackageById(packageId: String): PricingPackage? {
        return try {
            val snapshot = packagesRef.child(packageId).get().await()
            snapshot.getValue(PricingPackage::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Create a new transaction record
     */
    suspend fun createTransaction(transaction: Transaction): String {
        return try {
            val transactionId = transactionsRef.push().key ?: UUID.randomUUID().toString()
            val transactionWithId = transaction.copy(id = transactionId)
            transactionsRef.child(transactionId).setValue(transactionWithId).await()
            transactionId
        } catch (e: Exception) {
            throw e
        }
    }
    
    /**
     * Update transaction status
     */
    suspend fun updateTransactionStatus(transactionId: String, status: TransactionStatus) {
        try {
            val updates = mapOf(
                "status" to status.name,
                "updatedAt" to Date()
            )
            transactionsRef.child(transactionId).updateChildren(updates).await()
        } catch (e: Exception) {
            throw e
        }
    }
    
    /**
     * Get user's transaction history
     */
    suspend fun getUserTransactions(userId: String): List<Transaction> {
        return try {
            val snapshot = transactionsRef.orderByChild("userId").equalTo(userId).get().await()
            val transactions = mutableListOf<Transaction>()
            
            snapshot.children.forEach { child ->
                child.getValue(Transaction::class.java)?.let { transactions.add(it) }
            }
            
            transactions.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Add/Update pricing package (Admin function)
     */
    suspend fun savePackage(pricingPackage: PricingPackage): String {
        return try {
            val packageId = if (pricingPackage.id.isEmpty()) {
                packagesRef.push().key ?: UUID.randomUUID().toString()
            } else {
                pricingPackage.id
            }
            
            val packageWithId = pricingPackage.copy(
                id = packageId,
                updatedAt = Date()
            )
            
            packagesRef.child(packageId).setValue(packageWithId).await()
            packageId
        } catch (e: Exception) {
            throw e
        }
    }
    
    /**
     * Get default packages for offline/fallback scenarios
     */
    private fun getDefaultPackages(): List<PricingPackage> {
        return listOf(
            PricingPackage(
                id = "basic_1",
                name = "Basic Ad Package",
                description = "Perfect for small businesses starting out",
                price = 299.0,
                durationDays = 30,
                features = listOf(
                    PackageFeature.FEATURED_LISTING.displayName,
                    PackageFeature.MULTIPLE_IMAGES.displayName
                ),
                maxAds = 1,
                categoryType = PackageCategory.BASIC
            ),
            PricingPackage(
                id = "premium_1",
                name = "Premium Ad Package",
                description = "Enhanced visibility with social media promotion",
                price = 599.0,
                durationDays = 30,
                features = listOf(
                    PackageFeature.FEATURED_LISTING.displayName,
                    PackageFeature.SOCIAL_MEDIA_PROMOTION.displayName,
                    PackageFeature.MULTIPLE_IMAGES.displayName,
                    PackageFeature.ANALYTICS_DASHBOARD.displayName
                ),
                maxAds = 3,
                isPremium = true,
                categoryType = PackageCategory.PREMIUM
            ),
            PricingPackage(
                id = "business_1",
                name = "Business Ad Package",
                description = "Complete solution for growing businesses",
                price = 999.0,
                durationDays = 30,
                features = listOf(
                    PackageFeature.FEATURED_LISTING.displayName,
                    PackageFeature.SOCIAL_MEDIA_PROMOTION.displayName,
                    PackageFeature.PRIORITY_SUPPORT.displayName,
                    PackageFeature.ANALYTICS_DASHBOARD.displayName,
                    PackageFeature.MULTIPLE_IMAGES.displayName,
                    PackageFeature.VIDEO_ADS.displayName
                ),
                maxAds = 5,
                isPremium = true,
                categoryType = PackageCategory.BUSINESS
            )
        )
    }
}