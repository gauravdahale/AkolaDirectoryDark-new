package gauravdahale.gtech.akolacrm;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class App extends Application {
    FirebaseDatabase mDatabase;
    public void onCreate() {
        super.onCreate();
        {
          //  ThumbnailLoader.initialize(Constants.DEVELOPER_KEY);
//printHashKey();
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.setPersistenceEnabled(true);

            }

            Picasso.Builder builder = new Picasso.Builder(this);
//            builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(false);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);
            context = getApplicationContext();
            //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
            //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
           /* OneSignal.startInit(this)
                    .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                    .setNotificationReceivedHandler( new MyNotificationRecievedHandler() )
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();
       printHashKey();*/
        }
    }

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    //    MultiDex.install(this);
    }
    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "gauravdahale.gtech.akoladirectory",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }}