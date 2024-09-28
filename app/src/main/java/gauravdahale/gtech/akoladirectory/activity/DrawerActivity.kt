package gauravdahale.gtech.akoladirectory.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import gauravdahale.gtech.akoladirectory.ChangeFragment
import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.fragments.*
import gauravdahale.gtech.akoladirectory.models.UserModel
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.adapter.PlaceAdapter
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*
import kotlinx.coroutines.*
import java.lang.Runnable

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,

        ChangeFragment {
    override fun inflateFragment(fragmentTag: String, message: String, bundle: Bundle?) {

        if (fragmentTag == "ItemListActivity") {
            val fragment = HomeServicesFragment()
            doFragmentTransaction(fragment, fragmentTag, true, message)
        }
        /*  if (fragmentTag == "Record" ){
              val fragment = RecordFragment()
              doFragmentTransaction(fragment, fragmentTag, true, bundle!!)
          }*/

        /*else if (fragmentTag == getString(R.string.fragment_b)) {
            val fragment = BFragment()
            doFragmentTransaction(fragment, fragmentTag, true, message)
        } else if (fragmentTag == getString(R.string.fragment_c)) {
            val fragment = CFragment()
            doFragmentTransaction(fragment, fragmentTag, true, message)
        }*/

    }

    private var mHandler: Handler? = null
    private var sliderHandler: Handler? = null
    private var navHeader: View? = null
    var txtname: TextView? = null
    var txtcity: TextView? = null
    var userref: DatabaseReference? = null
    lateinit var mAuth: String
    private var mBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    lateinit var dialog: Dialog
val TAG = "Drawer Activity"
    ///-------------------------------------------------------------------------------------------------
private  var appUpdateManager: AppUpdateManager?=null
val MY_REQUEST_CODE=200
   private  lateinit var listener: InstallStateUpdatedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager == AppUpdateManagerFactory.create(this)
        setContentView(R.layout.activity_drawer)

        val toolbarmain = findViewById<Toolbar>(R.id.toolbarmain)
// Creates instance of the manager.

        //    setSupportActionBar(toolbarmain)
        //  toolbarmain.setNavigationIcon(R.drawable.logo)
          listener = InstallStateUpdatedListener { installState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                Log.d(TAG, "An update has been downloaded")
//                showSnackBarForCompleteUpdate()
                Toast.makeText(this, "Update Completed", Toast.LENGTH_SHORT).show()
            }
        }
        checkUpdate()

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this@DrawerActivity, PhoneAuth::class.java))
            finish()
        } else {
            mAuth = FirebaseAuth.getInstance().currentUser!!.uid
            userref = FirebaseDatabase.getInstance().reference.child("USERS").child(mAuth)
        }


        //setAdMob()

        //initinterstitial()


        storePlacePreference()

//Bottom Sheet Behaviour
        val bottom_sheet = findViewById<View>(R.id.bottom_sheet)
        mBehavior = BottomSheetBehavior.from<View>(bottom_sheet)
//------------------------------------------PLACE SELECTOR------------------------------------------
        home_bar_location.setOnClickListener {
            placeDialog()


        }
///-------------------------------------------------------------------------------------------------

        mHandler = Handler()
        sliderHandler = Handler()
        val toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbarmain,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        //     toggle.setDrawerIndicatorEnabled(false);
        //       toggle.setHomeAsUpIndicator(R.drawable.hamburgericon);

        drawer_layout.addDrawerListener(toggle)
        toggle.setToolbarNavigationClickListener {


            if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        };
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        setupmainslider()

        if (savedInstanceState == null) {
            navItemIndex = 0

            CURRENT_TAG = TAG_HOME
            loadfragment(HomeFragment())
        }
        loadNavHeader()
    }

    private fun storePlacePreference() {
        val prefs: SharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
//        Log.d("PLACE ON START", prefs.getString("PLACE", ""))
        val place = prefs.getString("PLACE", "")
        if (prefs.getString("PLACE", "") == "") {

            val editor = prefs.edit()
            val placearray = resources.getStringArray(R.array.Places)
            editor.putString("PLACE", placearray[0])
            editor.apply()

    //            Toast.makeText(DrawerActivity@ this, "Akola City Selected", Toast.LENGTH_SHORT).show()
    //        placeDialog()
        }
    }


//
    public fun placeDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        //dialog.windocw.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.place_dialog)
        var placearray = getResources().getStringArray(R.array.Places)
        val list = mutableListOf<String>(*placearray)
        val adapter = PlaceAdapter(this, list)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.place_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        dialog.show()
    }


    private fun setupmainslider() {
        val mDemoSlider = findViewById<SliderLayout>(R.id.main_slider)
        MainScope().launch(Dispatchers.Main) {
            val bannersRef = FirebaseDatabase.getInstance().getReference("Banners")

            bannersRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        val post = child.getValue(ContactModel::class.java)
                        val textSliderView = DefaultSliderView(applicationContext)
                        // initialize a SliderLayout
                        textSliderView
                                .image(post!!.i).empty(R.drawable.akolanotice)

                                .setOnSliderClickListener {
                                    //Toast.makeText(getApplicationContext(), "clicked image= "+post.getN(), Toast.LENGTH_SHORT).show();
                                    runOnUiThread { showBottomSheetDialog(post) }
                                }.scaleType = BaseSliderView.ScaleType.Fit

                        mDemoSlider.addSlider(textSliderView)

                    }
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion)
                    mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible)
                    mDemoSlider.setCustomAnimation(DescriptionAnimation())
                    mDemoSlider.setDuration(3500)

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

    }

    private fun showBottomSheetDialog(obj: ContactModel) {
        if (mBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.sheet_floating, null)
        val image = view.findViewById<ImageView>(R.id.bannerimage)
        Picasso.with(applicationContext).load(obj.i).into(image)
        val phone = obj.p
        (view.findViewById(R.id.bannername) as TextView).text = obj.n
        (view.findViewById(R.id.banneraddress) as TextView).text = obj.a
        (view.findViewById(R.id.bannerdescription) as TextView).text = obj.d
        /*  (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.hide();
            }
        });*/

        view.findViewById<Button>(R.id.submit_rating).setOnClickListener(View.OnClickListener {
            val uri = "tel:$phone"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        })

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog?.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog?.window!!.addFlags(WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_STATUS)
        }

        // set background transparent
        (view.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))

        mBottomSheetDialog?.show()
        mBottomSheetDialog?.setOnDismissListener(DialogInterface.OnDismissListener {
            mBottomSheetDialog = null
        })

    }

    private fun loadfragment(fragment: Fragment) {
        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments
            val fragment = fragment
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            )
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
fragmentTransaction.addToBackStack(CURRENT_TAG)
            fragmentTransaction.commitAllowingStateLoss()
        }

        // If mPendingRunnable is not null, then add to the message queue
        mHandler!!.post(mPendingRunnable)

        // show or hide the fab button
        //toggleFab();

        //Closing drawer on item click
        drawer_layout!!.closeDrawers()

        // refresh toolbar menu
    }

    fun loadfragment(fragment: Fragment, tag: String) {
        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments
            val fragment = fragment
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            )
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
            fragmentTransaction.addToBackStack(tag)
            fragmentTransaction.commitAllowingStateLoss()
        }

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler!!.post(mPendingRunnable)
        }

        // show or hide the fab button
        //toggleFab();

        //Closing drawer on item click
        drawer_layout!!.closeDrawers()

        // refresh toolbar menu
    }


//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//            if (mInterstitialAd.isLoaded) {
//                mInterstitialAd.show()
//            } else {
//                Log.d("TAG", "The interstitial wasn't loaded yet.")
//            }
//        }
//    }
/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }*/


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                navItemIndex = 0
                CURRENT_TAG = TAG_HOME
                loadfragment(HomeFragment())
            }
            R.id.nav_homeservices -> {
                navItemIndex = 1
                CURRENT_TAG = TAG_HOME_SERVICES
                Toast.makeText(this, "fdfsfd", Toast.LENGTH_LONG).show()

                loadfragment(HomeServicesFragment())
            }

            R.id.nav_shopping -> {
                CURRENT_TAG = TAG_SHOPING
                navItemIndex = 2
                loadfragment(ShoppingFragment())
            }
            R.id.nav_health -> {
                CURRENT_TAG = TAG_HEALTH
                navItemIndex = 3
                loadfragment(HealthFragment())
            }

            R.id.nav_construction -> {
                CURRENT_TAG = TAG_CONSTRUCTION
                navItemIndex = 4
                loadfragment(HomeSolutionsFragment())
            }
            R.id.nav_services -> {
                CURRENT_TAG = TAG_SERVICES
                navItemIndex = 5
                loadfragment(ServicesFragment())
            }
            R.id.nav_education -> {
                navItemIndex = 6
                loadfragment(EducationFragment())
            }


            R.id.nav_emergency -> {
                navItemIndex = 7
                loadfragment(EmergencyFragment())
            }

            R.id.nav_automobiles -> {
                navItemIndex = 8
                loadfragment(AutomobilesFragment())
            }
            R.id.nav_contactus -> {
                navItemIndex = 9
                loadfragment(ContactUS(), "Contact Us")
            }
            R.id.nav_register -> {
                startActivity(Intent(DrawerActivity@ this, NewRegisterActivity::class.java))
            }
            R.id.nav_profile -> {
                Toast.makeText(DrawerActivity@ this, "Coming Soon!!!", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_share -> {
                navItemIndex = 12
                shareApp(applicationContext)
            }
            R.id.nav_send -> {

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("jay.dahale@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Any feedback if you want")
                intent.setPackage("com.google.android.gm")
                if (intent.resolveActivity(packageManager) != null)
                    startActivity(intent)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    val homeFragment: Fragment
        get() {
            when (navItemIndex) {

                0 -> return HomeFragment()

                1 -> {
                    return HealthFragment()
                }

                2 -> {
                    return HomeServicesFragment()
                }
                3 -> {
                    return ConstructionFragment()
                }
                4 -> return HomeSolutionsFragment()
                5, 6 -> return HomeFragment()
                else -> return HomeFragment()
            }
        }

    fun loadNavHeader() {
        navHeader = nav_view.getHeaderView(0)
        txtname = navHeader?.findViewById(R.id.username)
        txtcity = navHeader?.findViewById(R.id.usercity)
        val navimage = navHeader?.findViewById(R.id.header_image) as ImageView
        userref?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val model = p0.getValue(UserModel::class.java)
                txtname?.text = model?.userName

                txtcity?.text = model?.userCity
//                navHeader!!.editprofile.setPaintFlags(navHeader!!.editprofile.getPaintFlags() or Paint
//                                .UNDERLINE_TEXT_FLAG)
                Glide.with(this@DrawerActivity).load(model?.userOccupation).apply(RequestOptions().error(R.drawable.logo).circleCrop()).into(navimage)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })


    }

    companion object {
        // urls to load navigation header background image
        // and profile image
        private val urlNavHeaderBg =
                "https://firebasestorage.googleapis.com/v0/b/yashnews-16aa6.appspot.com/o/yashnewslogo.jpg?alt=media&token=e108ea97-960c-4b2a-a58d-7534ec7bb28b"
        private val urlProfileImg =
                "https://firebasestorage.googleapis.com/v0/b/yashnews-16aa6.appspot.com/o/yashlogo.png?alt=media&token=bca5ae10-7d5a-4e5d-9967-75b305a632ba"
        // index to identify current nav menu item
        var navItemIndex = 0

        // tags used to attach the fragments
        private val TAG_HOME = "home"
        private val TAG_HOME_SERVICES = "homeservices"
        private val TAG_SHOPING = "shoping"
        private val TAG_SERVICES = "services"
        private val TAG_CONSTRUCTION = "construction"
        private val TAG_EDUCATION = "education"
        private val TAG_HEALTH = "health"
        private val TAG_CONTACTUS = "contactus"
        private val TAG_AUTOMOBILES = "automobiles"
        private val TAG_ABOUTUS = "aboutus"
        private val TAG_SHARE = "share"
        private val TAG_SEND = "feedback"
        var CURRENT_TAG = TAG_HOME
    }

    private fun doFragmentTransaction(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean,
            message: String
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        if (message != "") {
            val bundle = Bundle()
            bundle.putString(getString(R.string.reference), message)
            fragment.arguments = bundle
        }

        transaction.replace(R.id.frame, fragment, tag)

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    private fun doFragmentTransaction(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean,
            item: Bundle
    ) {
        val transaction = supportFragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putBundle(getString(R.string.reference), item)
        fragment.arguments = bundle


        transaction.replace(R.id.frame, fragment, tag)

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    private fun getfragment() {
        val fragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        )
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun shareApp(context: Context) {
        val appPackageName = context.packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
                Intent.EXTRA_TEXT,

                "Download ${getString(R.string.app_name)} App Today!!!\n  https://play.google.com/store/apps/details?id=$appPackageName"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }
    private fun checkUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        Log.d(TAG, "Checking for updates")
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager?.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                        AppUpdateType.IMMEDIATE,
                        // The current activity making the update request.
                        this,
                        // Include a request code to later monitor this update request.
                        MY_REQUEST_CODE)
                appUpdateManager?.registerListener(listener)

                // Request the update.
                Log.d(TAG, "Update available")
            } else {
                Log.d(TAG, "No Update available")
                appUpdateManager?.unregisterListener(listener!!)

            }
        }
    }

    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.
    override fun onResume() {
        super.onResume()

        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->

                    if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                    ) {
                        // If an in-app update is already running, resume the update.
                        appUpdateManager?.startUpdateFlowForResult(
                                appUpdateInfo,
                                IMMEDIATE,
                                this,
                                MY_REQUEST_CODE
                        );
                    }
                }
    }
  override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
      // super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "" + "Result Ok")
                    //  handle user's approval }
                }
                Activity.RESULT_CANCELED -> {
                    {
//if you want to request the update again just call checkUpdate()
                    }
                    Log.d(TAG, "" + "Result Cancelled")
                    //  handle user's rejection  }
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    //if you want to request the update again just call checkUpdate()
                    Log.d(TAG, "" + "Update Failure")
                    //  handle update failure
                }
            }
        }
    }

}

