package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import gauravdahale.gtech.akoladirectory.databinding.RecordActivityDarkBinding
import gauravdahale.gtech.akoladirectory.models.CallModel
import gauravdahale.gtech.akoladirectory.models.ContactModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*


class RecordActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    val ADMOBID = "ca-app-pub-4353073709762339~9362988006"

    private var mUploadbytes: ByteArray? = null
    private var addresstextview: TextView? = null
    private var descriptionview: TextView? = null
    private var sliderLayout: SliderLayout? = null
    internal var mAnalytics: FirebaseAnalytics? = null
    private val phonetextview: TextView? = null
    internal var url1: String? = null
    internal var url2: String? = null
    internal var url3: String? = null
    internal var url4: String? = null
    internal var imageurl1: String? = null
    internal var imageurl2: String? = null
    internal var imageurl3: String? = null
    internal var imageurl4: String? = null
    internal lateinit var database: FirebaseDatabase
    internal lateinit var auth: FirebaseAuth
    internal lateinit var user: String
    internal var storedphone: String? = null
    internal var n: String? = null
    internal var a: String? = null
    internal var p: String? = null
    internal var o: String? = null
    internal var t: String? = null
    internal var d: String? = null
    internal var c: String? = null
    private var reference: DatabaseReference? = null
    private var databaseReference: DatabaseReference? = null
    lateinit internal var callbutton: Button
    internal var marquee: TextView? = null
    lateinit var model: ContactModel
    private lateinit var binding: RecordActivityDarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up full-screen UI
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        actionBar?.hide()

        // Initialize View Binding
        binding = RecordActivityDarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        model = intent.getSerializableExtra("parcel") as ContactModel

        registerShopEvent()

        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val place = mSettings.getString("PLACE", "")
        Log.d("placeselected", "$place")

        try {
            val ref = "${model.city}/${model.ref}"
            databaseReference = database.getReference(ref)
        } catch (e: KotlinNullPointerException) {
            Log.e("Reference", e.message.toString())
        }

        // Bind views using View Binding
        binding.shoptitlerecord.text = model.n
        binding.recordaddress.text = model.a
        binding.callbtnrecord.setOnClickListener { handleCall() }
        binding.recordtiming.text = model.t
        binding.counter.text = model.c
        binding.ratingBar.rating = model.rating!!.toFloat()
        binding.ownertxtview.text = model.o
        binding.rating.text = "${model.rating}/${model.totalreviews} user reviews"

        // Description handling
        model.d?.let {
            binding.services.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } ?: run {
            binding.services.text = d
        }

        // Set slider if images exist
        if (!model.i.isNullOrEmpty()) {
            setslider(model.i, model.ii, model.iii, model.iiii)
        }

        // Log call analytics
        mAnalytics = FirebaseAnalytics.getInstance(this)
    }

    private fun handleCall() {
        val phone = model.p
        val SEPARATOR = " "
        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val storedname = mSettings.getString("USER_NAME", "")
        val storedphone = mSettings.getString("USER_NUMBER", "")

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, model.n)
            putString(FirebaseAnalytics.Param.CHARACTER, storedname)
            putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, storedphone)
        }
        mAnalytics?.logEvent("Call", bundle)

        val date: String? = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        logcall(model.n!!, storedname, storedphone, date)

        if (phone!!.contains(SEPARATOR)) {
            val parts = phone.split(SEPARATOR).filter { it.isNotEmpty() }
            if (parts.size == 2) {
                callClick(parts[0], parts[1])
            }
        } else {
            val uri = "tel:$phone"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
    }

    private fun registerShopEvent() {
        mAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("ShopName", model.n)
        mAnalytics!!.logEvent("shop_visited", bundle)
    }

    fun logcall(shoptitle: String, username: String?, userphone: String?, calldate: String?) {
        val call = CallModel(shoptitle, username, userphone, calldate)
        val databasereference = FirebaseDatabase.getInstance().reference
        databasereference.child("CallLog").child(shoptitle).push().setValue(call)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onStop() {
       binding. imagerecord!!.stopAutoCycle()
        super.onStop()

    }

    override fun onSliderClick(slider: BaseSliderView) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun getRating(view: View): View {

        val dialogbuilder = AlertDialog.Builder(this)
        val submit: Button
        val ratingBar: RatingBar
        val commentbox: TextInputLayout

        val inflater = this.layoutInflater
        val alertlayout = inflater.inflate(R.layout.custom_rating_dialog, null)
        dialogbuilder.setView(alertlayout)
        ratingBar = alertlayout.findViewById(R.id.addratingbar)
        commentbox = alertlayout.findViewById(R.id.commentbox)
        submit = alertlayout.findViewById(R.id.submitbtn)
        val rating = ratingBar.rating

        val alertDialog = dialogbuilder.create()
        alertDialog.show()

        submit.setOnClickListener(View.OnClickListener {
            if (ratingBar.rating == 0.0f) {
                Toast.makeText(applicationContext, "Please give some rating", Toast.LENGTH_LONG).show()

                return@OnClickListener
            }
            try {
                commentbox.editText?.text.toString()
                databaseReference?.parent?.parent?.child("Ratings")?.child(model.n!!)?.child(user)?.child("review")?.setValue(ratingBar.rating)
                databaseReference?.parent?.parent?.child("Ratings")?.child(model.n!!)?.child(user)?.child("comment")?.setValue(commentbox.editText?.text.toString())
                databaseReference?.child("Ratings")?.child(user)?.child("review")?.setValue(ratingBar.rating)
                logreview(this, model.n!!, rating.toString(), user)
            } catch (e: java.lang.Exception) {
                Log.e("Reference", e.message.toString())
            }

            alertDialog.dismiss()
        })
        return view

    }

    fun increasecounter(postreference: DatabaseReference) {
        postreference.child("c").runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                if (currentData.value == null) {
                    val r = Random()
                    val random = Random().nextInt(300) + 500

                    val randomcount = Integer.toString(random)
                    currentData.value = randomcount
                } else {
                    val stringValue = currentData.value as String?
                    val intValue = Integer.parseInt(stringValue!!)
                    val increasedIntValue = intValue + 1
                    currentData.value = increasedIntValue.toString()
                }
                return Transaction.success(currentData)
            }

            override fun onComplete(
                    databaseError: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
            ) {
                if (databaseError != null) {
                    println("Firebase counter increment failed!")
                } else {
                    println("Firebase counter increment succeeded!")
                }
            }
        })

    }

    fun setslider(imageurl1: String?, imageurl2: String?, imageurl3: String?, imageurl4: String?) {
        sliderLayout = binding.imagerecord
        url1 = imageurl1
        url2 = imageurl2
        url3 = imageurl3
        url4 = imageurl4
        if (url2 == null) {
            url2 = url1
        }
        if (url3 == null) {
            url3 = url1
        }

        if (url4 == null) {
            url4 = url1
        }

        val url_maps = HashMap<String?, String?>()


        url_maps["0"] = url1
        url_maps["1"] = url2
        url_maps["2"] = url3
        url_maps["3"] = url4
        for (name in url_maps.keys) {
            val textSliderView = TextSliderView(this)
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps[name]).empty(R.drawable.logo)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this)

            //add your extra information
            textSliderView.bundle(Bundle())
            textSliderView.bundle
                    .putString("extra", name)
            sliderLayout!!.addSlider(textSliderView)
        }
        sliderLayout!!.setPresetTransformer(SliderLayout.Transformer.Accordion)
        sliderLayout!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        sliderLayout!!.setCustomAnimation(DescriptionAnimation())

        sliderLayout!!.addOnPageChangeListener(this)
        sliderLayout!!.setCustomIndicator(findViewById<PagerIndicator>(R.id.custom_indicator))
    }
    fun setslider() {
        url1 = imageurl1

        val url_maps = HashMap<String?, String?>()


        url_maps["0"] = url1

            val textSliderView = TextSliderView(this)
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps["0"]).empty(R.drawable.noimage)
                    .setScaleType(BaseSliderView.ScaleType.Fit)

            //add your extra information
            textSliderView.bundle(Bundle())
            textSliderView.bundle
                    .putString("extra", url_maps["0"])
            sliderLayout!!.addSlider(textSliderView)

        sliderLayout!!.setPresetTransformer(SliderLayout.Transformer.Accordion)
        sliderLayout!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        sliderLayout!!.setCustomAnimation(DescriptionAnimation())
sliderLayout!!.stopAutoCycle()
        sliderLayout!!.addOnPageChangeListener(this)
        sliderLayout!!.setCustomIndicator(findViewById<PagerIndicator>(R.id.custom_indicator))
    }

    fun callClick(phone1: String, phone2: String) {
        val phonetxt1: TextView
        val phonetxt2: TextView
        val dialogbuilder = AlertDialog.Builder(this)

        val inflater = this.layoutInflater
        val alertlayout = inflater.inflate(R.layout.custom_call_dialog, null)
        dialogbuilder.setView(alertlayout)
        phonetxt1 = alertlayout.findViewById(R.id.numberone)
        phonetxt1.text = phone1
        phonetxt2 = alertlayout.findViewById(R.id.numbertwo)
        phonetxt2.text = phone2

        phonetxt1.setOnClickListener {
            val uri = "tel:$phone1"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
        phonetxt2.setOnClickListener {
            val uri = "tel:$phone2"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }

        val alertDialog = dialogbuilder.create()
        alertDialog.show()
    }

    fun setdata(record: ContactModel) {}

    inner class BackGroundImageResize(bitmap: Bitmap?) : AsyncTask<Uri, Int, ByteArray>() {
        internal var mBitmap: Bitmap? = null

        init {
            if (bitmap != null) this.mBitmap = bitmap

        }

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()

        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg uris: Uri): ByteArray? {
            if (mBitmap == null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uris[0])
                } catch (e: IOException) {
                    Log.e(TAG, "doInbackground: IOException" + e.message)
                }

            }
            var bytes: ByteArray? = null
            bytes = getbytesfrombitmap(mBitmap!!, 100)

            return bytes

        }


        override fun onPostExecute(bytes: ByteArray) {
            super.onPostExecute(bytes)
            mUploadbytes = bytes
            Toast.makeText(applicationContext, "Compressing Donw", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = "RecordActivity"


        fun getbytesfrombitmap(bitmap: Bitmap, quality: Int): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            return stream.toByteArray()
        }
    }
    fun logreview(context: Context, shopname: String, rating: String, uuid: String)
    {
        val  mAnalytics = FirebaseAnalytics.getInstance(context)
        val mSettings = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val storedname = mSettings.getString("USER_NAME", "")
        val storedphone = mSettings.getString("USER_NUMBER", "")
        val bundle = Bundle()
        bundle.putString("ShopName", shopname)
        bundle.putString("Username", storedname)
        bundle.putString("UserPhone", storedphone)
        bundle.putString("rating", rating)
        bundle.putString("userUid", uuid)
        mAnalytics.logEvent("Review", bundle)


    }

}


