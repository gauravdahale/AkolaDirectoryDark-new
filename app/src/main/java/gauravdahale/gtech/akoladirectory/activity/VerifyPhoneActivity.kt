package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.widget.EditText
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import org.json.JSONObject
import org.json.JSONException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import gauravdahale.gtech.akoladirectory.databinding.VerifyphoneBinding
import gauravdahale.gtech.akoladirectory.models.UserModel
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

class VerifyPhoneActivity : AppCompatActivity() {

    private companion object {
        private const val TAG = "VerifyPhoneActivity"
        private const val RESEND_TIMEOUT_SECONDS = 60
    }

    private var name: String? = null
    private var occupation: String? = null
    private var city: String? = null
    private var mobile: String? = null
    private var token: String? = null

    private var mVerificationId: String? = null
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var binding: VerifyphoneBinding
    private lateinit var mAuth: FirebaseAuth
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use View Binding
        binding = VerifyphoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize other components
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        // Retrieve user data from previous activity
        val intent = intent
        name = intent.getStringExtra("username")
        occupation = intent.getStringExtra("useroccupation")
        city = intent.getStringExtra("usercity")
        mobile = intent.getStringExtra("mobile")

        // Display user's mobile number for confirmation
        Toast.makeText(this, "Mobile Number: $mobile", Toast.LENGTH_SHORT).show()

        // Send verification code
        sendVerificationCode(mobile)

        // Resend verification code on button click
        binding.resendbtn.setOnClickListener {
            resendVerificationCode(mobile, mResendToken)
        }

        // Verify code entered manually on button click
        binding.buttonSignIn.setOnClickListener {
            val code = binding.editTextCode.text.toString().trim()
            if (code.isEmpty() || code.length < 6) {
                binding.editTextCode.error = "Enter valid code"
                binding.editTextCode.requestFocus()
                return@setOnClickListener
            }
            verifyVerificationCode(code)
        }
    }

    private fun sendVerificationCode(mobile: String?) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91$mobile")
            .setTimeout(RESEND_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d(TAG, "sendVerificationCode:Called ")
    }

    private fun resendVerificationCode(mobile: String?, token: PhoneAuthProvider.ForceResendingToken?) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91$mobile")
            .setTimeout(RESEND_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallbacks)
            .build()

        if (token != null) {
            Log.d(TAG, "resendVerificationCode: ")
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91$mobile", RESEND_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS, this, mCallbacks, token
            )
        } else {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        Toast.makeText(this, "OTP has been sent again to $mobile", Toast.LENGTH_LONG).show()
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            val code = credential.smsCode
            if (code != null) {
                binding.editTextCode.setText(code)
                verifyVerificationCode(code)
                Log.d(TAG, "onVerificationCompleted: ")
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@VerifyPhoneActivity, e.message, Toast.LENGTH_LONG).show()
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                val snackBar = Snackbar.make(binding.idVerifyPhone, "The SMS Quota is reached", Snackbar.LENGTH_SHORT)
                snackBar.show()
            }
        }

        override fun onCodeSent(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken)
            mVerificationId = verificationId
            mResendToken = forceResendingToken
            Log.d(TAG, "onCodeSent: ")
        }
    }

    private fun verifyVerificationCode(code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
            signInWithPhoneAuthCredential(credential)
            Log.d(TAG, "verifyVerificationCode: ")
        } catch (e: Exception) {
            val snackBar = Snackbar.make(binding.idVerifyPhone, e.localizedMessage, Snackbar.LENGTH_SHORT)
            snackBar.show()
            Log.d(TAG, "verifyVerificationCode" + e.localizedMessage)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Log.d(TAG, "signInWithPhoneAuthCredential: ")
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User Data Feeding to database.
                    val tags = JSONObject()
                    try {
                        tags.put("Id", token)
                        tags.put("Name", name)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    // OneSignal.sendTags(tags)
                    val datetime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
                    NewUser(name!!.trim(),
                        occupation!!.trim(),
                        mobile!!.trim(),
                        city!!.trim(),
                        token,
                        datetime)
                    val intent = Intent(this@VerifyPhoneActivity, DrawerActivity::class.java)
                    val snackbar = Snackbar.make(binding.idVerifyPhone, "Login Successful", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
                    var message = "Something is wrong, we will fix it soon..."
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                    }
                    val snackbar = Snackbar.make(binding.idVerifyPhone, message, Snackbar.LENGTH_SHORT)
                    snackbar.setAction("Dismiss") {}
                    snackbar.show()
                }
            }
    }

    private fun NewUser(newuserName: String, newuserOccupation: String, newuserNumber: String, newuserCity: String, newtoken: String?, newdatetime: String) {
        val user = UserModel()
        user.userName = newuserName
        user.userOccupation = newuserOccupation
        user.userNumber = newuserNumber
        user.userCity = newuserCity
        user.date = newdatetime
        user.token = newtoken

        mDatabaseReference!!.child("users").push().setValue(user)
        mDatabaseReference!!.child("USERS").child(mAuth!!.currentUser!!.uid).setValue(user)
        val mAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("NewUser", newuserName)
        mAnalytics.logEvent("NewUsers", bundle)

        val username = newuserName.trim()
        val userphone = newuserNumber.trim()
        val prefs = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("USER_NAME", username)
        editor.putString("USER_NUMBER", userphone)
        editor.apply()
    }
}