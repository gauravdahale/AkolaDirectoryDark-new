package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.*

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase

import gauravdahale.gtech.akoladirectory.ItemClickListener
import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.R
import kotlinx.android.synthetic.main.activity_emergency.*
import kotlinx.android.synthetic.main.emergency_toolbar.view.*

class EmergencyActivity : AppCompatActivity() {
    internal var database = FirebaseDatabase.getInstance()
    private val fab: FloatingActionButton? = null
    internal var mDatabaseReference = this.database.reference
    private var mRecyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    lateinit var adapter: FirebaseRecyclerAdapter<*, *>
    lateinit var shrinkAnim: ScaleAnimation
    private var tvNoMovies: RelativeLayout? = null
    internal var ctx: Context? = null
lateinit var mAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emergency_toolbar)

        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val place = prefs.getString("PLACE", "")
        //-------------------------Admob--------------------------------------------------------------------
        //val ADMOBID="ca-app-pub-4353073709762339~9362988006"

//        MobileAds.initialize(this, ADMOBID)
//        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)

///-------------------------------------------------------------------------------------------------

        val toolbar = findViewById<View>(R.id.emertoolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mRecyclerView = findViewById<View>(R.id.emergency_recyclerview) as RecyclerView
        tvNoMovies = findViewById<RelativeLayout>(R.id.eemptylistview) as RelativeLayout
        shrinkAnim = ScaleAnimation(1.15f, 0.0f, 1.15f, 0.0f, 1, 0.5f, 1, 0.5f)
        if (mRecyclerView != null) {
            mRecyclerView!!.setHasFixedSize(true)
        }
        linearLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView!!.layoutManager = this.linearLayoutManager
        val title = intent.getStringExtra("Title")
        val titlebar = intent.getStringExtra("Settitle")
        Toast.makeText(EmergencyActivity@ this, "Title : $place", Toast.LENGTH_SHORT).show()
        toolbar.emeregnecytitle.text=titlebar
        toolbar.navigationIcon?.setColorFilter(resources.   getColor(R.color.colorPrimaryDark)
                , PorterDuff.Mode.SRC_ATOP);
        //logeveny
       mAnalytics= FirebaseAnalytics.getInstance(applicationContext)
        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val storedname = mSettings.getString("USER_NAME", "")
        val storedphone = mSettings.getString("USER_NUMBER", "")
        val bundle = Bundle()
        bundle.putString("Category", title)
        bundle.putString(FirebaseAnalytics.Param.CHARACTER, storedname)
        bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, storedphone)

        bundle.putString("City", place)
        mAnalytics.logEvent("EmergencyCategory", bundle)
        val query = place?.let {
            FirebaseDatabase.getInstance()
                .reference.child(it)
                .child(title)
        }


        val options = query?.let {
            FirebaseRecyclerOptions.Builder<ContactModel>()
                .setQuery(it, ContactModel::class.java)
                .build()

        }

        adapter = object : FirebaseRecyclerAdapter<ContactModel, EmergencyActivity.ItemViewHolder>(options!!) {


            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyActivity.ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.emergencycardview, parent, false)
                if (this@EmergencyActivity.tvNoMovies!!.visibility == View.VISIBLE) {
                    this@EmergencyActivity.tvNoMovies!!.visibility = View.GONE

                }
                return ItemViewHolder(view)
            }


            override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int, items: ContactModel) {


                viewHolder.Name.text = items.n
                viewHolder.Address.text = items.p
            }
        }
        //Full screen ads


        this.mRecyclerView!!.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }


    override fun onStop() {
        super.onStop()
        adapter.stopListening()
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

    override fun onBackPressed() {
        super.onBackPressed()
    }
    class ItemViewHolder(v: View) : ViewHolder(v) {
        internal var itemClickListener: ItemClickListener? = null
        internal var Image: ImageView? = null
        internal var Name: TextView
        internal var Address: TextView
        internal var Phone: TextView? = null
        internal var Description: TextView? = null
        internal var mview: View? = null

        init {
            this.Name = v.findViewById<View>(R.id.emergencytitle) as TextView
            this.Address = v.findViewById<View>(R.id.emergencynumber) as TextView


        }

    }

}
