package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.adapter.ShopListAdapter
import gauravdahale.gtech.akoladirectory.databinding.CattoolbarBinding
import gauravdahale.gtech.akoladirectory.livedata.ShopListLiveData
import gauravdahale.gtech.akoladirectory.models.CallModel
import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.viewmodels.ShopListViewModel
import gauravdahale.gtech.akoladirectory.viewmodels.ShopListViewModelFactory

class ShopListActivity : AppCompatActivity() {

    private lateinit var binding: CattoolbarBinding
    private lateinit var mAnalytics: FirebaseAnalytics
    private lateinit var adapter: ShopListAdapter
    private lateinit var viewModel: ShopListViewModel
    private lateinit var mRef: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var lastPosition = -1

    private var fab: FloatingActionButton? = null
    private var tvNoMovies: ProgressBar? = null
    private val datalist = ArrayList<ContactModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CattoolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        initializeAnalytics()
        setupRecyclerView()
        setupFloatingActionButton()
        observeLiveData()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbarnew
        setSupportActionBar(toolbar)

        val title = intent.getStringExtra("Title") ?: ""
        val titlebar = intent.getStringExtra("Settitle") ?: ""
        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val place = prefs.getString("PLACE", "") ?: ""

        toolbar.findViewById<TextView>(R.id.catoolbartext).text = titlebar
        toolbar.findViewById<TextView>(R.id.placeselected).text = "($place)"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP)
    }

    private fun initializeAnalytics() {
        mAnalytics = FirebaseAnalytics.getInstance(this)
        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val storedName = prefs.getString("USER_NAME", "")
        val storedPhone = prefs.getString("USER_NUMBER", "")
        val city = prefs.getString("PLACE", "")

        val bundle = Bundle().apply {
            putString("Category", intent.getStringExtra("Title"))
            putString(FirebaseAnalytics.Param.CHARACTER, storedName)
            putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, storedPhone)
            putString("City", city)
        }

        mAnalytics.logEvent("CategoryVisited", bundle)
    }

    private fun setupRecyclerView() {
        val emptyView = findViewById<ImageView>(R.id.emptyimage)
        tvNoMovies = findViewById<ProgressBar>(R.id.emptylistview)
        val recyclerView = findViewById<RecyclerView>(R.id.itemlist_recyclerview)

        linearLayoutManager = LinearLayoutManager(this)
        adapter = ShopListAdapter(this, datalist)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_slide_from_right)

        emptyView.visibility = View.GONE
        tvNoMovies?.visibility = View.GONE
    }

    private fun setupFloatingActionButton() {
        fab = findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener {
                startActivity(Intent(this@ShopListActivity, NewRegisterActivity::class.java))
            }

            startAnimation(ScaleAnimation(1.15f, 0.0f, 1.15f, 0.0f, 1, 0.5f, 1, 0.5f))
        }

        findViewById<RecyclerView>(R.id.itemlist_recyclerview).addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) fab?.hide() else fab?.show()
            }
        })
    }

    private fun observeLiveData() {
        val place = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE).getString("PLACE", "") ?: ""
        viewModel = ViewModelProvider(this, ShopListViewModelFactory("$place/${intent.getStringExtra("Title")}", place)).get(ShopListViewModel::class.java)

        viewModel.getDataSnapshotLiveData().observe(this, Observer {
            adapter.submitList(it)
            tvNoMovies?.visibility = View.GONE
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun logcall(shopTitle: String, userName: String?, userPhone: String?, callDate: String) {
        val call = CallModel(shopTitle, userName, userPhone, callDate)
        FirebaseDatabase.getInstance().reference.child("CallLog").child(shopTitle).push().setValue(call)
    }

    fun calldialog(phone1: String, phone2: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val alertLayout = inflater.inflate(R.layout.custom_call_dialog, null)

        dialogBuilder.setView(alertLayout)
        alertLayout.findViewById<TextView>(R.id.numberone).apply {
            text = phone1
            setOnClickListener { dialNumber(phone1) }
        }
        alertLayout.findViewById<TextView>(R.id.numbertwo).apply {
            text = phone2
            setOnClickListener { dialNumber(phone2) }
        }

        dialogBuilder.create().show()
    }

    private fun dialNumber(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        startActivity(intent)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_slide_from_right)
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        const val ADMOBID = "ca-app-pub-4353073709762339~9362988006"
    }
}
