package gauravdahale.gtech.akoladirectory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import gauravdahale.gtech.akoladirectory.activity.MainActivityTabVersion
import gauravdahale.gtech.akoladirectory.models.UserModel

class UserForm : AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var UserName: TextInputEditText? = null
    private var UserOcccupation: TextInputEditText? = null
    private var UserNumber: TextInputEditText? = null
    private var UserCity: TextInputEditText? = null

    private var bSubmit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_form)


        UserName = findViewById<View>(R.id.userform_name) as TextInputEditText
        val userna = UserName!!.text.toString().trim { it <= ' ' }
        UserOcccupation = findViewById<View>(R.id.userform_occupation) as TextInputEditText
        UserNumber = findViewById<View>(R.id.userform_number) as TextInputEditText
        UserCity = findViewById<View>(R.id.userform_city) as TextInputEditText
        bSubmit = findViewById<View>(R.id.b_submit) as Button


        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        val mSettings = getSharedPreferences("USER_INFO", MODE_PRIVATE)

        val token = mSettings.getString("USER_TOKEN", "")

        bSubmit!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                when (v.id) {
                    R.id.b_submit -> if (!isEmpty(UserName) && !isEmpty(UserOcccupation) && !isEmpty(
                            UserNumber
                        ) && !isEmpty(UserCity)
                    ) {
                        NewShop(
                            UserName!!.text.toString().trim { it <= ' ' },
                            UserOcccupation!!.text.toString().trim { it <= ' ' },
                            UserNumber!!.text.toString().trim { it <= ' ' },
                            UserCity!!.text.toString().trim { it <= ' ' },
                            token
                        )
                    } else {
                        if (isEmpty(UserName)) {
                            Toast.makeText(
                                applicationContext,
                                "Please enter your Name!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (isEmpty(UserOcccupation)) {
                            Toast.makeText(
                                applicationContext,
                                "Please Enter Your Occupation",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (isEmpty(UserNumber)) {
                            Toast.makeText(
                                applicationContext,
                                "Please Enter Your Number",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (isEmpty(UserCity)) {
                            Toast.makeText(
                                applicationContext,
                                "Please Enter Your City",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }

            private fun NewShop(
                userName: String,
                userOccupation: String,
                userNumber: String,
                userCity: String,
                token: String?
            ) {
                //Creating a movie object with user defined variables
                val user = UserModel(userName, userOccupation, userNumber, userCity, token)
                //referring to movies node and setting the values from movie object to that location
                mDatabaseReference!!.child("users").push().setValue(user)

                val mAnalytics = FirebaseAnalytics.getInstance(this@UserForm)

                val bundle = Bundle()
                bundle.putString("NewUser", userna)
                mAnalytics.logEvent("NewUsers", bundle)


                // Get current version code
                val username = UserName!!.text.toString().trim { it <= ' ' }
                val userphone = UserNumber!!.text.toString().trim { it <= ' ' }

                // Get saved version code
                val prefs = getSharedPreferences("USER_INFO", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("USER_NAME", username)
                editor.putString("USER_NUMBER", userphone)
                editor.apply()

                backtomain()
            }
        })
    }

    private fun backtomain() {
        val i = Intent(applicationContext, MainActivityTabVersion::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }


    //check if edittext is empty
    private fun isEmpty(textInputEditText: TextInputEditText?): Boolean {
        if (textInputEditText!!.text.toString().trim { it <= ' ' }.isNotEmpty()) return false

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Please fill the form to continue!!!", Toast.LENGTH_LONG)
            .show()
    }
}