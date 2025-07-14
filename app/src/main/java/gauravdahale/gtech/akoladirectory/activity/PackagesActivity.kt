package gauravdahale.gtech.akoladirectory.activity

import android.content.Intent
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
import gauravdahale.gtech.akoladirectory.adapter.PackageAdapter
import gauravdahale.gtech.akoladirectory.data.PricingRepository
import gauravdahale.gtech.akoladirectory.models.PricingPackage
import kotlinx.coroutines.launch

/**
 * Activity for displaying available advertisement packages
 */
class PackagesActivity : AppCompatActivity(), PackageAdapter.OnPackageClickListener {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var packageAdapter: PackageAdapter
    private val pricingRepository = PricingRepository.getInstance()
    
    companion object {
        private const val TAG = "PackagesActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)
        
        setupUI()
        loadPackages()
    }
    
    private fun setupUI() {
        // Setup toolbar
        supportActionBar?.apply {
            title = "Advertisement Packages"
            setDisplayHomeAsUpEnabled(true)
        }
        
        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewPackages)
        progressIndicator = findViewById(R.id.progressIndicator)
        
        // Setup RecyclerView
        packageAdapter = PackageAdapter(this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PackagesActivity)
            adapter = packageAdapter
        }
    }
    
    private fun loadPackages() {
        showLoading(true)
        
        lifecycleScope.launch {
            try {
                val packages = pricingRepository.getActivePackages()
                runOnUiThread {
                    showLoading(false)
                    if (packages.isNotEmpty()) {
                        packageAdapter.updatePackages(packages)
                    } else {
                        showError("No packages available at the moment")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading packages", e)
                runOnUiThread {
                    showLoading(false)
                    showError("Failed to load packages. Please try again.")
                }
            }
        }
    }
    
    override fun onPackageClick(pricingPackage: PricingPackage) {
        // Navigate to payment activity
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("package_id", pricingPackage.id)
            putExtra("package_name", pricingPackage.name)
            putExtra("package_price", pricingPackage.price)
            putExtra("package_currency", pricingPackage.currency)
            putExtra("package_description", pricingPackage.description)
        }
        startActivity(intent)
    }
    
    override fun onPackageInfoClick(pricingPackage: PricingPackage) {
        // Show package details dialog or navigate to details activity
        showPackageDetails(pricingPackage)
    }
    
    private fun showPackageDetails(pricingPackage: PricingPackage) {
        val detailsBuilder = StringBuilder()
        detailsBuilder.append("Package: ${pricingPackage.name}\n\n")
        detailsBuilder.append("Price: ${pricingPackage.currency} ${pricingPackage.price}\n")
        detailsBuilder.append("Duration: ${pricingPackage.durationDays} days\n")
        detailsBuilder.append("Max Ads: ${pricingPackage.maxAds}\n\n")
        detailsBuilder.append("Features:\n")
        pricingPackage.features.forEach { feature ->
            detailsBuilder.append("• $feature\n")
        }
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Package Details")
            .setMessage(detailsBuilder.toString())
            .setPositiveButton("Purchase") { _, _ ->
                onPackageClick(pricingPackage)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showLoading(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}