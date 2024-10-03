//package gauravdahale.gtech.akoladirectory.fcm
//
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Intent
//import android.media.RingtoneManager
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import gauravdahale.gtech.akoladirectory.R
//import gauravdahale.gtech.akoladirectory.activity.MainActivity
//
///**
// * Created by Gaurav on 8/8/2018.
// */
//internal class MyFirebaseMessagingService : FirebaseMessagingService() {
//    private fun sendMyNotification(message: String) {
//        //On click of notification it redirect to this Activity
//
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this)
//            .setSmallIcon(R.drawable.akolalogo)
//            .setContentTitle("My Firebase Push notification")
//            .setContentText(message)
//            .setAutoCancel(true)
//            .setSound(soundUri)
//            .setContentIntent(pendingIntent)
//
//        val notificationManager =
//            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        notificationManager.notify(0, notificationBuilder.build())
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        // ...
//
//
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//
//
//        Log.d(TAG, "From: " + remoteMessage.from)
//
//        // Check if message contains a data payload.
//        if (remoteMessage.data.size > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.data)
//
//            /*
//            if (*/
//            /* Check if data needs to be processed by long running job */ /* true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }*/
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.notification != null) {
//            Log.d(
//                TAG, "Message Notification Body: " + remoteMessage.notification!!
//                    .body
//            )
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }
//
//    companion object {
//        private const val TAG = "FIREBASEMESSAGINSERVICE"
//    }
//}