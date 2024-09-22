package gauravdahale.gtech.akolaadmin

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

import java.util.HashMap

class RequestsFragment : Fragment() {
    internal var mRecyclerView: RecyclerView? = null
    internal var shrinkAnim: ScaleAnimation? = null
    private val tvNoMovies: ProgressBar? = null
    internal var editremark: String? = null
    internal var state: Parcelable? = null
    internal var layoutManager: LinearLayoutManager? = null
    internal var mAnalytics: FirebaseAnalytics? = null
    internal lateinit var adapter: FirebaseRecyclerAdapter<*, *>
    var LIST_STATE_KEY = "liststate"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        val query = FirebaseDatabase.getInstance()
            .reference.child("followup").child("NewShops")


        val options = FirebaseRecyclerOptions.Builder<RequestModel>()
            .setQuery(query, RequestModel::class.java)
            .build()
        adapter = object : FirebaseRecyclerAdapter<RequestModel, ItemViewHolder>(options) {


            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.requestcard, parent, false)
                return ItemViewHolder(view)
            }


            override fun onBindViewHolder(
                viewHolder: ItemViewHolder,
                position: Int,
                items: RequestModel
            ) {
                viewHolder.Name.text = items.n
                viewHolder.Address.text = items.a
                viewHolder.Phone.text = items.p
                viewHolder.Username.text = items.i
                viewHolder.Userphone.text = items.d
                viewHolder.createdat.text = items.t
                viewHolder.status.text = items.s


                viewHolder.sendbtn.setOnClickListener {
                    val rem = HashMap<String, String?>()
                    editremark = viewHolder.remark.text.toString().trim { it <= ' ' }
                    rem["s"] = editremark
                    getRef(position).updateChildren(rem as Map<String, Any>)
                    viewHolder.remark.text.clear()
                }
                viewHolder.delbtn.setOnClickListener { getRef(position).removeValue() }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_requests, container, false)

        mRecyclerView = v.findViewById<View>(R.id.request_recycler) as RecyclerView
        layoutManager = LinearLayoutManager(context)
        layoutManager?.reverseLayout = true
        layoutManager?.stackFromEnd = true

        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.adapter = adapter
        layoutManager?.onRestoreInstanceState(state)

        return v


    }

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var Name: TextView
        internal var Username: TextView
        internal var Address: TextView
        internal var Phone: TextView
        internal var Userphone: TextView
        internal var ntext: TextView
        internal var phtext: TextView
        internal var createdat: TextView
        internal var status: TextView
        internal var remark: EditText
        internal var sendbtn: ImageButton
        internal var delbtn: ImageButton

        init {
            this.Name = v.findViewById<View>(R.id.shopname) as TextView
            this.Address = v.findViewById<View>(R.id.shopaddress) as TextView
            this.Userphone = v.findViewById<View>(R.id.userphone) as TextView
            this.Username = v.findViewById<View>(R.id.username) as TextView
            this.Phone = v.findViewById<View>(R.id.shopphone) as TextView
            this.phtext = v.findViewById<View>(R.id.textView3) as TextView
            this.ntext = v.findViewById<View>(R.id.textView) as TextView
            this.status = v.findViewById<View>(R.id.status) as TextView
            this.remark = v.findViewById<View>(R.id.editeremark) as EditText
            this.createdat = v.findViewById(R.id.createdat)
            this.sendbtn = v.findViewById(R.id.sendbtn)
            this.delbtn = v.findViewById(R.id.deletebutton)
        }


    }

    override fun onPause() {
        super.onPause()
        state = layoutManager?.onSaveInstanceState()

    }

    override fun onStart() {
        super.onStart()

        adapter.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.stopListening()
    }

    override fun onStop() {

        super.onStop()
    }
}// Required empty public constructor