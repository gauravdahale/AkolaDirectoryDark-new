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
import kotlinx.android.synthetic.main.activity_offer_detail.*
import java.text.DateFormat
import java.util.*


class OfferDetailActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    val ADMOBID="ca-app-pub-4353073709762339~9362988006"

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
    lateinit internal var database: FirebaseDatabase
    lateinit internal var auth: FirebaseAuth
    lateinit internal var user: String
    internal var storedphone: String? = null
    internal var n: String? = null
    internal var a: String? = null
    internal var p: String? = null
    internal var o: String? = null
    internal var t: String? = null
    internal var d: String? = null
    val TAG ="Offer Detail"
    internal var c: String? = null
    private var reference: DatabaseReference? = null
    private var databaseReference: DatabaseReference? = null
    lateinit internal var callbutton: Button
    internal var marquee: TextView? = null
    lateinit var model: OfferModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_detail)
        //-------------------------Admob--------------------------------------------------------------------
//        val ADMOBID="ca-app-pub-4353073709762339~9362988006"
//
//        MobileAds.initialize(this, ADMOBID)
//        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)

///-------------------------------------------------------------------------------------------------

        mAnalytics = FirebaseAnalytics.getInstance(this)
        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val place = mSettings.getString("PLACE", "")
        Log.d("placeselected", "$place")
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val i = intent

        model = i.getSerializableExtra("parcel") as OfferModel
        try {
            val ref = "${model.city}/${model.ref}"
            databaseReference = database.getReference(ref)

        } catch (e: KotlinNullPointerException) {
            Log.e("Reference", e.message.toString())
        }

        val titletextview = findViewById<TextView>(R.id.shoptitlerecord) as TextView
        addresstextview = findViewById<View>(R.id.recordaddress) as TextView
        callbutton = findViewById(R.id.callbtnrecord)
        sliderLayout = findViewById<View>(R.id.imagerecord) as SliderLayout
        descriptionview = findViewById<View>(R.id.services) as TextView
        val owner = findViewById<View>(R.id.ownertxtview) as TextView
        val timings = findViewById<View>(R.id.recordtiming) as TextView
        val counter = findViewById<View>(R.id.counter) as TextView

        user = auth.currentUser!!.uid

        //  databaseReference = database.getReference(ref)

        storedphone = mSettings.getString("USER_NUMBER", "")
        val storedname = mSettings.getString("USER_NAME", "")
        imageurl1 = model.oi
        imageurl2 = model.i
        imageurl3 = model.ii
        imageurl4 = model.iii

        try {
            titletextview.text = model.n
            phonetext.text = model.p
            timings.text = model.t
            addresstextview!!.text = model.a
            owner.text = model.o
            counter.text = model.c
            ratingBar.rating = model.rating!!.toFloat()
            rating.text = model.totalreviews
            ownertxtview.text =model.o


        } catch (e: NullPointerException) {
        }

        try {
            model.od.let { descriptionview!!.text = HtmlCompat.fromHtml(it!!, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        } catch (e: NullPointerException) {
        }

        setslider(imageurl1, imageurl2, imageurl3, imageurl4)
        val phone = model.p
        callbutton.setOnClickListener {
            val SEPARATOR = " "
            val n1: String
            val n2: String
            mAnalytics = FirebaseAnalytics.getInstance(this@OfferDetailActivity)
            val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val storedname = mSettings.getString("USER_NAME", "")
            val storedphone = mSettings.getString("USER_NUMBER", "")
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.n)
            bundle.putString(FirebaseAnalytics.Param.CHARACTER, storedname)
            bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, storedphone)

            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storedname)
            mAnalytics?.logEvent("Offer", bundle)
            val date:String? = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
            //Toast.makeText(getApplicationContext(),"Tap again to dial",Toast.LENGTH_LONG).show();
           try {
               logcall(model.n!!, storedname, storedphone, date)
           }catch (e:KotlinNullPointerException){
               Log.e("Error in Call",e.message.toString())
           }

            if (phone!!.contains(SEPARATOR)) {
                val parts = phone.split(SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                n1 = parts[0]
                n2 = parts[1]
                buttonclicker(n1, n2)
            } else {
                val uri = "tel:$phone"
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(uri)
                startActivity(intent)
            }
        }

    }

    fun logcall(shoptitle: String, username: String?, userphone: String?, calldate: String?) {
        val call = CallModel(shoptitle, username, userphone, calldate)
        val databasereference = FirebaseDatabase.getInstance().reference
        databasereference.child("CallLog").child(shoptitle).push().setValue(call)

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onStop() {
        sliderLayout!!.stopAutoCycle()
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

    fun buttonclicker(phone1: String, phone2: String) {
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



}


