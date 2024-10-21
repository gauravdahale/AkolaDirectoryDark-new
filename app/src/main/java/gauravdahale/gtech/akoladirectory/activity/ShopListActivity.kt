package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.appevents.codeless.internal.ViewHierarchy.setOnClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.adapter.ShopListAdapter
import gauravdahale.gtech.akoladirectory.databinding.CattoolbarBinding
import gauravdahale.gtech.akoladirectory.models.CallModel
import gauravdahale.gtech.akoladirectory.viewmodels.ShopListViewModel
import gauravdahale.gtech.akoladirectory.viewmodels.ShopListViewModelFactory

class ShopListActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: CattoolbarBinding

    // Firebase
    private lateinit var mAnalytics: FirebaseAnalytics

    // RecyclerView and Adapter
    private lateinit var adapter: ShopListAdapter

    // ViewModel
    private lateinit var viewModel: ShopListViewModel

    // UI elements
    private lateinit var fab: FloatingActionButton
    private lateinit var progressBar: ProgressBar

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

    // Toolbar setup
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarnew)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarnew.navigationIcon?.setColorFilter(
            resources.getColor(R.color.colorPrimaryDark),
            PorterDuff.Mode.SRC_ATOP
        )

        val titlebar = intent.getStringExtra("Settitle") ?: ""
        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val place = prefs.getString("PLACE", "") ?: ""

        binding.catoolbartext.text = titlebar
        binding.placeselected.text = "($place)"
    }

    // Firebase Analytics setup
    private fun initializeAnalytics() {
        mAnalytics = FirebaseAnalytics.getInstance(this)
        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val storedName = prefs.getString("USER_NAME", "") ?: ""
        val storedPhone = prefs.getString("USER_NUMBER", "") ?: ""
        val city = prefs.getString("PLACE", "") ?: ""
        val category = intent.getStringExtra("Title") ?: ""

        val bundle = Bundle().apply {
            putString("Category", category)
            putString(FirebaseAnalytics.Param.CHARACTER, storedName)
            putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, storedPhone)
            putString("City", city)
        }

        mAnalytics.logEvent("CategoryVisited", bundle)
    }

    // RecyclerView setup
    private fun setupRecyclerView() {
        val emptyView = findViewById<ImageView>(R.id.emptyimage)
        progressBar = binding.contentSub.emptylistview
        val recyclerView = findViewById<RecyclerView>(R.id.itemlist_recyclerview)

        adapter = ShopListAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            this,
            R.anim.layout_animation_slide_from_right
        )

        emptyView.isVisible = false
        progressBar.isVisible = false
    }

    // Floating Action Button setup
    private fun setupFloatingActionButton() {
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, NewRegisterActivity::class.java))
        }

//        fab.startAnimation(
////            AnimationUtils.loadAnimation(this, R.anim.fab_scale_animation)
//        )

        findViewById<RecyclerView>(R.id.itemlist_recyclerview)
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) fab.hide() else fab.show()
                }
            })
    }

    // LiveData observation
    private fun observeLiveData() {
        val place = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            .getString("PLACE", "") ?: ""
        val category = intent.getStringExtra("Title") ?: ""
        viewModel = ViewModelProvider(
            this,
            ShopListViewModelFactory("$place/$category", place)
        )[ShopListViewModel::class.java]

        viewModel.getDataSnapshotLiveData().observe(this) {

            adapter.submitList(it)
            progressBar.isVisible = false
        }
    }

    // Handle Up button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    // Log call details to Firebase
    fun logcall(
        shopTitle: String,
        userName: String?,
        userPhone: String?,
        callDate: String
    ) {
        val call = CallModel(shopTitle, userName, userPhone, callDate)
        FirebaseDatabase.getInstance().reference.child("CallLog").child(shopTitle)
            .push().setValue(call)
    }

    // Show call dialog
    fun calldialog(phone1: String, phone2: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val alertLayout = layoutInflater.inflate(R.layout.custom_call_dialog, null)

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

    // Dial a phone number
    private fun dialNumber(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        startActivity(intent)
    }

    companion object {
        const val ADMOBID = "ca-app-pub-4353073709762339~9362988006"
    }
}