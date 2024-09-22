package gauravdahale.gtech.akoladirectory.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import gauravdahale.gtech.akoladirectory.R
import gauravdahale.gtech.akoladirectory.R.id.splashscreen
import gauravdahale.gtech.akoladirectory.activity.DrawerActivity

class SplashScreen : Activity() {
    private var mRemoteConfig: FirebaseRemoteConfig? = null
    var splash: ImageView? = null
    var splashurl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        splash = findViewById<View>(splashscreen) as ImageView
        //splash.setImageResource(R.drawable.akola1);
        val SPLASH_TIME_OUT = 4000
        Handler().postDelayed({
            this@SplashScreen.startActivity(Intent(this@SplashScreen, DrawerActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT.toLong())
        mRemoteConfig = FirebaseRemoteConfig.getInstance()
        val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
                .build()
        mRemoteConfig?.setConfigSettingsAsync(remoteConfigSettings)
        mRemoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
        if (isNetworkAvailable) {
            val fetch = mRemoteConfig?.fetch(0)
            fetch?.addOnSuccessListener(this) {
                mRemoteConfig?.fetchAndActivate()
                mRemoteConfig?.getString("background")
                splashurl = mRemoteConfig?.getString("splashscreen")
                Companion.setsplash(this)
            }
        }
    }

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    companion object {
        private fun setsplash(splashScreen: SplashScreen) {
            Picasso.with(splashScreen.applicationContext).load(splashScreen.splashurl).networkPolicy(NetworkPolicy.OFFLINE).into(splashScreen.splash, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(splashScreen).load(splashScreen.splashurl).into(splashScreen.splash)
                }
            })
            /*Glide.with(this)


                        .load(splashurl) // image url

                        .error(R.drawable.akola1)  // any image in case of error
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(splash);  // imageview object
    */
        }
    }
}