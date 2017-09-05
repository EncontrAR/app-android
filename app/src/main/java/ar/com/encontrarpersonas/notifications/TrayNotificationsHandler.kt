package ar.com.encontrarpersonas.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.activities.MainActivity
import com.google.firebase.messaging.RemoteMessage

/**
 * MIT License
 *
 * Copyright (c) 2017 Proyecto Encontrar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */
class TrayNotificationsHandler(val context: Context) : INotificationHandler {

    private val DEFAULT_NOTIFICATION_CHANNEL_ID = "ABC123"
    private val DEFAULT_NOTIFICATION_INDIVIDUAL_ID = 123

    override fun notify(remoteMessage: RemoteMessage) {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(
                DEFAULT_NOTIFICATION_INDIVIDUAL_ID,
                buildNotification(remoteMessage))
    }

    private fun buildNotification(remoteMessage: RemoteMessage): Notification {
        val largeIconBitmap = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_notification_big)

        return NotificationCompat
                .Builder(context, DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.theme_color_2))
                .setLights(ContextCompat.getColor(context, R.color.theme_color_4), 500, 500)
                .setVibrate(longArrayOf(0, 500, 250, 500, 250, 500))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(largeIconBitmap)
                .setContentTitle(context.getString(R.string.general_app_name))
                .setSubText(context.getString(R.string.notification_missing_person_nearby))
                .setContentText(remoteMessage.notification.body)
                .setContentIntent(getNotificationOpenIntent())
                .build()
    }

    private fun getNotificationOpenIntent(): PendingIntent {
        return PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

}