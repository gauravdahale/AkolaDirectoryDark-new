package gauravdahale.gtech.akoladirectory

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.multidex.MultiDex
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.initialize
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

//import com.codewaves.youtubethumbnailview.ThumbnailLoader;
//import com.onesignal.OneSignal;
//import gauravdahale.gtech.akoladirectory.fcm.MyNotificationOpenedHandler;
//import gauravdahale.gtech.akoladirectory.fcm.MyNotificationRecievedHandler;
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        Firebase.initialize(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        // Initialize Firebase Database with persistence enabled
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)

        // Uncomment to print hash key for debugging (not recommended in production)
        // printHashKey()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                "gauravdahale.gtech.akoladirectory",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("KeyHashError", "Package not found", e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHashError", "SHA algorithm not found", e)
        }
    }
}