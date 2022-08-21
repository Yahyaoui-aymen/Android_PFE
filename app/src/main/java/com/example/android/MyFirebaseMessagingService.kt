package com.example.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.android.ui.notification.NotificationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId = "notification_channel"
const val channelName = "com.example.android"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    //**add this line**


    //generate notification
    //attach notification created with the custom layout
    //show notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }
    }

    fun getRemoteView(title: String,message: String) : RemoteViews{
        val remoteView = RemoteViews(channelName , R.layout.alert_notification)
        remoteView.setTextViewText(R.id.notif_title, title)
        remoteView.setTextViewText(R.id.notif_message, message)
        remoteView.setImageViewResource(R.id.notif_logo, R.drawable.ic_notifications_alert)
        return remoteView
    }

    fun generateNotification(title: String,
                             message : String) {
        var requestID = System.currentTimeMillis().toInt()

        val intent = Intent(this,NotificationActivity::class.java)
        //**add this line**
        //**add this line**
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        var pendingIntent: PendingIntent? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {



            pendingIntent = PendingIntent.getActivity(
                this,
                0, intent,
                PendingIntent.FLAG_IMMUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity(
                this,
                0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        }

        //val pendingIntent = PendingIntent.getActivity(this, requestID, intent, 0)

        //channel id , channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications_alert)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())

    }
}