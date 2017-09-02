package ar.com.encontrarpersonas.api

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebasePushNotificationsReceiver : FirebaseMessagingService() {

    companion object {
        val DEFAULT_NOTIFICATION_CHANNEL_ID = "ABC123"
        val DEFAULT_NOTIFICATION_INDIVIDUAL_ID = 123
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage != null) {
            val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(
                    DEFAULT_NOTIFICATION_INDIVIDUAL_ID,
                    buildNotification(remoteMessage))
        }

    }

    fun buildNotification(remoteMessage: RemoteMessage): Notification {
        val largeIconBitmap = BitmapFactory.decodeResource(
                this.resources,
                R.drawable.ic_notification_big)

        return NotificationCompat
                .Builder(this, DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(largeIconBitmap)
                .setContentTitle(this.getString(R.string.general_app_name))
                .setContentText(remoteMessage.notification.body)
                .setContentIntent(getNotificationOpenIntent())
                .build()
    }

    fun getNotificationOpenIntent(): PendingIntent {
        return PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

    }
}
