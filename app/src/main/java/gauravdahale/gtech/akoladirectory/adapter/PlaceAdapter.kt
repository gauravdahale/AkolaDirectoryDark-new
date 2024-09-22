package gauravdahale.gtech.akoladirectory.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.activity.DrawerActivity

class PlaceAdapter(val context: Context, val list: List<String>) : RecyclerView.Adapter<PlaceAdapter.PlaceHolder>() {

    class PlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val place: TextView

        init {
            place = itemView.findViewById(R.id.placetextview)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.PlaceHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.itemview_place, parent, false)
        return PlaceHolder(itemview)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlaceAdapter.PlaceHolder, position: Int) {
        val place = list.get(position)
        holder.place.text = place
        holder.place.setOnClickListener {
            val prefs = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("PLACE", place)
            editor.apply()
            Toast.makeText(context, "Selected place is $place", Toast.LENGTH_SHORT).show()
            (context as DrawerActivity).dialog.dismiss()
var placeselected= prefs.getString("PLACE","")
Log.d("onBindViewHolder","$placeselected")
        }
    }


}