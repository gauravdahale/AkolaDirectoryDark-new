package gauravdahale.gtech.akoladirectory.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import gauravdahale.gtech.akoladirectory.models.CallModel
import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.activity.RecordActivity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ShopListAdapter(internal var context: Context, private val list: ArrayList<ContactModel>) : ListAdapter<ContactModel, ShopHolder>(DiffUtilCallback()) {
    var mDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHolder {
        return ShopHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ShopHolder, position: Int) {
        var model = getItem(position) as ContactModel
        holder.bind(model)
        Glide.with(context).load(model.i).apply(RequestOptions().error(R.drawable.noimage)).into(holder.image)

        holder.setItemOnClickListener(ItemClickListener {

          //  holder.increasecounter(mDatabase.child("${model.city}/${model.ref}"))

            val intent = Intent(context, RecordActivity::class.java)
            intent.putExtra("parcel", model)
            context.startActivity(intent)



        })

    }
companion object{
    var mDatabase = FirebaseDatabase.getInstance().reference
}
}


class ShopHolder private constructor(itemView: View) :
        View.OnClickListener, RecyclerView.ViewHolder(itemView) {

    val context = itemView.context
    internal val shopname: TextView
    internal val ownername: TextView
    internal val address: TextView
    internal val callBtn: TextView
    internal val description: TextView
    internal val review: TextView
    internal val reviewcount: TextView
    internal val views: TextView
    internal val timing: TextView
    internal val shareButton: ImageView
    internal var image: ImageView
    internal var container: View
    internal var ratingBar: RatingBar
    internal lateinit var itemClickListener: ItemClickListener
    internal lateinit var reviewedby: TextView
    internal lateinit var ratingtxt: TextView
    internal lateinit var counter: TextView

    init {

        this.container = itemView.findViewById(R.id.rootcontactview)
        shopname = itemView.findViewById(R.id.shoptitle)
        ratingtxt = itemView.findViewById(R.id.reviewint)
        reviewedby = itemView.findViewById(R.id.numbreview)
        ownername = itemView.findViewById(R.id.ownername)
        address = itemView.findViewById(R.id.addresstxtview)
        callBtn = itemView.findViewById(R.id.callbtn)
        description = itemView.findViewById(R.id.list_services)
        review = itemView.findViewById(R.id.numbreview)
        reviewcount = itemView.findViewById(R.id.reviewint)
        timing = itemView.findViewById(R.id.timingtxtview)
        views = itemView.findViewById(R.id.viewscounter)
        image = itemView.findViewById(R.id.list_contact_image)
        shareButton = itemView.findViewById(R.id.fsharebutton)
        ratingBar = itemView.findViewById(R.id.ratingBar)
        counter = itemView.findViewById(R.id.viewscounter)
        itemView.setOnClickListener(this)

    }

    fun bind(model: ContactModel) {
        var phone: String? = null
        try {
            model.n.let { shopname.text = HtmlCompat.fromHtml(it!!, 0) }
            model.a.let { address.text = HtmlCompat.fromHtml(it!!, 0) }
            model.o.let { ownername.text = it }
            model.d.let { description.text = HtmlCompat.fromHtml(it!!, 0) }
            model.t.let { timing.text = it }
            phone = model.p.toString()
            model.c.let { counter.text = model.c }
        } catch (e: Exception) {
            Log.e("Error in bindview:", e.message.toString())
        }
        callBtn.setOnClickListener {
            phone.let { it ->
                val SEPARATOR = " "
                val n1: String
                val n2: String
                val mAnalytics = FirebaseAnalytics.getInstance(context)
                val mSettings = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                val storedname = mSettings.getString("USER_NAME", "")
                val storedphone = mSettings.getString("USER_NUMBER", "")
                val bundle = Bundle()
                val shopname = model.n
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.n)
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storedname)
                mAnalytics.logEvent("Call", bundle)
                val date = java.text.DateFormat.getDateTimeInstance()
                        .format(Calendar.getInstance().time)
                //Toast.makeText(getApplicationContext(),"Tap again to dial",Toast.LENGTH_LONG).show();
                logcall(shopname!!, storedname, storedphone, date)

                if (it!!.contains(SEPARATOR)) {
                    val parts = phone?.split(SEPARATOR.toRegex())!!.dropLastWhile { it.isEmpty() }
                            .toTypedArray()

                    n1 = parts[0]
                    n2 = parts[1]
                    calldialog(n1, n2)
                } else {
                    val uri = "tel:$phone"
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse(uri)
                    context.startActivity(intent)
                }
            }
//            shareButton.setOnClickListener {
//                (context as ShopListActivity).postPicture(shareButton, container)
//                Log.d("Share Button", "Share button Clicked")
//            }

        }

        val rating = mDatabase.child(model.city.toString()).child(model.ref.toString()).child("Ratings")
        //getrating(rating)

    }

    fun getrating(rating: DatabaseReference) {
val ref =rating
        rating.addListenerForSingleValueEvent(object : ValueEventListener {
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
                ratingBar.rating = average.toFloat()
                val formatter =
                        NumberFormat.getNumberInstance()
                formatter.minimumFractionDigits = 1
                formatter.maximumFractionDigits = 1
                //                            getInstance(context.resources.configuration.locale)
                //                          .format(average)
                val rating = formatter.format(average)
                reviewedby.text = count.toInt().toString() + " user reviews"
                ratingtxt.text = rating
            dataSnapshot.childrenCount
ref.child("totalreviews")?.setValue(count.toInt().toString())
           ref.parent?.child("rating")?.setValue(rating)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
    fun increasecounter(postreference:DatabaseReference){
        postreference.child("c").runTransaction(object : Transaction.Handler {
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

    }

    override fun onClick(p0: View?) {
        itemClickListener.onItemClick(layoutPosition)
    }

    fun setItemOnClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    companion object {
        var mDatabase = FirebaseDatabase.getInstance().reference
        fun from(parent: ViewGroup): ShopHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shoplist_neo, parent, false)
            return ShopHolder(view)

        }
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
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
//dialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_call_dialog)

        phonetxt1 = dialog.findViewById(R.id.numberone)
        phonetxt1.text = phone1
        phonetxt2 = dialog.findViewById(R.id.numbertwo)
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

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.equals(newItem)
    }

}
