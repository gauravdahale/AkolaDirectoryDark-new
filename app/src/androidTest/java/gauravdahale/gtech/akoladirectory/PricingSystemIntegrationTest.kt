package gauravdahale.gtech.akoladirectory

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import gauravdahale.gtech.akoladirectory.activity.PackagesActivity
import gauravdahale.gtech.akoladirectory.data.PricingRepository
import gauravdahale.gtech.akoladirectory.models.PackageCategory
import gauravdahale.gtech.akoladirectory.models.PricingPackage
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for the pricing system.
 * These tests verify that the components work together correctly.
 */
@RunWith(AndroidJUnit4::class)
class PricingSystemIntegrationTest {
    
    @get:Rule
    var activityRule: ActivityTestRule<PackagesActivity> = ActivityTestRule(PackagesActivity::class.java)
    
    private lateinit var pricingRepository: PricingRepository
    
    @Before
    fun setUp() {
        pricingRepository = PricingRepository.getInstance()
    }
    
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("gauravdahale.gtech.akoladirectory", appContext.packageName)
    }
    
    @Test
    fun testDefaultPackagesAvailable() = runBlocking {
        // Test that default packages are available when Firebase is not accessible
        val packages = pricingRepository.getActivePackages()
        
        assertNotNull("Packages should not be null", packages)
        assertTrue("Should have at least one package", packages.isNotEmpty())
        assertEquals("Should have 3 default packages", 3, packages.size)
        
        // Verify package structure
        val basicPackage = packages.find { it.categoryType == PackageCategory.BASIC }
        assertNotNull("Basic package should exist", basicPackage)
        assertEquals("Basic package price", 299.0, basicPackage!!.price, 0.01)
        
        val premiumPackage = packages.find { it.categoryType == PackageCategory.PREMIUM }
        assertNotNull("Premium package should exist", premiumPackage)
        assertEquals("Premium package price", 599.0, premiumPackage!!.price, 0.01)
        assertTrue("Premium package should be marked as premium", premiumPackage.isPremium)
        
        val businessPackage = packages.find { it.categoryType == PackageCategory.BUSINESS }
        assertNotNull("Business package should exist", businessPackage)
        assertEquals("Business package price", 999.0, businessPackage!!.price, 0.01)
    }
    
    @Test
    fun testPackageFeatures() = runBlocking {
        val packages = pricingRepository.getActivePackages()
        
        packages.forEach { package ->
            assertFalse("Package name should not be empty", package.name.isEmpty())
            assertFalse("Package description should not be empty", package.description.isEmpty())
            assertTrue("Package price should be positive", package.price > 0)
            assertTrue("Duration should be positive", package.durationDays > 0)
            assertTrue("Max ads should be positive", package.maxAds > 0)
            assertFalse("Features should not be empty", package.features.isEmpty())
        }
    }
    
    @Test
    fun testPackageSorting() = runBlocking {
        val packages = pricingRepository.getActivePackages()
        
        // Verify packages are sorted by price
        for (i in 0 until packages.size - 1) {
            assertTrue(
                "Packages should be sorted by price", 
                packages[i].price <= packages[i + 1].price
            )
        }
    }
    
    @Test
    fun testPackageRepository() = runBlocking {
        val repository = PricingRepository.getInstance()
        assertNotNull("Repository should not be null", repository)
        
        // Test singleton pattern
        val repository2 = PricingRepository.getInstance()
        assertSame("Should return same instance", repository, repository2)
    }
}