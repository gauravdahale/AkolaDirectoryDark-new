package gauravdahale.gtech.akoladirectory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import gauravdahale.gtech.akoladirectory.livedata.ShopListLiveData
import gauravdahale.gtech.akoladirectory.models.ContactModel


class ShopListViewModel(url: String,placeselected: String) : ViewModel() {

private val ref = FirebaseDatabase.getInstance().getReference(url)
    private val liveData: ShopListLiveData = ShopListLiveData(ref,placeselected)


    fun getDataSnapshotLiveData(): LiveData<ArrayList<ContactModel>?> {
        return liveData
    }
}


class ShopListViewModelFactory constructor(private val url: String,private  val placeselected:String): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ShopListViewModel::class.java)) {
            ShopListViewModel(this.url,placeselected) as T
        }
        else {

            throw IllegalArgumentException("ViewModel Not Found")

        }
    }
}