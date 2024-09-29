package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import gauravdahale.gtech.akoladirectory.BuildConfig
import gauravdahale.gtech.akoladirectory.ItemClickListener
import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.CallModel
import gauravdahale.gtech.akoladirectory.adapter.ShopListAdapter


class ShopListActivityBackup : AppCompatActivity() {
//
//    internal var database = FirebaseDatabase.getInstance()
//    private var fab: FloatingActionButton? = null
//    internal lateinit var mRef: DatabaseReference
//    private var lastPosition = -1
//    private val context: Context? = null
//    private var mRecyclerView: RecyclerView? = null
//    private var linearLayoutManager: LinearLayoutManager? = null
//    internal lateinit var shrinkAnim: ScaleAnimation
//    private var tvNoMovies: ProgressBar? = null
//    private var securekey: String? = null
//    internal lateinit var mAnalytics: FirebaseAnalytics
//    internal lateinit var adapter: ShopListAdapter
//    var image: Bitmap? = null
//    internal var datalist = ArrayList<ContactModel>()
//    var counter = 0
//    //Facebook Callback
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.cattoolbar)
////set toolbar and get intents
//        val toolbar = findViewById<View>(R.id.toolbarnew) as Toolbar
//        setSupportActionBar(toolbar)
//        val title = intent.getStringExtra("Title")
//        val titlebar = intent.getStringExtra("Settitle")
//        val City = intent.getStringExtra("Place")
//        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
//        val Place = prefs.getString("PLACE", "")
//        mAnalytics = FirebaseAnalytics.getInstance(this)
//        toolbar.catoolbartext.text = titlebar
//        toolbar.placeselected.text = "($Place)"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        //Code to change the back drawable color
//        toolbar.getNavigationIcon()?.setColorFilter(getResources().getColor(R.color.colorPrimaryDark)
//                , PorterDuff.Mode.SRC_ATOP);
////---------------------------------------------------------------------------------------------------
//
//        //-------------------------Admob--------------------------------------------------------------------
//
/////-------------------------------------------------------------------------------------------------
//
////Log Event-----------------------------------------------------------------------------------------
//        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
//        val storedname = mSettings.getString("USER_NAME", "")
//        val storedphone = mSettings.getString("USER_NUMBER", "")
//        val bundle = Bundle()
//        bundle.putString("Category", title)
//        bundle.putString(FirebaseAnalytics.Param.CHARACTER, storedname)
//        bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, storedphone)
//
//        bundle.putString("City", Place)
//        mAnalytics.logEvent("CategoryVisited", bundle)
//
//
////
//        mRecyclerView = findViewById<View>(R.id.itemlist_recyclerview) as RecyclerView
//        tvNoMovies = findViewById<View>(R.id.emptylistview) as ProgressBar
//        val empty = findViewById<View>(R.id.emptyimage) as ImageView
//        linearLayoutManager = LinearLayoutManager(applicationContext)
//
//
//        //FAB
//        fab = findViewById<View>(R.id.fab) as FloatingActionButton
//        shrinkAnim = ScaleAnimation(1.15f, 0.0f, 1.15f, 0.0f, 1, 0.5f, 1, 0.5f)
//        fab!!.startAnimation(shrinkAnim)
//        fab!!.setOnClickListener {
//            startActivity(
//                    Intent(
//                            applicationContext,
//                            NewRegisterActivity::class.java
//                    )
//            )
//        }
////Fab Listener
//        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0 && fab?.getVisibility() == View.VISIBLE) {
//                    fab?.hide();
//                } else if (dy < 0 && fab?.getVisibility() != View.VISIBLE) {
//                    fab?.show();
//                }
//            }
//        });
//
////---------------------------------------------------------------------------------------------------------
////        FacebookSdk.sdkInitialize(applicationContext)
////        initfb()
//
//        val query = FirebaseDatabase.getInstance()
//                //  .reference.child(Place!!)
//                .reference.child(Place!!)
//                .child(title!!)
//
//
//        val options = FirebaseRecyclerOptions.Builder<ContactModel>().setLifecycleOwner(this)
//                .setQuery(query, ContactModel::class.java)
//                .build()
//        //---------------------
//        mRef = FirebaseDatabase.getInstance().reference.child("Akola").child(title)
//        val isemptylistener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    mRecyclerView!!.adapter = adapter
//                    empty.visibility = View.GONE
//                    emptylistview.hide()
//                    tvNoMovies?.visibility = View.GONE
//                } else
//                    empty.visibility = View.VISIBLE
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        }
//        mRef.addListenerForSingleValueEvent(isemptylistener)
//        datalist = ArrayList<ContactModel>()
//
//        val listener = object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                Log.d("onCancelled", "Error")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                datalist.clear()
//                for (ds: DataSnapshot in p0.children) {
//                    try{
//                        val model = ds.getValue(ContactModel::class.java)!!
//                        model.key = ds.key
//                        model.ref = "${p0.key}/${ds.key}"
//                        model.city = Place
//                        datalist.add(model)
//                        Log.d("onDataChange", "${model.ref}")
//                    }catch (e:Exception){
//                        Log.e("SHOPLIST", "Shoplist $title"+ e.message.toString() )
//                    }
//                }
//                val newlist = ArrayList(datalist)
//                adapter.submitList(datalist)
//                emptylistview.hide()
//                empty.visibility = View.GONE
//            }
//        }
//
//        query.addValueEventListener(listener)
//
//
//        adapter = ShopListAdapter(this, datalist)
//        val layoutManager = LinearLayoutManager(applicationContext)
//        mRecyclerView!!.layoutManager = layoutManager
//        val resId = R.anim.layout_animation_slide_from_right
//        val ctx = mRecyclerView!!.context
//        // Apply another theme to make the spinner text the right color
//        //   final ContextThemeWrapper themedCtx = new ContextThemeWrapper(ctx, R.style.Theme_AppCompat);
//        val animation = AnimationUtils.loadLayoutAnimation(ctx, resId)
//        mRecyclerView!!.layoutAnimation = animation
//
//        mRecyclerView!!.adapter = adapter
//        runLayoutAnimation(mRecyclerView!!)
//        mRecyclerView!!.invalidate()
//    }
//
//
////    public fun postPicture(shareButton: ImageView, rootView: View) {
////
////
////        //save the screenshot
////        val image: Bitmap
////        //  rootView = findViewById(R.id.rootcontactview);
////        rootView.isDrawingCacheEnabled = true
////        // creates immutable clone of image
////        image = Bitmap.createBitmap(rootView.drawingCache)
////        // destroy
////        rootView.destroyDrawingCache()
////
////
////    }
//
//
//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        super.onBackPressed()
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                return true
//            }
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
//
//    fun logcall(shoptitle: String, username: String?, userphone: String?, calldate: String) {
//        val call = CallModel(
//                shoptitle,
//                username,
//                userphone,
//                calldate
//        )
//        val databasereference = FirebaseDatabase.getInstance().reference
//        databasereference.child("CallLog").child(shoptitle).push().setValue(call)
//
//    }
//
//
//    fun calldialog(phone1: String, phone2: String) {
//        val phonetxt1: TextView
//        val phonetxt2: TextView
//        val dialogbuilder = AlertDialog.Builder(this)
//
//        val inflater = this.layoutInflater
//        val alertlayout = inflater.inflate(R.layout.custom_call_dialog, null)
//        dialogbuilder.setView(alertlayout)
//        phonetxt1 = alertlayout.findViewById(R.id.numberone)
//        phonetxt1.text = phone1
//        phonetxt2 = alertlayout.findViewById(R.id.numbertwo)
//        phonetxt2.text = phone2
//
//        phonetxt1.setOnClickListener {
//            val uri = "tel:$phone1"
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse(uri)
//            startActivity(intent)
//        }
//        phonetxt2.setOnClickListener {
//            val uri = "tel:$phone2"
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse(uri)
//            startActivity(intent)
//        }
//
//        val alertDialog = dialogbuilder.create()
//        alertDialog.show()
//    }
//
//    private fun setAnimation(viewToAnimate: View, position: Int) {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
//            val animation =
//                    AnimationUtils.loadAnimation(context, R.anim.layout_animation_slide_from_right)
//            viewToAnimate.startAnimation(animation)
//            lastPosition = position
//        }
//
//
//    }
//
//    private fun runLayoutAnimation(recyclerView: RecyclerView) {
//        val context = recyclerView.context
//        val controller =
//                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_right)
//
//        recyclerView.layoutAnimation = controller
//        recyclerView.adapter!!.notifyDataSetChanged()
//        recyclerView.scheduleLayoutAnimation()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //   callbackManager.onActivityResult(requestCode, resultCode, data)
//    }
//
//    companion object {
//        private val USER_ID = "53"
//        val ADMOBID = "ca-app-pub-4353073709762339~9362988006"
//
//    }

}
