package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import gauravdahale.gtech.akoladirectory.BuildConfig
import gauravdahale.gtech.akoladirectory.ItemClickListener
import gauravdahale.gtech.akoladirectory.R

import gauravdahale.gtech.akoladirectory.data.OfferModel
import kotlinx.android.synthetic.main.activity_offers.*
import java.util.*

class OffersActivity : AppCompatActivity() {
    private var securekey: String? = null
    internal var database = FirebaseDatabase.getInstance()
    private val fab: FloatingActionButton? = null
    internal var mDatabaseReference = this.database.getReference()
    private var mRecyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    var adapter: FirebaseRecyclerAdapter<*, *>? = null
    private var noOffers: ImageView? = null
    internal var ctx: Context? = null

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        internal lateinit var itemClickListener: ItemClickListener
        internal var OfferImage: ImageView
        internal var OfferDetails: TextView

        internal var mview: View? = null


        init {
            this.OfferDetails = v.findViewById<View>(R.id.offer_text) as TextView

            this.OfferImage = v.findViewById<View>(R.id.offer_card_image) as ImageView
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
        setContentView(R.layout.activity_offers)
        setSupportActionBar(toolbaroffers)
        val empty = findViewById<View>(R.id.emptyofferview) as ImageView
        empty.visibility = View.GONE
        securekey = BuildConfig.Firebaseseckey
//        val ADMOBID = "ca-app-pub-4353073709762339~9362988006"
//
//        MobileAds.initialize(this, ADMOBID)
//        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)
        mRecyclerView = findViewById<View>(R.id.offersrecyclerview) as RecyclerView
        noOffers = findViewById<View>(R.id.emptyofferview) as ImageView
        // shrinkAnim = ScaleAnimation(1.15f, 0.0f, 1.15f, 0.0f, 1, 0.5f, 1, 0.5f)
        title = "Offers and Events"
        linearLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView!!.layoutManager = this.linearLayoutManager
        val query = FirebaseDatabase.getInstance()
                .reference.child("directory")
                .child("Offer")
        val options = FirebaseRecyclerOptions.Builder<OfferModel>()
                .setQuery(query, OfferModel::class.java)
                .build()


        adapter =
                object : FirebaseRecyclerAdapter<OfferModel, ItemViewHolder>(options) {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                        val view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.offers_card, parent, false)
                        return ItemViewHolder(view)
                    }

                    override fun onBindViewHolder(
                            viewHolder: ItemViewHolder,
                            position: Int,
                            offers: OfferModel
                    ) {

                        viewHolder.OfferDetails.text = offers.od
                        Picasso.with(applicationContext).load(offers.oi)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(viewHolder.OfferImage, object : Callback {
                                    override fun onSuccess() {

                                    }

                                    override fun onError() {
                                        Picasso.with(this@OffersActivity).load(offers.oi)
                                                .into(viewHolder.OfferImage)
                                    }

                                })
                        if (offers.oi == null) {
                            viewHolder.OfferImage.setImageResource(R.drawable.noimage)
                        }
                        val postRef = getRef(position)

                        viewHolder.setItemOnClickListener(ItemClickListener { pos ->
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

                            var reference = adapter?.getRef(pos).toString()
                            val intent = Intent(applicationContext, OfferDetailActivity::class.java)
                            intent.putExtra("parcel", offers)

                            startActivity(intent)


                        })
                    }
                }

        mRecyclerView!!.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        private val USER_ID = "53"
    }
}
