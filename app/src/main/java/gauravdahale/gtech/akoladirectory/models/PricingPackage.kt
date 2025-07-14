package gauravdahale.gtech.akoladirectory.models

import java.util.*

/**
 * Data model representing an advertisement package that users can purchase
 */
data class PricingPackage(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val currency: String = "INR",
    val durationDays: Int = 30,
    val features: List<String> = emptyList(),
    val maxAds: Int = 1,
    val isPremium: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val categoryType: PackageCategory = PackageCategory.BASIC
)

enum class PackageCategory {
    BASIC, PREMIUM, BUSINESS, ENTERPRISE
}

/**
 * Package features that can be included
 */
enum class PackageFeature(val displayName: String) {
    FEATURED_LISTING("Featured Listing"),
    SOCIAL_MEDIA_PROMOTION("Social Media Promotion"),
    PRIORITY_SUPPORT("Priority Support"),
    ANALYTICS_DASHBOARD("Analytics Dashboard"),
    CUSTOM_BRANDING("Custom Branding"),
    MULTIPLE_IMAGES("Multiple Images"),
    VIDEO_ADS("Video Advertisements"),
    LOCATION_TARGETING("Location Targeting")
}