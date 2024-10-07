package gauravdahale.gtech.akoladirectory.activity

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import gauravdahale.gtech.akoladirectory.databinding.ActivityRegisterBinding
import gauravdahale.gtech.akoladirectory.models.ContactModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

class NewRegisterActivity : AppCompatActivity() {
    private val TAG = "NewRegisterActivity"
    private lateinit var binding: ActivityRegisterBinding
    private val mStorage = FirebaseStorage.getInstance().getReference("requests")
    private val mReference = FirebaseDatabase.getInstance().getReference("NewRequests")
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var datetime: String
    private lateinit var storedPhone: String
    private lateinit var storedName: String
    private val imagelist = ArrayList<Uri>()

    // Launcher for the Image Picker
    private lateinit var pickImagesLauncher: ActivityResultLauncher<String>
    private lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = ProgressDialog(this)
        dialog.setMessage("Uploading images...")
        dialog.setCancelable(false) // Prevent the dialog from being dismissed by tapping outside

        title = "Business Registration Form"
        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        storedPhone = mSettings.getString("USER_NUMBER", "") ?: ""
        storedName = mSettings.getString("USER_NAME", "") ?: ""
        datetime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        // Initialize image picker launcher using scoped storage
        pickImagesLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    if (imagelist.size < 4) { // Limit to a maximum of 4 images
                        imagelist.add(it)
                        Glide.with(this).load(it).into(binding.registerUpload)
                        binding.registerImageText.text = "Images selected: ${imagelist.size}/4"
                    } else {
                        showToast("You can only select up to 4 images.")
                    }
                }
            }

        binding.registerUpload.setOnClickListener { pickImageFromGallery() }
        binding.registerSubmit.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        if (isInputValid()) {
            logAnalyticsEvent()
            uploadImages(imagelist)
        } else {
            showInputErrors()
        }
    }

    private fun isInputValid(): Boolean {
        return binding.registerName.editText?.text?.isNotBlank() == true &&
                binding.registerAddress.editText?.text?.isNotBlank() == true &&
                binding.registerNumber.editText?.text?.isNotBlank() == true &&
                binding.registerCity.editText?.text?.isNotBlank() == true
    }

    private fun showInputErrors() {
        when {
            binding.registerName.editText?.text.isNullOrBlank() -> showToast("Please enter your Name!")
            binding.registerAddress.editText?.text.isNullOrBlank() -> showToast("Please enter your Address!")
            binding.registerNumber.editText?.text.isNullOrBlank() -> showToast("Please enter your Number!")
            binding.registerCity.editText?.text.isNullOrBlank() -> showToast("Please enter your City!")
        }
    }

    private fun logAnalyticsEvent() {
        val mAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle().apply {
            putString("UserName", storedName)
            putString("UserNumber", storedPhone)
        }
        mAnalytics.logEvent("RegisterForm", bundle)
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    // Picks image from the gallery using scoped storage
    private fun pickImageFromGallery() {
        pickImagesLauncher.launch("image/*")
    }

    private fun uploadImages(images: List<Uri>) {
        dialog.show()  // Show the progress dialog

        val model = ContactModel().apply {
            n = binding.registerName.editText?.text.toString()
            a = binding.registerAddress.editText?.text.toString()
            p = binding.registerNumber.editText?.text.toString()
            c = binding.registerCity.editText?.text.toString()
            o = storedName
            i = storedPhone
            datetime = this@NewRegisterActivity.datetime
            d = binding.registerDescription.editText?.text.toString()
            uuid = currentUser

        }
        model.images = HashMap()  // Initialize the HashMap
        if (images.isNotEmpty()) {
            images.forEachIndexed { index, uri ->
                val imageRef = mStorage.child("RequestImages/${uri.lastPathSegment}.jpg")
                imageRef.putFile(uri)
                    .addOnProgressListener { taskSnapshot ->
                        val progress =
                            (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                        dialog.setMessage("Uploading image ${index + 1} of ${images.size}: $progress%")
                    }
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            model.addImage(
                                "image$index",
                                downloadUri.toString()
                            ) // Add the image URL to the model
                            if (index == images.lastIndex) {
                                saveToDatabase(model)
                                dialog.dismiss()  // Dismiss dialog after last upload
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        showToast("File uploading failed: ${e.message}")
                        dialog.dismiss()  // Dismiss dialog on failure
                    }
            }
        } else {
            saveToDatabase(model)
            dialog.dismiss()  // Dismiss dialog if no images to upload
        }
    }

    private fun saveToDatabase(model: ContactModel) {
        Log.d(TAG, "saveToDatabase: ${model.toString()}")
        mReference.child(currentUser!!).setValue(model)
            .addOnSuccessListener {
                showToast("Your details have been accepted, we will contact you shortly.")
                finish()
            }
            .addOnFailureListener { e ->
                showToast("Failed to save data: ${e.message}")
            }
    }
}