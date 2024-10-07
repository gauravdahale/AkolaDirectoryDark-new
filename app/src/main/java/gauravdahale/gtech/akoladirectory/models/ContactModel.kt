package gauravdahale.gtech.akoladirectory.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
@IgnoreExtraProperties
class ContactModel : Serializable {
    var d: String? = null
    var p: String? = null
    var a: String? = null
    var i: String? = null
    var n: String? = null
    var t: String? = null
    var o: String? = null
    var otpnumber: String? = null
    var username: String? = null
    var ii: String? = null
    var iii: String? = null
    var iiii: String? = null
    var c: String? = null
    var key: String? = null
    var ref: String? = null
    var city: String? = null
    var rating: String? = null
    var totalreviews: String? = null
    var datetime: String? = null
    var uuid: String? = null
    var images: HashMap<String?, String?> = HashMap()  // Initialize the HashMap

    // Method to add images
    fun addImage(key: String, url: String) {
        images[key] = url
    }
}