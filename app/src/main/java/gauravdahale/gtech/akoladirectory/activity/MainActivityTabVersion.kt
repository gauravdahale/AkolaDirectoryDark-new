package gauravdahale.gtech.akoladirectory.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager.widget.ViewPager
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.picasso.Picasso
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.UserForm
import gauravdahale.gtech.akoladirectory.activity.OffersActivity
//import gauravdahale.gtech.akoladirectory.activity. RegisterActivity
import gauravdahale.gtech.akoladirectory.fcm.Constants
import gauravdahale.gtech.akoladirectory.fragments.*
import gauravdahale.gtech.akoladirectory.models.ContactModel
import java.util.*

class MainActivityTabVersion : AppCompatActivity {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    var mDemoSlider: SliderLayout? = null
    var auth: FirebaseAuth? = null
    var registerbutton: ImageView? = null
    var dealsbutton: ImageView? = null
    var sharebutton: ImageView? = null
    var spinner: Spinner? = null
    private var mBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null

    constructor(mRemoteConfig: FirebaseRemoteConfig) {
        val mRemoteConfig1 = mRemoteConfig
    }

    constructor() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tab_version)
        val toolbar = findViewById<View>(R.id.toolbartab) as Toolbar
        setSupportActionBar(toolbar)
        tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        //Bottom Sheet
//        MobileAds.initialize(this, "ca-app-pub-4353073709762339/7293683279")
        Constants.CHANNEL_ID = "my_channel_01"
        Constants.CHANNEL_NAME = "Simplified Coding Notification"
        Constants.CHANNEL_DESCRIPTION = "www.sim"
        val bottom_sheet = findViewById<View>(R.id.bottom_sheet)
        mBehavior = BottomSheetBehavior.from(bottom_sheet)
        val marquee = findViewById<TextView>(R.id.marquee)
        marquee.isSelected = true
        val msgref = FirebaseDatabase.getInstance().reference.child("msg")
        msgref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val model = dataSnapshot.getValue(ContactModel::class.java)!!
                marquee.text = model.n
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        //Spinner
        val prefs = getSharedPreferences("CITY", Context.MODE_PRIVATE)
        spinner = findViewById(R.id.locationspinner)
        val spinneradapter = ArrayAdapter(this@MainActivityTabVersion, R.layout.custom_spinner_item, resources.getStringArray(R.array.Places))
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.setAdapter(spinneradapter)
        spinneradapter.notifyDataSetChanged()
        spinner?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                var editor = prefs.edit()
                editor.putString("CITY_SELECTION", spinner?.getSelectedItem().toString())
                Toast.makeText(this@MainActivityTabVersion, spinner?.getSelectedItem().toString(), Toast.LENGTH_SHORT).show()
                editor = prefs.edit()
                editor.putString("CITY_SELECTION", spinner?.getSelectedItem().toString())
                editor.apply()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
        //FCM
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance)
            mChannel.description = Constants.CHANNEL_DESCRIPTION
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannel(mChannel)
        }
        /*
             * Displaying a notification locally
             */
//MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");
//FCM ENDED
// options. if you override onClickTabListener.
        viewPager = findViewById<View>(R.id.newViewPager) as ViewPager
        setupViewPager(viewPager)
        //          tabLayout = (UltimateTabLayout) findViewById(R.id.tabs);
        tabLayout!!.setupWithViewPager(viewPager)
        //Register
        registerbutton = findViewById<View>(R.id.registershop) as ImageView
//        registerbutton!!.setOnClickListener {
//            val i = Intent(applicationContext, RegisterActivity::class.java)
//            startActivity(i)
//        }
        //Deal
//        val animation1: Animation = AlphaAnimation(this,1, 0)
//        animation1.duration = 1000
//        animation1.interpolator = LinearInterpolator()
//        animation1.repeatCount = Animation.INFINITE
//        animation1.repeatMode = Animation.REVERSE
//        dealsbutton = findViewById<View>(R.id.deals) as ImageView
//        dealsbutton!!.startAnimation(animation1)
//        dealsbutton!!.setOnClickListener {
//            val i = Intent(applicationContext, OffersActivity::class.java)
//            startActivity(i)
//        }
        //ShareButton
        sharebutton = findViewById<View>(R.id.sharebtn) as ImageView
        sharebutton!!.setOnClickListener(View.OnClickListener {
            val share = Intent("android.intent.action.SEND")
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, "आपल्या अकोल्याची अकोला डिरेक्टरी अँप आजच डाउनलोड करा: https://play.google.com/store/apps/details?id=gauravdahale.gtech.akoladirectory ")
            share.putExtra("android.intent.extra.SUBJECT", "डाउनलोड करा अकोला शहराची एक नंबर अँप")
            startActivity(Intent.createChooser(share, "Akola Directory"))
            return@OnClickListener
        })
        //BannerSlider
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mDemoSlider = findViewById<View>(R.id.slider) as SliderLayout
        mDemoSlider!!.post {
            val bannersRef = FirebaseDatabase.getInstance().getReference("Banners")
            bannersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        val post = child.getValue(ContactModel::class.java)!!
                        val textSliderView = DefaultSliderView(applicationContext)
                        // initialize a SliderLayout
                        textSliderView
                                .image(post.i).empty(R.drawable.akolanotice)
                                .setOnSliderClickListener {
                                    //Toast.makeText(getApplicationContext(), "clicked image= "+post.getN(), Toast.LENGTH_SHORT).show();
                                    runOnUiThread { showBottomSheetDialog(post) }
                                }.scaleType = BaseSliderView.ScaleType.Fit
                        mDemoSlider!!.addSlider(textSliderView)
                    }
                    mDemoSlider!!.setPresetTransformer(SliderLayout.Transformer.Accordion)
                    mDemoSlider!!.indicatorVisibility = PagerIndicator.IndicatorVisibility.Invisible
                    mDemoSlider!!.setCustomAnimation(DescriptionAnimation())
                    mDemoSlider!!.currentPosition = 1
                    mDemoSlider!!.setDuration(2500)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(HomeServicesFragment(), "Home Services")
        adapter.addFrag(ShoppingFragment(), "Shopping")
        adapter.addFrag(HealthFragment(), "Health")
        adapter.addFrag(EducationFragment(), "Education")
        adapter.addFrag(HomeSolutionsFragment(), "Construction")
        adapter.addFrag(WeddingFragment(), "Wedding")
        adapter.addFrag(ConstructionFragment(), "Food & Hotel")
        adapter.addFrag(AutomobilesFragment(), "Automobiles")
        adapter.addFrag(ServicesFragment(), "Consultants and Services")
        adapter.addFrag(EmergencyFragment(), "Emergency")
        //    adapter.addFrag(new SocialFragment(), "Social");
//  adapter.addFrag(new MidcFragment(), "Industries");
        adapter.addFrag(ContactUS(), "Contact US")
        viewPager!!.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager?) : FragmentStatePagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivityTabVersion)
        builder.setTitle("Please Share")
        builder.setMessage("Please Share Akola Directory App With Your Friends And Family")
        builder.setNegativeButton("SHARE"
        ) { dialog, which ->
            val share = Intent("android.intent.action.SEND")
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, "आपल्या अकोल्याची अकोला डिरेक्टरी अँप आजच डाउनलोड करा: https://play.google.com/store/apps/details?id=gauravdahale.gtech.akoladirectory ")
            share.putExtra("android.intent.extra.SUBJECT", "डाउनलोड करा अकोला शहराची एक नंबर अँप")
            startActivity(Intent.createChooser(share, "Akola Directory"))
        }
        builder.setPositiveButton("QUIT"
        ) { dialog, which -> finish() }
        builder.show()
    }

//    private fun checkFirstRun() {
//        val PREFS_NAME = "MyPrefsFile"
//        val PREF_VERSION_CODE_KEY = "3.0"
//        val DOESNT_EXIST = -1
//        // Get current version code
//        val currentVersionCode = xBuildConfig.VERSION_CODE
//        // Get saved version code
//        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        val savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)
//        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
//        val userrinfo = mSettings.getString("USER_NAME", "")
//        // Check for first run or upgrade
//        if (userrinfo!!.isEmpty()) {
//            val intent = Intent(applicationContext, UserForm::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//        if (currentVersionCode == savedVersionCode) { // This is just a normal run
//            return
//        } else if (savedVersionCode == DOESNT_EXIST) { // TODO This is a new install (or the user cleared the shared preferences)
//            startActivity(Intent(applicationContext, UserForm::class.java))
//        } else if (currentVersionCode > savedVersionCode) {
//            val intent = Intent(applicationContext, UserForm::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//            // TODO This is an upgrade
//        }
//        // Update the shared preferences with the current version code
//        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
//    }

    private fun showBottomSheetDialog(obj: ContactModel) {
        if (mBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val view = layoutInflater.inflate(R.layout.sheet_floating, null)
        val image = view.findViewById<ImageView>(R.id.bannerimage)
        Picasso.with(applicationContext).load(obj.i).into(image)
        val phone = obj.p
        (view.findViewById<View>(R.id.bannername) as TextView).text = obj.n
        (view.findViewById<View>(R.id.banneraddress) as TextView).text = obj.a
        (view.findViewById<View>(R.id.bannerdescription) as TextView).text = obj.d
        /*  (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.hide();
            }
        });*/view.findViewById<View>(R.id.submit_rating).setOnClickListener {
            val uri = "tel:$phone"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog!!.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // set background transparent
        (view.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        mBottomSheetDialog!!.show()
        mBottomSheetDialog!!.setOnDismissListener { mBottomSheetDialog = null }
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }

    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}