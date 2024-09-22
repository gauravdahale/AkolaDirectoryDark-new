package gauravdahale.gtech.akolacrm.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import gauravdahale.gtech.akolacrm.Model.ContactModel
import gauravdahale.gtech.akolacrm.R

import java.io.IOException
import java.util.HashMap


class RecordActivity : AppCompatActivity(), View.OnClickListener {
    private var nameeditxt: EditText?=null
    private var ownereditxt: EditText?=null
    private var addresseditxt: EditText?=null
    private var phoneditxt: EditText?=null
    private var deseditxt: EditText?=null
    private var timingeditxt: EditText?=null
    private var couneditxt: EditText?=null
    private var ideditxt: EditText?=null
    private var image1: ImageView?=null
    private var image2: ImageView?=null
    private var image3: ImageView?=null
    private var image4: ImageView?=null
    var i1: String?=null
    var i2: String?=null
    var i3: String?=null
    var i4: String?=null
    private var name: String?=null
    private var owner: String?=null
    private var address: String?=null
    private var phone: String?=null
    private var desc: String?=null
    private var timing: String?=null
    private var count: String?=null
    private var uploadbtn1: Button?=null
    private var uploadbtn2: Button?=null
    private var uploadbtn3: Button?=null
    private var uploadbtn4: Button?=null
    private var deletebutton: Button?=null
    private var submitbutton: Button?=null
    private var filepath: Uri? = null
    private lateinit var mStorageRef: StorageReference
    var shopname: String? = null
    var cityref: String?=null
    var id: String?=null
    private var mReference: DatabaseReference? = null
    private var imageref: StorageReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        image1 = findViewById(R.id.recordimage1)
        image2 = findViewById(R.id.recordimage2)
        image3 = findViewById(R.id.recordimage3)
        image4 = findViewById(R.id.recordimage4)
        uploadbtn1 = findViewById(R.id.upload1)
        uploadbtn2 = findViewById(R.id.upload2)
        uploadbtn3 = findViewById(R.id.upload3)
        uploadbtn4 = findViewById(R.id.upload4)
        deletebutton = findViewById(R.id.deleterecord)
        submitbutton = findViewById(R.id.submitrecord)
        deletebutton?.setOnClickListener(this)
        submitbutton?.setOnClickListener(this)
        image1?.setOnClickListener(this)
        image2?.setOnClickListener(this)
        image3?.setOnClickListener(this)
        image4?.setOnClickListener(this)
        uploadbtn1?.setOnClickListener(this)
        uploadbtn2?.setOnClickListener(this)
        uploadbtn3?.setOnClickListener(this)
        uploadbtn4?.setOnClickListener(this)
        nameeditxt = findViewById(R.id.shopname)
        ownereditxt = findViewById(R.id.ownernme)
        addresseditxt = findViewById(R.id.address)
        phoneditxt = findViewById(R.id.phone)
        deseditxt = findViewById(R.id.description)
        timingeditxt = findViewById(R.id.timing)
        couneditxt = findViewById(R.id.count)
        ideditxt = findViewById(R.id.shopid)
        mStorageRef = FirebaseStorage.getInstance().reference
        cityref = intent.getStringExtra("CITY")
        id = intent.getStringExtra("ID")
        //-----------------------------------------------------------------------
        mReference = FirebaseDatabase.getInstance().getReference(intent.getStringExtra("REF"))

        try {
            val extras = intent.extras
            if (extras != null) {
                val contactModel =
                    intent.getSerializableExtra("PARCEL") as ContactModel //Obtaining data
                shopname = contactModel.n
                nameeditxt?.setText(contactModel.n)
                ownereditxt?.setText(contactModel.o)
                addresseditxt?.setText(contactModel.a)
                phoneditxt?.setText(contactModel.p)
                deseditxt?.setText(contactModel.d)
                timingeditxt?.setText(contactModel.t)
                couneditxt?.setText(contactModel.c)
                ideditxt?.setText(id)
                Glide.with(applicationContext).load(contactModel.i).error(R.drawable.photoicon)
                    .into(image1)
                Glide.with(applicationContext).load(contactModel.ii).error(R.drawable.photoicon)
                    .into(image2)
                Glide.with(applicationContext).load(contactModel.iii).error(R.drawable.photoicon)
                    .into(image3)
                Glide.with(applicationContext).load(contactModel.iiii).error(R.drawable.photoicon)
                    .into(image4)
                name = nameeditxt?.text.toString().trim { it <= ' ' }
                owner = ownereditxt?.text.toString().trim { it <= ' ' }
                address = addresseditxt?.text.toString().trim { it <= ' ' }
                phone = phoneditxt?.text.toString().trim { it <= ' ' }
                desc = deseditxt?.text.toString().trim { it <= ' ' }
                timing = timingeditxt?.text.toString().trim { it <= ' ' }
                count = couneditxt?.text.toString().trim { it <= ' ' }
                id = ideditxt?.text.toString().trim { it <= ' ' }


            }

        } catch (e: Exception) {
            Log.e(TAG, "" + e)
        }


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.recordimage1 -> showfilechooser(415)
            R.id.recordimage2 -> showfilechooser(416)
            R.id.recordimage3 -> showfilechooser(417)
            R.id.recordimage4 -> showfilechooser(418)
            R.id.upload1 -> uploadimage1(shopname, "_1","i")
            R.id.upload2 -> uploadimage1(shopname, "_2","ii")
            R.id.upload3 -> uploadimage1(shopname, "_3","iii")
            R.id.upload4 -> uploadimage1(shopname, "_4","iiii")

            R.id.submitrecord -> submitdata()
            R.id.deleterecord -> deletedata()
        }

    }

    private fun deletedata() {
        id = ideditxt?.text.toString().trim { it <= ' ' }
        if (id != "") {
            mReference!!.child(id!!).removeValue()

        }
    }

    private fun submitdata() {
        val record = HashMap<String, String>()
        record["n"] = nameeditxt?.text.toString()
        record["a"] = addresseditxt?.text.toString()
        record["p"] = phoneditxt?.text.toString()
        record["o"] = ownereditxt?.text.toString()
        record["d"] = deseditxt?.text.toString()
        record["t"] = timingeditxt?.text.toString()
        record["c"] = couneditxt?.text.toString()
        id = ideditxt?.text.toString().trim { it <= ' ' }

        mReference!!.child(id!!).setValue(record)
            .addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    "Data Added Succesfully",
                    Toast.LENGTH_LONG
                ).show()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "" + e,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

        private fun uploadimage1(shopname: String?, imagenumber: String,image:String) {
            imageref = mStorageRef?.child("$cityref/$shopname$imagenumber.jpg")

            if (filepath != null) {

                val layout = findViewById<RelativeLayout>(R.id.progressbarlayout)
                val progressBar =
                    ProgressBar(this@RecordActivity, null, android.R.attr.progressBarStyleSmall)
                val params = RelativeLayout.LayoutParams(100, 100)
                params.addRule(RelativeLayout.CENTER_IN_PARENT)
                layout.addView(progressBar, params)
                progressBar.visibility = View.VISIBLE  //To show ProgressBar
                //  mStorageRef.putFile(filepath)

                imageref?.putFile(filepath!!)?.addOnSuccessListener {
                    // Get a URL to the uploaded content
                    imageref?.downloadUrl?.addOnSuccessListener { uri ->
                        i1 = uri.toString()
                        mReference!!.child(ideditxt?.text.toString().trim { it <= ' ' }).child(image).setValue(i1)
                    filepath=null
                    }


                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
                }?.addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                    // ...
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "File uploading failed$exception",
                        Toast.LENGTH_LONG
                    ).show()
                }
                    ?.addOnProgressListener { taskSnapshot ->
                        val progress =
                            (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                        println("Upload is $progress% done")
                    }

            } else {
                Toast.makeText(applicationContext, "Choose an Image first", Toast.LENGTH_LONG).show()
            }
        }

    private fun uploadimage2(shopname: String?, imagenumber: String) {
        imageref = mStorageRef.child("$cityref/$shopname$imagenumber.jpg")

        if (filepath != null) {

            val layout = findViewById<RelativeLayout>(R.id.progressbarlayout)
            val progressBar =
                ProgressBar(this@RecordActivity, null, android.R.attr.progressBarStyleSmall)
            val params = RelativeLayout.LayoutParams(100, 100)
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layout.addView(progressBar, params)
            progressBar.visibility = View.VISIBLE  //To show ProgressBar
            //  mStorageRef.putFile(filepath)

            imageref?.putFile(filepath!!)?.addOnSuccessListener {
                    // Get a URL to the uploaded content
                    imageref?.downloadUrl?.addOnSuccessListener { uri ->
                        i2 = uri.toString()

                        mReference!!.child(ideditxt?.text.toString().trim { it <= ' ' }).child("ii").setValue(i2)
                    }
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
            filepath=null
            }
                ?.addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                    // ...
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "File uploading failed$exception",
                        Toast.LENGTH_LONG
                    ).show()
                }
                ?.addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                    println("Upload is $progress% done")
                }

        } else {
            Toast.makeText(applicationContext, "Choose an Image first", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadimage3(shopname: String?, imagenumber: String) {
        imageref = mStorageRef?.child("$cityref/$shopname$imagenumber.jpg")

        if (filepath != null) {

            val layout = findViewById<RelativeLayout>(R.id.progressbarlayout)
            val progressBar =
                ProgressBar(this@RecordActivity, null, android.R.attr.progressBarStyleSmall)
            val params = RelativeLayout.LayoutParams(100, 100)
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layout.addView(progressBar, params)
            progressBar.visibility = View.VISIBLE  //To show ProgressBar
            //  mStorageRef.putFile(filepath)

            imageref?.putFile(filepath!!)
                ?.addOnSuccessListener {
                    // Get a URL to the uploaded content
                    imageref?.downloadUrl?.addOnSuccessListener { uri ->
                        i3 = uri.toString()

                        mReference!!.child(ideditxt?.text.toString().trim { it <= ' ' }).child("iii").setValue(i3)
                    }
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
                filepath=null
                }
                ?.addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                    // ...
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "File uploading failed$exception",
                        Toast.LENGTH_LONG
                    ).show()
                }
                ?.addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                    println("Upload is $progress% done")
                }

        } else {
            Toast.makeText(applicationContext, "Choose an Image first", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadimage4(shopname: String?, imagenumber: String) {
        imageref = mStorageRef?.child("$cityref/$shopname$imagenumber.jpg")

        if (filepath != null) {

            val layout = findViewById<RelativeLayout>(R.id.progressbarlayout)
            val progressBar =
                ProgressBar(this@RecordActivity, null, android.R.attr.progressBarStyleSmall)
            val params = RelativeLayout.LayoutParams(100, 100)
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layout.addView(progressBar, params)
            progressBar.visibility = View.VISIBLE  //To show ProgressBar
            //  mStorageRef.putFile(filepath)

            imageref?.putFile(filepath!!)
                ?.addOnSuccessListener {
                    // Get a URL to the uploaded content
                    imageref?.downloadUrl?.addOnSuccessListener { uri ->
                        i4 = uri.toString()

                        mReference!!.child(ideditxt?.text.toString().trim { it <= ' ' })
                            .child("iiii").setValue(i4)
                    }
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
                    filepath = null
                }
                ?.addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                    // ...
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "File uploading failed$exception",
                        Toast.LENGTH_LONG
                    ).show()
                }
                ?.addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                    println("Upload is $progress% done")
                }

        } else {
            Toast.makeText(applicationContext, "Choose an Image first", Toast.LENGTH_LONG).show()
        }
    }


    private fun showfilechooser(PICK_IMAGE_CODE: Int) {
        val intent = Intent()
        intent.type = "image/*"

        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Choose image to upload"),
            PICK_IMAGE_CODE
        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 415 && resultCode == RESULT_OK && data != null && data.data != null) {

            filepath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)

                image1?.setImageBitmap(bitmap)
      //          uploadimage1(shopname, "_1")
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if (requestCode == 416 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            filepath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)

                image2?.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if (requestCode == 417 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            filepath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)

                image3?.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if (requestCode == 418 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            filepath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)

                image4?.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    companion object {
        //  StorageReference mStorageRef;
        private val TAG = "RecordActivity"
    }
}