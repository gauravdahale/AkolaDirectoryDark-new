package gauravdahale.gtech.akoladirectory.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import gauravdahale.gtech.akoladirectory.models.ContactModel


class ShopListLiveData : LiveData<ArrayList<ContactModel>?> {
    private val query: Query
    private val placeselected: String
    private val listener
    : MyValueEventListener = MyValueEventListener()
    val shoplist = ArrayList<ContactModel>()

    constructor(query: Query, place: String) {
        this.query = query
        this.placeselected = place
    }

    constructor(ref: DatabaseReference, place: String) {
        query = ref
        this.placeselected = place
    }

    override fun onActive() {
//        Log.d(LOG_TAG, "onActive $placeselected")
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
//        Log.d(LOG_TAG, "onInactive $placeselected")
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataSnapshot.children.forEach {
                try{
                    val model = it.getValue(ContactModel::class.java)
                    model?.key = it.key
                    model?.ref = dataSnapshot.key
                    model?.city = placeselected
                    shoplist.add(model!!)
                }
catch (e:Exception){
//    Log.e("SHOPLISTLIVEDATA", "onDataChange: " + { e.message.toString() })
}
            }.also { value = shoplist }


        }

        override fun onCancelled(databaseError: DatabaseError) {
//            Log.e(LOG_TAG, "Can't listen to query $query", databaseError.toException())
        }
    }

    companion object {
        private const val LOG_TAG = "FirebaseQueryLiveData"
    }
}
