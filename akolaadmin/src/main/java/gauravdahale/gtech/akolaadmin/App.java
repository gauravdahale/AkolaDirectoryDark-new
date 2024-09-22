package gauravdahale.gtech.akolaadmin;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;


public class App extends Application {
    FirebaseDatabase mDatabase;
    public void onCreate() {
        super.onCreate();
        {
            FirebaseApp.initializeApp(this);
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.setPersistenceEnabled(true);

            }


            context = getApplicationContext();
            //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
            //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.

        }
    }

    private static Context context;

    public static Context getContext() {
        return context;
    }


}