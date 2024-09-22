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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.appcompat.widget.Toolbar
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide


//import com.facebook.CallbackManager
//import com.facebook.FacebookCallback
//import com.facebook.FacebookException
//import com.facebook.FacebookSdk
//import com.facebook.share.Sharer
//import com.facebook.share.model.ShareHashtag
//import com.facebook.share.model.ShareLinkContent
//import com.facebook.share.model.SharePhoto
//import com.facebook.share.model.SharePhotoContent
//import com.facebook.share.widget.ShareDialog
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

import java.text.NumberFormat
import java.util.Calendar
import java.util.Random

import gauravdahale.gtech.akoladirectory.ItemClickListener
import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.CallModel
import kotlinx.android.synthetic.main.cattoolbar.view.*

class RecordListActivity : AppCompatActivity() {
    internal var database = FirebaseDatabase.getInstance()
    private var fab: FloatingActionButton? = null
    internal lateinit var mRef: DatabaseReference
    private var lastPosition = -1
    private val context: Context? = null
    private var mRecyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    internal lateinit var shrinkAnim: ScaleAnimation
    private var tvNoMovies: ProgressBar? = null
    private var securekey: String? = null
    internal lateinit var mAnalytics: FirebaseAnalytics
    internal lateinit var adapter: FirebaseRecyclerAdapter<*, *>
//    internal lateinit var callbackManager: CallbackManager
//    internal lateinit var shareDialog1: ShareDialog
    var image: Bitmap? = null
    var counter = 0

    //Facebook Callback
//    private val callback = object : FacebookCallback<Sharer.Result> {
//        override fun onSuccess(result: Sharer.Result) {
//            Toast.makeText(applicationContext, "Post shared Succesfully", Toast.LENGTH_LONG).show()
//        }
//
//        override fun onCancel() {
//            Toast.makeText(applicationContext, "Post sharing cancelled", Toast.LENGTH_LONG).show()
//        }
//
//        override fun onError(error: FacebookException) {
//            Toast.makeText(applicationContext, "Error in sharing post", Toast.LENGTH_LONG).show()
//        }
//    }

    inner class ItemViewHolder(v: View) : ViewHolder(v), View.OnClickListener {
        internal lateinit var itemClickListener: ItemClickListener
        internal var Image: ImageView
        internal var fshare: ImageView? = null
        internal var Name: TextView
        internal var ratingBar: RatingBar
        internal var Address: TextView
        internal var reviewedby: TextView
        internal var timing: TextView
        internal var ratingtxt: TextView? = null
        internal var Description: TextView
        internal var mview: View? = null
        internal var owner: TextView
        internal var viewscounter: TextView
        //internal var services: TextView
        internal var callbtn: Button
        internal var c: String? = null
        internal var shareButton: ImageView
        internal var container: View

        init {
            //     this.services = v.findViewById<TextView>(R.id.list_services) as TextView
            this.Name = v.findViewById<TextView>(R.id.shoptitle) as TextView
            this.Address = v.findViewById<TextView>(R.id.addresstxtview) as TextView
            this.Image = v.findViewById<ImageView>(R.id.list_contact_image) as ImageView
            this.Description = v.findViewById<TextView>(R.id.list_services) as TextView
            this.timing = v.findViewById<TextView>(R.id.timingtxtview) as TextView
            this.ratingtxt = v.findViewById<TextView>(R.id.reviewint) as TextView
            this.owner = v.findViewById<TextView>(R.id.ownername) as TextView
            this.callbtn = v.findViewById(R.id.callbtn)
            this.ratingBar = v.findViewById(R.id.ratingBar)
            this.shareButton = v.findViewById<View>(R.id.fsharebutton) as ImageView
            this.reviewedby = v.findViewById(R.id.numbreview)
            this.viewscounter = v.findViewById<View>(R.id.viewscounter) as TextView
            this.container = v.findViewById(R.id.rootcontactview)
            v.setOnClickListener(this)

        }

        fun setItemOnClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View) {
            this.itemClickListener.onItemClick(layoutPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cattoolbar)

        val toolbar = findViewById<View>(R.id.toolbarnew) as Toolbar
        setSupportActionBar(toolbar)


        val title = intent.getStringExtra("Title")
        val titlebar = intent.getStringExtra("Settitle")
        val City = intent.getStringExtra("Place")

        toolbar.catoolbartext.text = titlebar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //Code to change the back drawable color
        toolbar.getNavigationIcon()?.setColorFilter(getResources().getColor(R.color.colorPrimaryDark)
                , PorterDuff.Mode.SRC_ATOP);
//-------------------------Admob--------------------------------------------------------------------
//        val ADMOBID=getString(R.string.admob_id)
//        MobileAds.initialize(this, ADMOBID)
//        val adRequest = AdRequest.Builder().build()
//adView.loadAd(adRequest)

///-------------------------------------------------------------------------------------------------
        mRecyclerView = findViewById<View>(R.id.itemlist_recyclerview) as RecyclerView
        tvNoMovies = findViewById<View>(R.id.emptylistview) as ProgressBar
        shrinkAnim = ScaleAnimation(1.15f, 0.0f, 1.15f, 0.0f, 1, 0.5f, 1, 0.5f)


        //FAB
        fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab!!.startAnimation(shrinkAnim)
        fab!!.setOnClickListener {
            startActivity(
                    Intent(
                            applicationContext,
                            NewRegisterActivity::class.java
                    )
            )
        }
//Fab Listener
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab?.getVisibility() == View.VISIBLE) {
                    fab?.hide();
                } else if (dy < 0 && fab?.getVisibility() != View.VISIBLE) {
                    fab?.show();
                }
            }
        });


        //FacebookSdk.sdkInitialize(applicationContext)
        //initfb()
        val empty = findViewById<View>(R.id.emptyimage) as ImageView
        empty.visibility = View.GONE

        val prefs = getSharedPreferences("CITY", Context.MODE_PRIVATE)

        val Place = prefs.getString("CITY_SELECTION", "")

        linearLayoutManager = LinearLayoutManager(applicationContext)

        setTitle(titlebar)
        val query = FirebaseDatabase.getInstance()
                //  .reference.child(Place!!)
                .reference.child("Akola")
                .child(title)


        val options = FirebaseRecyclerOptions.Builder<ContactModel>().setLifecycleOwner(this)
                .setQuery(query, ContactModel::class.java)
                .build()
        mRef = FirebaseDatabase.getInstance().reference.child("Akola").child(title).child("1")
        val isemptylistener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val model = dataSnapshot.getValue(ContactModel::class.java)
                if (model != null) {
                    mRecyclerView!!.adapter = adapter
                } else
                    empty.visibility = View.VISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mRef.addValueEventListener(isemptylistener)
        adapter = object : FirebaseRecyclerAdapter<ContactModel, ItemViewHolder>(options) {

            override fun onDataChanged() {
                super.onDataChanged()
                tvNoMovies!!.visibility = if (itemCount == 0) View.VISIBLE else View.GONE

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_shoplist, parent, false)
                return ItemViewHolder(view)
            }

            override fun onBindViewHolder(
                    viewHolder: ItemViewHolder,
                    position: Int,
                    items: ContactModel
            ) {

                val c: String

                adapter.getRef(position).child("Ratings")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var total = 0.0
                                var count = 0.0
                                var average = 0.0

                                for (child in dataSnapshot.children) {
                                    val rating =
                                            java.lang.Double.parseDouble(child.child("review").value!!.toString())
                                    total = total + rating
                                    count = count + 1
                                    average = total / count

                                }
                                viewHolder.ratingBar.rating = average.toFloat()
                                val text =
                                        NumberFormat.getInstance(applicationContext.resources.configuration.locale)
                                                .format(average)
                                viewHolder.reviewedby.text = count.toInt().toString() + " user reviews"
                                viewHolder.ratingtxt?.text = text
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })


                viewHolder.Name.text = items.n
                viewHolder.Address.text = items.a
                val input = items.d
                val phone: String? = items.p
                if (phone != null) {
                    viewHolder.callbtn.setOnClickListener {
                        val SEPARATOR = " "
                        val n1: String
                        val n2: String
                        mAnalytics = FirebaseAnalytics.getInstance(this@RecordListActivity)
                        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                        val storedname = mSettings.getString("USER_NAME", "")
                        val storedphone = mSettings.getString("USER_NUMBER", "")
                        val bundle = Bundle()
                        val shopname = items.n
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, items.n)
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storedname)
                        mAnalytics.logEvent("Call", bundle)
                        val date = java.text.DateFormat.getDateTimeInstance()
                                .format(Calendar.getInstance().time)
                        //Toast.makeText(getApplicationContext(),"Tap again to dial",Toast.LENGTH_LONG).show();
                        logcall(shopname!!, storedname, storedphone, date)


                        if (phone!!.contains(SEPARATOR)) {
                            val parts = phone?.split(SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()

                            n1 = parts[0]
                            n2 = parts[1]
                            calldialog(n1, n2)
                        } else {
                            val uri = "tel:$phone"
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse(uri)
                            startActivity(intent)
                        }
                    }
                }
                if (items.d != null) {
                    try {
                        viewHolder.Description.text = Html.fromHtml(input)
                    } catch (e: NullPointerException) {
                        viewHolder.Description.text = input
                    }

                }
                viewHolder.timing.text = items.t
                viewHolder.owner.text = items.o
                /* ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
               */ viewHolder.shareButton.setOnClickListener {
                    //postPicture(viewHolder.shareButton, viewHolder.container)
                }

                val postRef = getRef(position)
                viewHolder.viewscounter.text = items.c
                Glide.with(this@RecordListActivity).load(items.i).into(viewHolder.Image)
                if (items.i == null) {
                    viewHolder.Image.setImageResource(R.drawable.logo)
                }
                if (items.t == null) {
                    viewHolder.timing.text = "10:30AM -9:30PM"

                }
                if (items.d == null) {
                    viewHolder.Description.visibility = View.GONE
                }
                this@RecordListActivity.tvNoMovies!!.visibility = View.GONE
                viewHolder.setItemOnClickListener(ItemClickListener { position ->
                    postRef.child("c").runTransaction(object : Transaction.Handler {
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

                    val i = Intent(
                            this@RecordListActivity.applicationContext,
                            RecordActivity::class.java
                    )
                    var reference = adapter.getRef(position).toString()
                    reference = reference.replace(securekey!!, "")
                    i.putExtra("key", reference)
                    i.putExtra("recordtitle", items.n)
                    i.putExtra("address", items.a)
                    i.putExtra("url", items.i)
                    i.putExtra("url2", items.ii)
                    i.putExtra("url3", items.iii)
                    i.putExtra("url4", items.iiii)
                    i.putExtra("phone", items.p)
                    i.putExtra("desc", items.d)
                    i.putExtra("Timings", items.t)
                    i.putExtra("Owner", items.o)

                    i.putExtra("Counter", items.c)
                    this@RecordListActivity.startActivity(i)
                })
            }

//            private fun sharingiscaring() {
//                val content = ShareLinkContent.Builder()
//                        .setContentUrl(Uri.parse("  https://www.youtube.com/watch?v=vOloJKJv1Rc"))
//                        .build()
//                // https://www.youtube.com/watch?v=vOloJKJv1Rc
//            }
        }
        val layoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView!!.layoutManager = layoutManager
        val resId = R.anim.layout_animation_slide_from_right
        val ctx = mRecyclerView!!.context
        // Apply another theme to make the spinner text the right color
        //   final ContextThemeWrapper themedCtx = new ContextThemeWrapper(ctx, R.style.Theme_AppCompat);
        val animation = AnimationUtils.loadLayoutAnimation(ctx, resId)
        mRecyclerView!!.layoutAnimation = animation

        mRecyclerView!!.adapter = adapter
        runLayoutAnimation(mRecyclerView!!)
        mRecyclerView!!.invalidate()
    }

//    private fun initfb() {
//        callbackManager = CallbackManager.Factory.create()
//        shareDialog1 = ShareDialog(this)
//        shareDialog1.registerCallback(callbackManager, callback)
//    }

//    private fun postPicture(shareButton: ImageView, rootView: View) {
//
//        if (counter == 0) {
//            //save the screenshot
//            val image: Bitmap
//            //  rootView = findViewById(R.id.rootcontactview);
//            rootView.isDrawingCacheEnabled = true
//            // creates immutable clone of image
//            image = Bitmap.createBitmap(rootView.drawingCache)
//            // destroy
//            rootView.destroyDrawingCache()
//
//            //share dialog
//            val shareDialog = AlertDialog.Builder(this)
//            shareDialog.setTitle("Share Screen Shot")
//            shareDialog.setMessage("Share image to Facebook?")
//            shareDialog.setPositiveButton("Yes") { dialog, which ->
//                //share the image to Facebook
//                val photo = SharePhoto.Builder().setBitmap(image).build()
//                val content1 = SharePhotoContent.Builder().addPhoto(photo)
//                        .setShareHashtag(ShareHashtag.Builder().setHashtag(getString(R.string.hashtag)).build())
//
//                        .build()
//                if (shareDialog1.canShow(content1)) {
//                    shareDialog1.show(content1)
//                }
//                counter = 1
//                shareButton.performClick()
//            }
//            shareDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
//            shareDialog.show()
//        } else {
//            counter = 0
//            // shareButton.setShareContent(null);
//        }
//    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter.stopListening()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun logcall(shoptitle: String, username: String?, userphone: String?, calldate: String) {
        val call = CallModel(
                shoptitle,
                username,
                userphone,
                calldate
        )
        val databasereference = FirebaseDatabase.getInstance().reference
        databasereference.child("CallLog").child(shoptitle).push().setValue(call)

    }


    fun calldialog(phone1: String, phone2: String) {
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

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation =
                    AnimationUtils.loadAnimation(context, R.anim.layout_animation_slide_from_right)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }


    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_right)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    //    callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val USER_ID = "53"
    }
}
