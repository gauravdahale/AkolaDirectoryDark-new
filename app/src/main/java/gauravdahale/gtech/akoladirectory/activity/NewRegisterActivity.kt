package gauravdahale.gtech.akoladirectory.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
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
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.models.ContactModel
import kotlinx.android.synthetic.main.content_register.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class NewRegisterActivity : AppCompatActivity() {
    val TAG = "RegisterActivity"
    var mPath: List<String> = listOf<String>()
    val mStorage = FirebaseStorage.getInstance().getReference().child("requests")
    val mReference = FirebaseDatabase.getInstance().getReference("NewRequests")
    val currentuser = FirebaseAuth.getInstance().currentUser?.uid
    private var mDatabaseReference: DatabaseReference? = null
    private lateinit var shopName: TextInputLayout
    private lateinit var shopAddress: TextInputLayout
    private lateinit var shopNumber: TextInputLayout
    private lateinit var shopCity: TextInputLayout
    private lateinit var bSubmit: Button
    internal var timestamp: Map<String, String>? = null
    internal var storedphone: String? = null
    internal var storedname: String? = null
    internal lateinit var datetime: String
    lateinit var name: String
    lateinit var address: String
    lateinit var phone: String
    lateinit var city: String
    var username: String? = null
    var userphone: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        title = "Business Registration Form"
        shopName = findViewById<View>(R.id.register_name) as TextInputLayout
        shopAddress = findViewById<View>(R.id.register_address) as TextInputLayout
        shopNumber = findViewById<View>(R.id.register_number) as TextInputLayout
        shopCity = findViewById<View>(R.id.register_city) as TextInputLayout
        name = shopName.editText?.text.toString()
        address = shopAddress.editText?.text.toString()
        phone = shopNumber.editText?.text.toString()
        city = shopCity.editText?.text.toString()
        val description: String = register_description.editText?.text.toString()
        bSubmit = findViewById(R.id.register_submit)
        val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        register_upload.setOnClickListener { requestPermissions() }
        storedphone = mSettings.getString("USER_NUMBER", "")
        storedname = mSettings.getString("USER_NAME", "")
        //Geting Requset Date and Time
        datetime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        /*

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp.toString()));
        final String datetime = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
*/
        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        bSubmit.setOnClickListener {


            if (!shopName.editText?.text.isNullOrEmpty() && !shopAddress.editText?.text.isNullOrEmpty() && !shopNumber.editText?.text.isNullOrEmpty()) {
//                uploadimageOnCancel(imagelist)
                val mAnalytics = FirebaseAnalytics.getInstance(this@NewRegisterActivity)
                val mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                val storedname = mSettings.getString("USER_NAME", "")
                val storedphone = mSettings.getString("USER_NUMBER", "")
                val bundle = Bundle()
                bundle.putString("UserName", storedname)
                bundle.putString("UserNumber", storedphone)

                mAnalytics.logEvent("RegisterForm", bundle)
                uploadimage(imagelist)
            } else {

                when {
                    shopName.editText?.text.isNullOrBlank() -> {
                        Toast.makeText(applicationContext, "Please enter your Name!", Toast.LENGTH_SHORT).show()
                    }
                    shopAddress.editText?.text.isNullOrBlank() -> {
                        Toast.makeText(applicationContext, "Please Enter Your Address", Toast.LENGTH_SHORT).show()
                    }
                    shopNumber.editText?.text.isNullOrBlank() -> {
                        Toast.makeText(applicationContext, "Please Enter Your Number", Toast.LENGTH_SHORT).show()
                    }
                    shopCity.editText?.text.isNullOrBlank() -> {
                        Toast.makeText(applicationContext, "Please Enter Your City", Toast.LENGTH_SHORT).show()
                    }
                    register_description.editText?.text.isNullOrBlank() -> {
                        Toast.makeText(applicationContext, "Please business description", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }

    private fun backtomain() {
        val i = Intent(applicationContext, MainActivityTabVersion::class.java)
        startActivity(i)
        finish()
    }

    //check if edittext is empty
    private fun isEmpty(textInputEditText: TextInputLayout): Boolean {
        return if (textInputEditText.editText?.text!!.toString().trim { it <= ' ' }.length > 0) false else true

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri? = null
        Log.d(
                TAG, "onActivityResult() called with: requestCode = [" + requestCode +
                "], " +
                "resultCode = [" + resultCode + "], data = [" + data + "]"
        );

        if (requestCode == 200 && resultCode == RESULT_OK) {
//            mPath = Matisse.obtainPathResult(data)

            Log.d(TAG, "total item selected:${imagelist.size} ");
            mPath.indices.forEach { i ->
                uri = Uri.fromFile(File(mPath.get(i)))

                imagelist.add(uri!!)
                register_image_text.text = " Image selected : ${imagelist.size}"

                Log.d(TAG, "uri: ${uri}");
            }

            Log.d(TAG, "imagelist: ${imagelist.size}");


// Toast.makeText(this@PostFragment.context, "ccss", Toast.LENGTH_SHORT).show();


        }
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            val images: ArrayList<Image> = ImagePicker.getImages(data)
            // Do stuff with image's path or id. For example:
            for (image in images) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Glide.with(this).load(image.uri).into(register_upload)
                    imagelist.add(image.uri)
                } else {
                    Glide.with(this)
                            .load(image.path)
                            .into(register_upload)
                    imagelist.add(image.uri)

                }
            }
        }
    }

    private fun uploadimage(images: ArrayList<Uri>) {
        val dialog = ProgressDialog(this)
        dialog.setMessage("Uploading Please Wait")
        dialog.setTitle("Please Wait...")
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setCancelable(false)
        dialog.show()
        val date = Date()
        val df = SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss aa")
        var formattedDate = df.format(date)

        Log.d(TAG, ": Upload Mode : Multiple");
        Log.d(TAG, ": size = ${images.size}");
        //  mStorageRef.putFile(filepath)

        val model = ContactModel()

        val hashmap = LinkedHashMap<String, String>()
        dialog.show()

            if (images.size != 0) {

                (0 until images.size).forEach { i ->
                    //             Toast.makeText(applicationContext, "Loop $i", Toast.LENGTH_SHORT).show();
                    val date = Date()
                    val df = SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss aa")
                    var formattedDate = df.format(date)
                    val imageref = mStorage.child("$name/" + images[i].lastPathSegment + ".jpg")

                    val uploadTask = imageref.putFile(images[i])

                    uploadTask
                            .addOnProgressListener { taskSnapshot ->
                                val progress =
                                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                                dialog.setMessage("Upload image $i of ${images.size}")
                                dialog.progress = progress.toInt()
                                Log.d(TAG, "Progress: $progress");
                            }

                            .addOnSuccessListener {
                                // Get a URL to the uploaded content
                                imageref.downloadUrl.addOnSuccessListener { uri ->
                                    var imageuri: String? = uri.toString()
                                    model.n = shopName.editText?.text!!.toString().trim { it <= ' ' }
                                    model.a = shopAddress.editText?.text!!.toString()
                                    model.p = shopNumber.editText?.text!!.toString()
                                    model.c = shopCity.editText?.text.toString()
                                    model.o = storedname!!
                                    model.i = storedphone!!
                                    model.datetime = formattedDate
                                    model.d = register_description.editText?.text.toString()
                                    hashmap["image$i"] = imageuri!!
                                    model.uuid = currentuser
                                    model.images = hashmap
                                    model.username = storedname
                                    model.otpnumber = storedphone
                                    mReference.child(currentuser!!).setValue(model)
                                    Log.d(TAG, ": upload Completed");


                                }
                                //progressBar.visibility = View.GONE
                                Toast.makeText(this@NewRegisterActivity, "Your Details are accepted and we " +
                                        "will  contact you shortly", Toast.LENGTH_LONG)
                                        .show();
                                progressbar.hide()
                                dialog.dismiss()
                                model.images = hashmap
                                mReference.child(currentuser!!).setValue(model)
                                Log.d(TAG, ": upload Completed");
                                onBackPressed()

                            }.addOnFailureListener { e ->
                                // progressBar.visibility = View.GONE
                                Toast.makeText(
                                        applicationContext, "File uploading failed" + e.message,
                                        Toast.LENGTH_SHORT
                                ).show()
dialog.dismiss()
                            }

                }

            } else {
                model.n = shopName.editText?.text!!.toString().trim { it <= ' ' }
                model.a = shopAddress.editText?.text!!.toString()
                model.p = shopNumber.editText?.text!!.toString()
                model.c = shopCity.editText?.text.toString()
                model.o = storedname!!
                model.i = storedphone!!
                model.datetime = formattedDate
                model.d = register_description.editText?.text.toString()
                model.uuid = currentuser
                model.username = storedname
                model.otpnumber = storedphone
                //       mReference.child(currentuser!!).setValue(model)
                mReference.push().setValue(model)
                Toast.makeText(NewRegisterActivity@ this, "Request Recieved!!We will call you shortly", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }


    }

    private fun uploadimageOnCancel(images: ArrayList<Uri>) {
        val date = Date()
        val df = SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss aa")
        var formattedDate = df.format(date)
        val imageref = mStorage.child(
                "$name/" + formattedDate +
                        ".jpg"
        )
        Log.d(TAG, ": Upload Mode : Multiple");
        Log.d(TAG, ": size = ${images.size}");
        //  mStorageRef.putFile(filepath)

        val model = ContactModel()

        val hashmap = LinkedHashMap<String, String>()
        if (images.size != 0) {
            for (i in 0..images.size - 1) {
                //             Toast.makeText(applicationContext, "Loop $i", Toast.LENGTH_SHORT).show();
                val date = Date()
                val df = SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss aa")
                var formattedDate = df.format(date)
                val imageref = mStorage.child(
                        "$name/" + images.get(i).lastPathSegment +
                                ".jpg"
                )

                val uploadTask = imageref.putFile(images.get(i))

                uploadTask.addOnSuccessListener {
                    // Get a URL to the uploaded content
                    imageref.downloadUrl.addOnSuccessListener { uri ->
                        var imageuri: String? = uri.toString()


                        model.n = shopName.editText?.text!!.toString().trim { it <= ' ' }
                        model.a = shopAddress.editText?.text!!
                                .toString()
                        model.p = shopNumber.editText?.text!!.toString()
                        model.c = shopCity.editText?.text.toString()
                        model.o = storedname!!
                        model.i = storedphone!!
                        model.datetime = formattedDate
                        model.d = register_description.editText?.text.toString()
                        hashmap["image$i"] = imageuri!!
                        model.uuid = currentuser
                        model.t = "Did not shared the post"

                        model.images = hashmap
                        model.username = storedname
                        model.otpnumber = storedphone
                        mReference.child(currentuser!!).setValue(model)
                        Log.d(TAG, ": upload Completed");


                    }
                    //progressBar.visibility = View.GONE
                    Toast.makeText(this@NewRegisterActivity, "Your Details are accepted and we " +
                            "will  contact you shortly", Toast.LENGTH_LONG).show();

                    onBackPressed()

                }.addOnFailureListener { e ->
                    // progressBar.visibility = View.GONE
                    Toast.makeText(
                            applicationContext, "File uploading failed" + e.message,
                            Toast.LENGTH_SHORT
                    ).show()
                }
                        .addOnProgressListener { taskSnapshot ->
                            val progress =
                                    (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                            progressbar.progress = progress.toInt()
                            Log.d(TAG, "Progress: $progress");
                        }
            }
            progressbar.hide()
            model.images = hashmap
            mReference.child(currentuser!!).setValue(model)
            Log.d(TAG, ": upload Completed");
        } else {
            model.n = shopName.editText?.text!!.toString().trim { it <= ' ' }
            model.a = shopAddress.editText?.text!!
                    .toString()
            model.p = shopNumber.editText?.text!!.toString()
            model.c = shopCity.editText?.text.toString()
            model.o = storedname!!
            model.i = storedphone!!
            model.datetime = formattedDate
            model.d = register_description.editText?.text.toString()
            model.uuid = currentuser
            model.username = storedname
            model.t = "Did not shared the post"
            model.otpnumber = storedphone
            mReference.push().setValue(model)
            Toast.makeText(NewRegisterActivity@ this, "Request Received !!We will call you shortly", Toast.LENGTH_SHORT).show()
        }


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
                            //       Toast.makeText(applicationContext, "All permissions are granted!", Toast.LENGTH_SHORT).show();
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
        val builder = AlertDialog.Builder(RegisterActivity@ this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel();
            openSettings();
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel(); };
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