package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.UserModel
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity() : AppCompatActivity() {
    var name: String? = null
    var occupation: String? = null
    var city: String? = null
    var mobile: String? = null
    var token: String? = null
    //These are the objects needed
//It is the verification id that will be sent to the user
    private var mVerificationId: String? = null
    private var mDatabaseReference: DatabaseReference? = null
    //The edittext to input the code
    private var editTextCode: EditText? = null
    lateinit var resendbutton: Button
    lateinit var  mView:RelativeLayout
    private var mSubmitButton: Button? = null
    private var mAuth= FirebaseAuth.getInstance()
    private var mResendToken: ForceResendingToken? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verifyphone)
        //initializing objects
        resendbutton = findViewById(R.id.resendbtn)
        mSubmitButton = findViewById<Button>(R.id.buttonSignIn)
        mAuth = FirebaseAuth.getInstance()
        editTextCode = findViewById(R.id.editTextCode)
        mView = findViewById<RelativeLayout>(R.id.id_verify_phone)
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        token = mSettings.getString("USER_TOKEN", "")
        //getting mobile number from the previous activity
        //and sending the verification code to the number
        val intent = intent
        mobile = intent.getStringExtra("mobile")
        name = intent.getStringExtra("username")
        occupation = intent.getStringExtra("useroccupation")
        city = intent.getStringExtra("usercity")
        Toast.makeText(this, " MObile Nmber : $mobile", Toast.LENGTH_SHORT).show()
        sendVerificationCode(mobile)
        resendbutton.setOnClickListener({ resendVerificationCode(mobile, mResendToken) })
        //if the automatic sms detection did not work, user can also enter the code manually
//so adding a click listener to the button
        mSubmitButton?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val code = editTextCode?.getText().toString().trim { it <= ' ' }
                if (code.isEmpty() || code.length < 6) {
                    editTextCode?.setError("Enter valid code")
                    editTextCode?.requestFocus()
                    return
                }
                //verifying the code entered manually
                verifyVerificationCode(code)
            }
        })
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private fun sendVerificationCode(mobile: String?) {

val options = PhoneAuthOptions.newBuilder(mAuth)
        .setPhoneNumber("+91"+mobile)
        .setTimeout(60L,TimeUnit.SECONDS)
        .setActivity(this)
        .setCallbacks(mCallbacks)
        .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun resendVerificationCode(mobile: String?,
                                       mResendToken: ForceResendingToken?) {

        val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91"+mobile)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91$mobile",  // Phone number to verify
                30,  // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                this,  // Activity (for callback binding)
                mCallbacks,  // OnVerificationStateChangedCallbacks
                mResendToken) // ForceResendingToken from callbacks
        Toast.makeText(this, "OTP has been sent again", Toast.LENGTH_LONG).show()
    }

    //the callback to detect the verification status
    private val mCallbacks: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) { //Getting the code sent by SMS
            val code = phoneAuthCredential.smsCode
            //sometime the code is not detected automatically
//in this case the code will be null
//so user has to manually enter the code
            if (code != null) {
                editTextCode!!.setText(code)
                //verifying the code
                verifyVerificationCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@VerifyPhoneActivity, e.message, Toast.LENGTH_LONG).show()
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
              val snackBar =   make(mView,"The SMS Quota is reached ", LENGTH_SHORT)
                snackBar.show()

            }

        }

        override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            //storing the verification id that is sent to the user
            mVerificationId = s
            mResendToken = forceResendingToken
        }
    }

    private fun verifyVerificationCode(code: String) { //creating the credential
//signing the user
        try {
            val credential = PhoneAuthProvider.getCredential((mVerificationId)!!, code)
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {

          val snackbar =  make(mView, e.localizedMessage, LENGTH_SHORT)
            snackbar.show()
            Log.d(TAG, "verifyVerificationCode" + e.localizedMessage)
            // startActivity( new Intent(VerifyPhoneActivity.this,DrawerActivity.class));
            finish()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this@VerifyPhoneActivity) { task ->
                    if (task.isSuccessful) { //verification successful we will start the profile activity
                        //User Data Feeding to database.
                        val tags = JSONObject()
                        try {
                            tags.put("Id", token)
                            tags.put("Name", name)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
//                        OneSignal.sendTags(tags)
                        val datetime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
                        NewUser(name!!.trim { it <= ' ' },
                                occupation!!.trim { it <= ' ' },
                                mobile!!.trim { it <= ' ' },
                                city!!.trim { it <= ' ' },
                                token,
                                datetime)
                        val intent = Intent(this@VerifyPhoneActivity, DrawerActivity::class.java)
                val snackbar = make(mView,"Login Successful", LENGTH_LONG)
                        snackbar.show()
                        //Toast.makeText(applicationContext, "Login Successfull", Toast.LENGTH_SHORT).show()
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    } else { //verification unsuccessful.. display an error message
                        var message = "Something is wrong, we will fix it soon..."
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered..."
                        }
                        val snackbar = make(findViewById(R.id.id_verify_phone), (message), LENGTH_LONG)
                        snackbar.setAction("Dismiss") { }
                        snackbar.show()
                    }
                }
    }

    private fun NewUser(newuserName: String, newuserOccupation: String, newuserNumber: String, newuserCity: String, newtoken: String?, newdatetime: String) { //Creating a movie object with user defined variables
        val user = UserModel()
        user.userName = newuserName
        user.userOccupation = newuserOccupation
        user.userNumber = newuserNumber
        user.userCity = newuserCity
        user.date = newdatetime
        user.token = newtoken

        //referring to movies node and setting the values from movie object to that location

        mDatabaseReference!!.child("users").push().setValue(user)
        mDatabaseReference!!.child("USERS").child(mAuth!!.currentUser!!.uid).setValue(user)
        val mAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("NewUser", newuserName)
        mAnalytics.logEvent("NewUsers", bundle)
        // Get current version code
        val username = newuserName.trim { it <= ' ' }
        val userphone = newuserNumber.trim { it <= ' ' }
        // Get saved version code
        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("USER_NAME", username)
        editor.putString("USER_NUMBER", userphone)
        editor.apply()
    }

    companion object {
        private val TAG = "VerifyPhoneActivity"
    }
}