package gauravdahale.gtech.akoladirectory.fcm;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Gaurav on 8/8/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private DatabaseReference mDatabaseReference;

    String refreshedToken = FirebaseInstanceId.getInstance().getToken();



    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Log.d("TOKEN", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //Creating a movie object with user defined variables
        SharedPreferences prefs = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("USER_TOKEN", token);
        editor.apply();



    }
}