package gauravdahale.gtech.akoladirectory.fragments

import androidx.fragment.app.Fragment

/**
 * Created by Gaurav on 8/7/2018.
 */


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

import gauravdahale.gtech.akoladirectory.models.ContactModel
import gauravdahale.gtech.akoladirectory.ItemClickListener
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.activity.ShopListActivity

class FoodFragment : Fragment() {
    internal lateinit var mRecyclerView: RecyclerView
    internal var shrinkAnim: ScaleAnimation? = null
    private val tvNoMovies: ProgressBar? = null

    internal var mAnalytics: FirebaseAnalytics? = null
    internal lateinit var adapter: FirebaseRecyclerAdapter<*, *>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val query = FirebaseDatabase.getInstance()
                .reference.child("Category")
                .child("Food")

        val options = FirebaseRecyclerOptions.Builder<ContactModel>()
                .setQuery(query, ContactModel::class.java)
                .build()
        adapter = object : FirebaseRecyclerAdapter<ContactModel, ItemViewHolder>(options) {


            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.categoryview, parent, false)
                return ItemViewHolder(view)
            }


            override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int, items: ContactModel) {
                val address = items.n
                val title = items.d
                viewHolder.catname.text = items.c
                Picasso.with(activity).load(items.i).into(viewHolder.catimage)
                viewHolder.setItemOnClickListener(ItemClickListener {
                    val i = Intent(activity, ShopListActivity::class.java)
                    i.putExtra("Title", address)
                    i.putExtra("Settitle", title)
                    startActivity(i)
                })
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_construction, container, false)

        mRecyclerView = v.findViewById(R.id.construction_recycler) as RecyclerView
        val layoutManager = GridLayoutManager(context, 2)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(5), true))

        mRecyclerView.adapter = this.adapter


        return v


    }

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        internal lateinit var itemClickListener: ItemClickListener
        var catimage: ImageView
        var catname: TextView

        init {
            this.catimage = v.findViewById(R.id.categoryimage) as ImageView
            this.catname = v.findViewById(R.id.cattxtname) as TextView
            v.setOnClickListener(this)

        }

        fun setItemOnClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View) {
            this.itemClickListener.onItemClick(layoutPosition)
        }

    }

    //DP TO PX METHOD
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mRecyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onStop() {
        adapter.stopListening()
        super.onStop()
    }
}// Required empty public constructor