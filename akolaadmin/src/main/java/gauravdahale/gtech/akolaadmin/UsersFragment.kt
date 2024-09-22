package gauravdahale.gtech.akolaadmin

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import gauravdahale.gtech.akolaadmin.adpters.UsersAdapter
import java.util.ArrayList

class UsersFragment : Fragment() {
    internal lateinit var mRecyclerView: RecyclerView
    internal var shrinkAnim: ScaleAnimation? = null
    private val tvNoMovies: ProgressBar? = null
    private val LIST_KEYS = "STATE"
    internal var mAnalytics: FirebaseAnalytics? = null
    internal lateinit var adapter: UsersAdapter
    internal lateinit var layoutManager: LinearLayoutManager
    internal lateinit var userModelList: MutableList<UserModel>
    private var mKeys: MutableList<String>? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val query = FirebaseDatabase.getInstance()
            .reference.child("users")
        userModelList = mutableListOf(UserModel())
        mKeys = mutableListOf()
        mKeys!!.clear()
        userModelList.clear()
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                updatelist(p0, p1)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                updatelist(p0, p1)

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                delete(p0)

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_users, container, false)
        adapter = UsersAdapter(userModelList)
        mRecyclerView = v.findViewById(R.id.users_recycler) as RecyclerView
        layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        mRecyclerView.layoutManager = layoutManager

        mRecyclerView.adapter = this.adapter


        return v


    }

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var name: TextView
        internal var Address: TextView? = null
        internal var Phone: TextView
        internal var Occu: TextView
        internal var City: TextView

        init {
            name = v.findViewById(R.id.user) as TextView
            Phone = v.findViewById(R.id.userphone) as TextView
            Occu = v.findViewById(R.id.userbusiness) as TextView
            City = v.findViewById(R.id.usercity) as TextView

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



    override fun onStart() {
        super.onStart()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    /*
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LIST_KEYS);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_KEYS, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }*/
    internal fun updatelist(dataSnapshot: DataSnapshot, s: String?) {
        //String key = dataSnapshot.getKey();
        //  String ref = "followup/NewShops/" + dataSnapshot.getKey();
        Log.d("updatelist", "" + dataSnapshot.key)

        val model = dataSnapshot.getValue(UserModel::class.java)
        // model.setRef(ref);
        val key = dataSnapshot.key

        // Insert into the correct location, based on previousChildName
        if (s == null) {
            userModelList.add(0, model!!)
            mKeys!!.add(0, key!!)
        } else {
            val previousIndex = mKeys!!.indexOf(s)
            val nextIndex = previousIndex + 1
            if (nextIndex == userModelList.size) {
                userModelList.add(model!!)
                mKeys!!.add(key!!)
            } else {
                userModelList.add(nextIndex, model!!)
                mKeys!!.add(nextIndex, key!!)
            }
        }


        // mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged()

    }


    internal fun delete(dataSnapshot: DataSnapshot) {
        val key = dataSnapshot.key
        val index = mKeys!!.indexOf(key)

        mKeys!!.removeAt(index)
        userModelList.removeAt(index)

        adapter.notifyDataSetChanged()
        //mRecyclerView.setAdapter(adapter);


    }

    }
