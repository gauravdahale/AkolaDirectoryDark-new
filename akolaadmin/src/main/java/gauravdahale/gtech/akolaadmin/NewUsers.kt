package gauravdahale.gtech.akolaadmin

import android.app.DownloadManager
import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.FirebaseError
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewUsers : Fragment() {
    internal var mRecyclerView: RecyclerView?=null
    internal var shrinkAnim: ScaleAnimation? = null
    private val tvNoMovies: ProgressBar? = null
    internal var state: Parcelable? = null
    internal var layoutManager: LinearLayoutManager?=null
    internal var mAnalytics: FirebaseAnalytics? = null
    internal var adapter: FirebaseRecyclerAdapter<*, *>?=null
    var LIST_STATE_KEY="liststate"
    private val TAG = "UsersFragement"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
        val query = FirebaseDatabase.getInstance()
            .reference.child("USERS")


        val options = FirebaseRecyclerOptions.Builder<UserModel>()
            .setQuery(query, UserModel::class.java)
            .build()
        adapter = object : FirebaseRecyclerAdapter<UserModel, ItemViewHolder>(options) {


            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.usercardview, parent, false)
                return ItemViewHolder(view)
            }


            override fun onBindViewHolder(
                viewHolder: ItemViewHolder,
                position: Int,
                items: UserModel
            ) {
                viewHolder.name.text = items.userName
                viewHolder.Phone.text = items.userNumber
                viewHolder.Occu.text = items.userOccupation
                viewHolder.City.text = items.userCity
                viewHolder.Date.text = items.date
                viewHolder.sr.text = position.toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_users, container, false)

        mRecyclerView = v.findViewById<View>(R.id.users_recycler) as RecyclerView
        layoutManager = LinearLayoutManager(activity)
        layoutManager?.reverseLayout = true
        layoutManager?.stackFromEnd = true

        mRecyclerView?.layoutManager = layoutManager
        layoutManager?.onRestoreInstanceState(state)
        mRecyclerView?.adapter = this.adapter
        restoreLayoutManagerPosition();

        return v


    }

    private fun restoreLayoutManagerPosition() {
        if (state != null) {
            mRecyclerView?.getLayoutManager()?.onRestoreInstanceState(state);
        }
    }

    class   ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var name: TextView
        internal var Address: TextView? = null
        internal var Phone: TextView
        internal var Occu: TextView
        internal var City: TextView
        internal var Date: TextView
        internal var sr: TextView
        init {
            name = v.findViewById<View>(R.id.user) as TextView
            Phone = v.findViewById<View>(R.id.userphone) as TextView
            Occu = v.findViewById<View>(R.id.userbusiness) as TextView
            City = v.findViewById<View>(R.id.usercity) as TextView
            Date = v.findViewById(R.id.installdateview)
            sr =v.findViewById(R.id.usersr) as TextView
        }


    }

    //DP TO PX METHOD
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }

    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing /
                        spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing /
                        spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing /
                        spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //  mRecyclerView?.adapter = adapter
        adapter?.startListening()
    }

    override fun onStop() {
         adapter?.stopListening()


        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        state = layoutManager?.onSaveInstanceState()
       // Toast.makeText(context,"On  Pause called", Toast.LENGTH_LONG).show()
        retainInstance(true)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }



    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState!=null){
            state=savedInstanceState.getParcelable(LIST_STATE_KEY)
            layoutManager
     //       Toast.makeText(context,"On  ViewRestored called", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.stopListening()
    }

    override fun onResume() {
        super.onResume()
     //   Toast.makeText(context,"On  Resume called", Toast.LENGTH_LONG).show()

    }
}// Required empty public constructor

private operator fun Boolean.invoke(b: Boolean) {

}
