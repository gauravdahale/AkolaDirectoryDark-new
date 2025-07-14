package gauravdahale.gtech.akoladirectory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.PricingPackage

/**
 * Adapter for displaying pricing packages in RecyclerView
 */
class PackageAdapter(
    private val listener: OnPackageClickListener
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {
    
    private var packages = listOf<PricingPackage>()
    
    interface OnPackageClickListener {
        fun onPackageClick(pricingPackage: PricingPackage)
        fun onPackageInfoClick(pricingPackage: PricingPackage)
    }
    
    fun updatePackages(newPackages: List<PricingPackage>) {
        packages = newPackages
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package, parent, false)
        return PackageViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        holder.bind(packages[position])
    }
    
    override fun getItemCount(): Int = packages.size
    
    inner class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPackageName: TextView = itemView.findViewById(R.id.tvPackageName)
        private val tvPackagePrice: TextView = itemView.findViewById(R.id.tvPackagePrice)
        private val tvPackageDescription: TextView = itemView.findViewById(R.id.tvPackageDescription)
        private val tvPackageDuration: TextView = itemView.findViewById(R.id.tvPackageDuration)
        private val tvPackageFeatures: TextView = itemView.findViewById(R.id.tvPackageFeatures)
        private val btnPurchase: Button = itemView.findViewById(R.id.btnPurchase)
        private val btnInfo: Button = itemView.findViewById(R.id.btnInfo)
        private val tvPremiumBadge: TextView = itemView.findViewById(R.id.tvPremiumBadge)
        
        fun bind(pricingPackage: PricingPackage) {
            tvPackageName.text = pricingPackage.name
            tvPackagePrice.text = "${pricingPackage.currency} ${String.format("%.2f", pricingPackage.price)}"
            tvPackageDescription.text = pricingPackage.description
            tvPackageDuration.text = "${pricingPackage.durationDays} days"
            
            // Show premium badge if applicable
            tvPremiumBadge.visibility = if (pricingPackage.isPremium) View.VISIBLE else View.GONE
            
            // Display features (limit to first 3 for card view)
            val featuresText = if (pricingPackage.features.size > 3) {
                pricingPackage.features.take(3).joinToString("\n• ", "• ") + 
                "\n• +${pricingPackage.features.size - 3} more features"
            } else {
                pricingPackage.features.joinToString("\n• ", "• ")
            }
            tvPackageFeatures.text = featuresText
            
            // Set click listeners
            btnPurchase.setOnClickListener {
                listener.onPackageClick(pricingPackage)
            }
            
            btnInfo.setOnClickListener {
                listener.onPackageInfoClick(pricingPackage)
            }
            
            // Set card background based on package type
            val backgroundRes = when (pricingPackage.categoryType) {
                gauravdahale.gtech.akoladirectory.models.PackageCategory.PREMIUM -> R.drawable.package_card_premium
                gauravdahale.gtech.akoladirectory.models.PackageCategory.BUSINESS -> R.drawable.package_card_business
                gauravdahale.gtech.akoladirectory.models.PackageCategory.ENTERPRISE -> R.drawable.package_card_enterprise
                else -> R.drawable.package_card_basic
            }
            
            try {
                itemView.setBackgroundResource(backgroundRes)
            } catch (e: Exception) {
                // Fallback to default card background
                itemView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
            }
        }
    }
}