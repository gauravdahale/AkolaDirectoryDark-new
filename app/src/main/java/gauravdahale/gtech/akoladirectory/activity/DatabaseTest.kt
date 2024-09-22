package gauravdahale.gtech.akoladirectory.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.UserModel

class DatabaseTest : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_test)
        val userRef = database.reference.child("testusers").limitToLast(20)
        val userRefs = database.reference.child("testusers")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val userlist = ArrayList<UserModel>()
                for (child in p0.children) {
                    var user = child.getValue(UserModel::class.java)!!
                    userlist.add(user)
                    val datetime = user.datetime
                    Log.d("onDataChange", "${user.userName} = ${child.key}")

                    userRefs.child(child.key.toString()).child("date").setValue(datetime).addOnSuccessListener {
                        Log.d("Data added succsesfully","msg")
                    }
                            .addOnFailureListener {
                                Log.d("onDataChange","Data operation failed")
                            }
                }
                Log.d("list size", "${userlist.size}")

            }
        })
    }
}
