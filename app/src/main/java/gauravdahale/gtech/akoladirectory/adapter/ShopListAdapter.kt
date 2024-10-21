package gauravdahale.gtech.akoladirectory.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import gauravdahale.gtech.akoladirectory.ItemClickListener
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.activity.RecordActivity
import gauravdahale.gtech.akoladirectory.models.CallModel
import gauravdahale.gtech.akoladirectory.models.ContactModel
import java.text.NumberFormat
import java.util.*

class ShopListAdapter(private val context: Context) :
    ListAdapter<ContactModel, ShopHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHolder {
        return ShopHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ShopHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model, context) // Pass context to the bind method

        Glide.with(context)
            .load(model.i)
            .apply(RequestOptions().error(R.drawable.noimage))
            .into(holder.image)

        holder.setItemOnClickListener(object : ItemClickListener {
            override fun onItemClick(position: Int) {
                //  holder.increasecounter(ShopHolder.mDatabase.child("<span class="math-inline">\{model\.city\}/</span>{model.ref}"))
                val intent = Intent(context, RecordActivity::class.java)
                intent.putExtra("parcel", model)
                context.startActivity(intent)
            }
        })
    }

    companion object {
        var mDatabase = FirebaseDatabase.getInstance().reference
    }
}

class ShopHolder private constructor(itemView: View) :
    View.OnClickListener, RecyclerView.ViewHolder(itemView) {

    // Context
    private val context = itemView.context

    // UI elements
    private val shopname: TextView = itemView.findViewById(R.id.shoptitle)
    private val ratingtxt: TextView = itemView.findViewById(R.id.reviewint)
    private val reviewedby: TextView = itemView.findViewById(R.id.numbreview)
    private val ownername: TextView = itemView.findViewById(R.id.ownername)
    private val address: TextView = itemView.findViewById(R.id.addresstxtview)
    private val callBtn: TextView = itemView.findViewById(R.id.callbtn)
    private val description: TextView = itemView.findViewById(R.id.list_services)
    private val timing: TextView = itemView.findViewById(R.id.timingtxtview)
    private val views: TextView = itemView.findViewById(R.id.viewscounter)
    val image: ImageView = itemView.findViewById(R.id.list_contact_image)
    private val shareButton: ImageView = itemView.findViewById(R.id.fsharebutton)
    private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    private val counter: TextView = itemView.findViewById(R.id.viewscounter)

    // Click listener
    private lateinit var itemClickListener: ItemClickListener

    init {
        itemView.setOnClickListener(this)
    }

    // Bind data to the ViewHolder
    fun bind(model: ContactModel, context: Context) { // Added context parameter
        try {
            shopname.text = HtmlCompat.fromHtml(model.n ?: "", 0)
            address.text = HtmlCompat.fromHtml(model.a ?: "", 0)
            ownername.text = model.o
            description.text = HtmlCompat.fromHtml(model.d ?: "", 0)
            timing.text = model.t
            counter.text = model.c
        } catch (e: Exception) {
            // Handle the exception appropriately (e.g., log it)
        }

        callBtn.setOnClickListener {
            val phone = model.p.toString()
            val SEPARATOR = " "
            val mAnalytics = FirebaseAnalytics.getInstance(context)
            val mSettings = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val storedname = mSettings.getString("USER_NAME", "") ?: ""
            val storedphone = mSettings.getString("USER_NUMBER", "") ?: ""
            val shopname = model.n ?: ""

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, shopname)
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storedname)
            mAnalytics.logEvent("Call", bundle)

            val date = java.text.DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().time)

            logcall(shopname, storedname, storedphone, date)

            if (phone.contains(SEPARATOR)) {
                val parts = phone.split(SEPARATOR.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                calldialog(parts[0], parts[1], context) // Pass context to calldialog
            } else {
                val uri = "tel:$phone"
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(uri)
                context.startActivity(intent)
            }
        }

        val rating =
            mDatabase.child(model.city.toString()).child(model.ref.toString()).child("Ratings")
        getrating(rating)
    }

    // Fetch and display rating
    private fun getrating(rating: DatabaseReference) {
        val ref = rating
        rating.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var total = 0.0
                var count = 0.0
                var average = 0.0

                for (child in dataSnapshot.children) {
                    val rating =
                        (child.child("review").value ?: 0).toString().toDoubleOrNull() ?: 0.0
                    total += rating
                    count += 1
                    average = total / count
                }
                ratingBar.rating = average.toFloat()
                val formatter = NumberFormat.getNumberInstance()
                formatter.minimumFractionDigits = 1
                formatter.maximumFractionDigits = 1
                val ratingFormatted = formatter.format(average)
                reviewedby.text = "$count user reviews"
                ratingtxt.text = ratingFormatted

                ref.child("totalreviews")?.setValue(count.toInt().toString())
                ref.parent?.child("rating")?.setValue(ratingFormatted)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error appropriately (e.g., log it)
            }
        })
    }

    // Increase view counter (currently commented out)
//    fun increasecounter(postreference: DatabaseReference) {
//        // ... (Code for increasing counter) ...
//    }

    // Handle item click
    override fun onClick(v: View?) {
        itemClickListener.onItemClick(bindingAdapterPosition)
    }

    // Set item click listener
    fun setItemOnClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    companion object {
        var mDatabase = FirebaseDatabase.getInstance().reference

        // Inflate the item view
        fun from(parent: ViewGroup): ShopHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shoplist_neo, parent, false)
            return ShopHolder(view)
        }
    }

    // Log call details to Firebase
    private fun logcall(
        shoptitle: String,
        username: String?,
        userphone: String?,
        calldate: String
    ) {
        val call = CallModel(shoptitle, username, userphone, calldate)
        val databasereference = FirebaseDatabase.getInstance().reference
        databasereference.child("CallLog").child(shoptitle).push().setValue(call)
    }

    // Show call dialog
    private fun calldialog(
        phone1: String,
        phone2: String,
        context: Context
    ) { // Added context parameter
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.custom_call_dialog)

        val phonetxt1: TextView = dialog.findViewById(R.id.numberone)
        phonetxt1.text = phone1
        val phonetxt2: TextView = dialog.findViewById(R.id.numbertwo)
        phonetxt2.text = phone2

        phonetxt1.setOnClickListener {
            val uri = "tel:$phone1"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            context.startActivity(intent)
        }
        phonetxt2.setOnClickListener {
            val uri = "tel:$phone2"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            context.startActivity(intent)
        }

        dialog.show()
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<ContactModel>() {
    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.key == newItem.key
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.n == newItem.n

    }
}