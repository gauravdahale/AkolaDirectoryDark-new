package gauravdahale.gtech.akoladirectory.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.databinding.VerifyphoneBinding
import gauravdahale.gtech.akoladirectory.models.UserModel
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {

    private var name: String? = null
    private var occupation: String? = null
    private var city: String? = null
    private var mobile: String? = null
    private var token: String? = null
    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }
    private lateinit var binding: VerifyphoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = VerifyphoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get user information from SharedPreferences
        val settings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        token = settings.getString("USER_TOKEN", "")

        // Get data from intent
        intent?.let {
            mobile = it.getStringExtra("mobile")
            name = it.getStringExtra("username")
            occupation = it.getStringExtra("useroccupation")
            city = it.getStringExtra("usercity")
        }

        Toast.makeText(this, "Mobile Number: $mobile", Toast.LENGTH_SHORT).show()
        sendVerificationCode(mobile)

        binding.resendbtn.setOnClickListener { resendVerificationCode() }
        binding.buttonSignIn.setOnClickListener { verifyCode() }
    }

    private fun sendVerificationCode(mobile: String?) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$mobile")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode() {
        mobile?.let {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91$it")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .setForceResendingToken(resendToken!!)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            Toast.makeText(this, "OTP has been sent again", Toast.LENGTH_LONG).show()
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            credential.smsCode?.let {
                binding.editTextCode.setText(it)
                verifyVerificationCode(it)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            val message = when (e) {
                is FirebaseAuthInvalidCredentialsException -> "Invalid request"
                is FirebaseTooManyRequestsException -> "The SMS quota is reached"
                else -> e.message
            }
            Snackbar.make(binding.root, message ?: "Verification failed", Snackbar.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            this@VerifyPhoneActivity.verificationId = verificationId
            resendToken = token
        }
    }

    private fun verifyCode() {
        val code = binding.editTextCode.text.toString().trim()
        if (code.isEmpty() || code.length < 6) {
            binding.editTextCode.error = "Enter valid code"
            binding.editTextCode.requestFocus()
            return
        }
        verifyVerificationCode(code)
    }

    private fun verifyVerificationCode(code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            Log.d(TAG, "verifyVerificationCode: ${e.localizedMessage}")
            Snackbar.make(binding.root, e.localizedMessage ?: "Error occurred", Snackbar.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                addUserToDatabase()
                Snackbar.make(binding.root, "Login Successful", Snackbar.LENGTH_LONG).show()
                Intent(this, DrawerActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(this)
                }
                finish()
            } else {
                val message = if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    "Invalid code entered..."
                } else {
                    "Something went wrong, please try again later."
                }
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).setAction("Dismiss") {}.show()
            }
        }
    }

    private fun addUserToDatabase() {
        val datetime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        val user = UserModel().apply {
            userName = name?.trim() ?: ""
            userOccupation = occupation?.trim() ?: ""
            userNumber = mobile?.trim() ?: ""
            userCity = city?.trim() ?: ""
            date = datetime
            token = this@VerifyPhoneActivity.token
        }

        auth.currentUser?.uid?.let { uid ->
            databaseReference.child("users").push().setValue(user)
            databaseReference.child("USERS").child(uid).setValue(user)
        }

        FirebaseAnalytics.getInstance(this).logEvent("NewUsers", Bundle().apply {
            putString("NewUser", user.userName)
        })

        getSharedPreferences("USER_INFO", Context.MODE_PRIVATE).edit().apply {
            putString("USER_NAME", user.userName)
            putString("USER_NUMBER", user.userNumber)
            apply()
        }
    }

    companion object {
        private const val TAG = "VerifyPhoneActivity"
    }
}
