package gauravdahale.gtech.akoladirectory.fragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import gauravdahale.gtech.akoladirectory.ChangeFragment
import gauravdahale.gtech.akoladirectory.ItemClickListener
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.activity.*
import gauravdahale.gtech.akoladirectory.activity.OffersActivity
import gauravdahale.gtech.akoladirectory.data.CategoryModel
import gauravdahale.gtech.akoladirectory.data.OfferModel
import gauravdahale.gtech.akoladirectory.models.ContactModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    lateinit var adapter: FirebaseRecyclerAdapter<*, *>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerview = v.findViewById<RecyclerView>(R.id.home_recyclerview)
        val prefs: SharedPreferences = activity!!.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        Log.d("PLACE ON START", prefs.getString("PLACE", ""))
        if(prefs.getString("PLACE", "")==null){
            val editor = prefs.edit()
            editor.putString("PLACE", "Akola")
            editor.apply()
            Toast.makeText(activity, "Akola City Selected", Toast.LENGTH_SHORT).show()

        }
        val query = FirebaseDatabase.getInstance()
                .getReference("Sections")

        val options = FirebaseRecyclerOptions.Builder<CategoryModel>().setLifecycleOwner(this)
                .setQuery(query, CategoryModel::class.java).build()

        adapter = object : FirebaseRecyclerAdapter<CategoryModel, CategoryHolder>(options) {
            override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
            ): CategoryHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.cardview_category_recyclerview, parent, false)
                return CategoryHolder(view)

            }

            override fun onBindViewHolder(holder: CategoryHolder, pos: Int, model: CategoryModel) {
                holder.heading.text = model.n

                val reference = "Category/" + model.r
                val context = activity
                if (reference != null) {

                    val currentadapter = setupadap(reference, activity!!)
                    val horizontallayout =
                            LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
                    holder.innerRecyclerView?.setLayoutManager(horizontallayout)
                    //holder.horizontalrecyclerview.setHasFixedSize(true);
                    holder.innerRecyclerView?.setAdapter(currentadapter)

                }
                holder.view_All.setOnClickListener {
                    when (model.n) {
                        "Health" -> (activity as DrawerActivity).loadfragment(HealthFragment(), "HealthFragment")
                        "Services" -> (activity as DrawerActivity).loadfragment(ServicesFragment(), "Services")
                        "Education" -> (activity as DrawerActivity).loadfragment(EducationFragment(), "Education")
                        "Construction" -> (activity as DrawerActivity).loadfragment(ConstructionFragment(), "Construction")
                        "Emergency" -> (activity as DrawerActivity).loadfragment(EmergencyFragment(), "Emergency")
                        "Wedding" -> (activity as DrawerActivity).loadfragment(WeddingFragment(), "Wedding")
                        "HomeServices" -> (activity as DrawerActivity).loadfragment(HomeServicesFragment(), "HomeServices")
                        "Food" -> (activity as DrawerActivity).loadfragment(FoodFragment(), "Food")
                        "Shopping" -> (activity as DrawerActivity).loadfragment(ShoppingFragment(), "Shopping")
                        "Automobiles" -> (activity as DrawerActivity).loadfragment(AutomobilesFragment(), "Automobiles")

                    }

                }

            }


        }
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = adapter
        setupofferslider(v)

        return v
    }

    private fun setupofferslider(view: View?) {
        val bannersRef = FirebaseDatabase.getInstance().getReference("Offers")
        val mDemoSlider = view?.findViewById(R.id.offer_slider) as SliderLayout
        GlobalScope.launch {
            bannersRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        val post = child.getValue(OfferModel::class.java)
                        val textSliderView = TextSliderView(activity)
                        // initialize a SliderLayout
                        textSliderView
                                .image(post!!.oi)
                                .description(post.od)
                                .setOnSliderClickListener {
                                    activity!!.startActivity(Intent(activity!!, OffersActivity::class.java))

//                                showBottomSheetDialog(post)
                                }.scaleType = BaseSliderView.ScaleType.Fit

                        mDemoSlider.addSlider(textSliderView)

                    }
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion)
                    mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible)
                    mDemoSlider.setCustomAnimation(DescriptionAnimation())
                    //mDemoSlider.setCurrentPosition(1);
                    mDemoSlider.setDuration(4000)

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })


        }
    }



    class CategoryHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        internal var itemClickListener: ItemClickListener? = null
        internal var heading: TextView
        internal var view_All: TextView
        internal var innerRecyclerView: RecyclerView? = null

        init {
            heading = v.findViewById(R.id.cat_heading)
            innerRecyclerView = v.findViewById(R.id.innerrecycleview)
            view_All = v.findViewById(R.id.cat_viewall)

//v.setOnClickListener (this)

        }


        override fun onClick(p0: View?) {
            itemClickListener?.onItemClick(layoutPosition)

        }

        fun setItemOnClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }
    }

    fun setupadap(adapterpath: String, context: Context): FirebaseRecyclerAdapter<*, *> {
        //Code for Horizontal layout recyclerview
        val query = FirebaseDatabase.getInstance()
                .reference.child(adapterpath)

        //Set options
        val options1 = FirebaseRecyclerOptions.Builder<ContactModel>().setLifecycleOwner(this)
                .setQuery(query, ContactModel::class.java).build()


        adapter = object : FirebaseRecyclerAdapter<ContactModel, InnerHolder>(options1) {
            @NonNull
            override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): InnerHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.cardview_categoryitem, parent, false)
                return InnerHolder(view)
            }

            override fun onBindViewHolder(
                    @NonNull holder: InnerHolder, position: Int, @NonNull
                    model: ContactModel
            ) {

                //Setup Card
                holder.categoryheading.text = model.c
                holder.categoryheading.isSelected = true
                Glide.with(context.applicationContext).load(model.i).into(holder.categoryimage)

                holder.setItemOnClickListener(ItemClickListener {
                    val bundle = Bundle()
                    //        listener?.inflateFragment("Recordlist", model.n!!, bundle)


                    try {

                        if (model.t.equals("DivyaHindi")) {
                            var intent = Intent(activity, ShopListActivity::class.java)

                            intent = Intent(activity, NewsActivity::class.java)
                            intent.putExtra("path", "Akola/divyahindi")
                       startActivity(intent)
                        } else
                            if (model.t.equals("e")) {
                                var intent = Intent(activity, ShopListActivity::class.java)

                                intent = Intent(activity, EmergencyActivity::class.java)
                                intent.putExtra("Title", model.n)
                                intent.putExtra("Settitle", model.c)

                                startActivity(intent)
                            } else if (model.t.equals("Link")) {
                                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.n))
                                startActivity(myIntent)
                            }
                        else
                            {
                                var intent = Intent(activity, ShopListActivity::class.java)

                                intent.putExtra("Title", model.n)
                                intent.putExtra("Settitle", model.c)

                                startActivity(intent)
                            }
                    } catch (e: NullPointerException) {
                        Log.d("Exception", "${e.message}")
                    }
                })

            }


        }
        return adapter
    }

    class InnerHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        internal var itemClickListener: ItemClickListener? = null
        internal lateinit var categoryheading: TextView
        internal var categoryimage: ImageView

        init {
            categoryheading = v.findViewById(R.id.cat_name)
            categoryimage = v.findViewById(R.id.cat_image)

            v.setOnClickListener(this)

        }


        override fun onClick(p0: View?) {
            itemClickListener?.onItemClick(layoutPosition)

        }

        fun setItemOnClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as DrawerActivity
        val TAG = "MainFragment"
    }

    companion object {
        var listener: ChangeFragment? = null

    }
}
