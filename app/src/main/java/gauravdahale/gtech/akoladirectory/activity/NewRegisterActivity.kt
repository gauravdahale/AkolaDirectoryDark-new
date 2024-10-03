package gauravdahale.gtech.akoladirectory.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import gauravdahale.gtech.akoladirectory.databinding.ActivityRegisterBinding
import gauravdahale.gtech.akoladirectory.models.ContactModel
import java.text.SimpleDateFormat
import java.util.*

class NewRegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"
    private lateinit var binding: ActivityRegisterBinding
    private val mStorage = FirebaseStorage.getInstance().getReference("requests")
    private val mReference = FirebaseDatabase.getInstance().getReference("NewRequests")
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var datetime: String
    private lateinit var storedPhone: String
    private lateinit var storedName: String
    private val imagelist = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Business Registration Form"
        Toast.makeText(this@NewRegisterActivity, "Register New Activity", Toast.LENGTH_SHORT).show()
        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        storedPhone = mSettings.getString("USER_NUMBER", "") ?: ""
        storedName = mSettings.getString("USER_NAME", "") ?: ""

        datetime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        binding.registerUpload.setOnClickListener { requestPermissions() }
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

    private fun uploadImages(images: List<Uri>) {
        val dialog = ProgressDialog(this).apply {
            setMessage("Uploading Please Wait")
            setTitle("Please Wait...")
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setCancelable(false)
            show()
        }

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

        if (images.isNotEmpty()) {
            images.forEachIndexed { index, uri ->
                val imageRef = mStorage.child("${model.n}/${uri.lastPathSegment}.jpg")
                imageRef.putFile(uri)
                    .addOnProgressListener { taskSnapshot ->
                        val progress = (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                        dialog.setMessage("Upload image $index of ${images.size}")
                        dialog.progress = progress
                    }
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            model.images?.set("image$index", downloadUri.toString())
                            if (index == images.lastIndex) {
                                saveToDatabase(model)
                                dialog.dismiss()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        showToast("File uploading failed: ${e.message}")
                        dialog.dismiss()
                    }
            }
        } else {
            saveToDatabase(model)
            dialog.dismiss()
        }
    }

    private fun saveToDatabase(model: ContactModel) {
        mReference.child(currentUser!!).setValue(model)
            .addOnSuccessListener {
                showToast("Your details have been accepted, we will contact you shortly.")
                finish()
            }
            .addOnFailureListener { e ->
                showToast("Failed to save data: ${e.message}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data)) {
            val images: ArrayList<Image> = ImagePicker.getImages(data)
            Log.d(TAG, "onActivityResult: ${images}")
            images.forEach { image ->
                Glide.with(this).load(image.uri).into(binding.registerUpload)
                imagelist.add(image.uri)
            }
            binding.registerImageText.text = "Image selected: ${imagelist.size}"
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }


fun pickImage() {
        //   requestCameraPermission()

        //   requestStoragePermission()
        imagelist.clear()
        ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(2)
                .setLimitMessage("You can select up to 10 images")
//                .setSelectedImages()
                .setRequestCode(200)
                .start();
    }

    companion object {
        //   var imagelist :ArrayList<PostModel> =ArrayList<PostModel>()
        internal val imagelist: ArrayList<Uri> = ArrayList()
    }

    private fun requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        // check if all permissions are granted
                        if (report!!.areAllPermissionsGranted()) {

                                   Toast.makeText(applicationContext, "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            pickImage()
                        }

                        // check for permanent denial of any permission
                        if (report!!.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener { Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show(); }
                .onSameThread()
                .check()
    }

    private fun requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        // permission is granted
//                        openCamera();
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel();
            openSettings();
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel(); };
        builder.show();

    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        val uri = Uri.fromParts("package", packageName, null);
        intent.data = uri;
        startActivityForResult(intent, 101);
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }
}