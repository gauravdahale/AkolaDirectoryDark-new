package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.R

import com.google.android.material.appbar.MaterialToolbar
import gauravdahale.gtech.akoladirectory.databinding.ActivityEmergencyBinding

class EmergencyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmergencyBinding
    private val database = FirebaseDatabase.getInstance()
    private var mDatabaseReference = database.reference
    private lateinit var adapter: FirebaseRecyclerAdapter<ContactModel, EmergencyActivity.ItemViewHolder>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var shrinkAnim: ScaleAnimation
    private lateinit var mAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("Title") ?: ""
        setupToolbar(title)
        initializeAnalytics()
        setupRecyclerView()

        val place = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE).getString("PLACE", "")
        loadEmergencyData(place, title)
    }

    private fun setupToolbar(title: String) {
//        val toolbar: MaterialToolbar = binding.emertoolbar
        val toolbar: MaterialToolbar = binding.emertoolbar
        val toolbarHeading  = binding.emeregnecytitle
        toolbarHeading.text = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initializeAnalytics() {
        mAnalytics = FirebaseAnalytics.getInstance(applicationContext)
        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val storedName = prefs.getString("USER_NAME", "")
        val storedPhone = prefs.getString("USER_NUMBER", "")
        val place = prefs.getString("PLACE", "")

        val bundle = Bundle().apply {
            putString("Category", intent.getStringExtra("Title"))
            putString(FirebaseAnalytics.Param.CHARACTER, storedName)
            putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, storedPhone)
            putString("City", place)
        }

        mAnalytics.logEvent("EmergencyCategory", bundle)
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        binding.emergencyRecyclerview.layoutManager = linearLayoutManager
        binding.eemptylistview.visibility = View.GONE
    }

    private fun loadEmergencyData(place: String?, title: String) {
        val query = place?.let { database.reference.child(it).child(title) }

        val options = query?.let {
            FirebaseRecyclerOptions.Builder<ContactModel>()
                .setQuery(it, ContactModel::class.java)
                .build()
        }

        adapter = object : FirebaseRecyclerAdapter<ContactModel, ItemViewHolder>(options!!) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.emergencycardview, parent, false)
                return ItemViewHolder(view)
            }

            override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int, items: ContactModel) {
                viewHolder.Name.text = items.n
                viewHolder.Address.text = items.p

//                if (items!=null == View.VISIBLE) {
                    binding.eemptylistview.visibility = View.GONE
//                }
            }
        }

        binding.emergencyRecyclerview.adapter = adapter
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
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Name: TextView = view.findViewById(R.id.emergencytitle)
        val Address: TextView = view.findViewById(R.id.emergencynumber)
    }
}

