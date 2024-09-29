package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat

import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.CallModel
import gauravdahale.gtech.akoladirectory.data.OfferModel
import gauravdahale.gtech.akoladirectory.databinding.ActivityOfferDetailBinding

class OfferDetailActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private lateinit var binding: ActivityOfferDetailBinding  // Replace with your actual binding class name
    private var mUploadbytes: ByteArray? = null
    private var mAnalytics: FirebaseAnalytics? = null
    private lateinit var model: OfferModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var databaseReference: DatabaseReference? = null
    private var reference: DatabaseReference? = null
    private val TAG = "Offer Detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferDetailBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the root of the binding

        mAnalytics = FirebaseAnalytics.getInstance(this)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        model = intent.getSerializableExtra("parcel") as OfferModel
        val ref = "${model.city}/${model.ref}"
        databaseReference = database.getReference(ref)

        // Set up views using binding
        binding.shoptitlerecord.text = model.n
        binding.recordaddress.text = model.a
        binding.callbtnrecord.setOnClickListener { onCallButtonClicked() }

        // Set up other views similarly
        binding.services.text = HtmlCompat.fromHtml(model.od ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.recordtiming.text = model.t
        binding.ownertxtview.text = model.o
        binding.counter.text = model.c
        binding.ratingBar.rating = model.rating?.toFloat() ?: 0f
        binding.rating.text = model.totalreviews

        setSlider(model.oi, model.i, model.ii, model.iii) // Adjust as necessary
    }

    private fun onCallButtonClicked() {
        // Your existing call button logic
    }

    private fun setSlider(imageurl1: String?, imageurl2: String?, imageurl3: String?, imageurl4: String?) {
        val sliderLayout = binding.imagerecord // Use binding to reference the slider layout

        val urlMaps = mapOf(
            "0" to imageurl1,
            "1" to imageurl2,
            "2" to imageurl3,
            "3" to imageurl4
        )

        urlMaps.forEach { (key, url) ->
            val textSliderView = TextSliderView(this).apply {
                image(url).empty(R.drawable.logo)
                setScaleType(BaseSliderView.ScaleType.Fit)
                setOnSliderClickListener(this@OfferDetailActivity)
                bundle(Bundle().apply { putString("extra", key) })
            }
            sliderLayout.addSlider(textSliderView)
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion)
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        sliderLayout.setCustomAnimation(DescriptionAnimation())
        sliderLayout.addOnPageChangeListener(this)
        sliderLayout.setCustomIndicator(binding.customIndicator) // Reference custom indicator from binding
    }

    // Other existing methods remain unchanged

    override fun onStop() {
        binding.imagerecord.stopAutoCycle() // Reference the slider layout from binding
        super.onStop()
    }

    override fun onSliderClick(slider: BaseSliderView?) {
        TODO("Not yet implemented")
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageSelected(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageScrollStateChanged(state: Int) {
        TODO("Not yet implemented")
    }
}



