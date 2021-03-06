package ar.com.encontrarpersonas.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.activities.MainActivity
import ar.com.encontrarpersonas.data.UserRepository
import ar.com.encontrarpersonas.data.models.Campaign


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

    private val DEFAULT_NOTIFICATION_CHANNEL_ID = "default_notification_channel"
    // Use a timestamp as default notification id
    private val DEFAULT_NOTIFICATION_INDIVIDUAL_ID = System.currentTimeMillis().toInt()

    override fun notify(campaign: Campaign, photoBitmap: Bitmap?): Boolean {

        // Check if the user has tray notifications enabled
        if (UserRepository.getSettingTrayNotifications()) {
            buildNotification(campaign, photoBitmap)
            return true
        } else {
            return false
        }
    }

    /**
     * Builds a system (tray) notification with the messages received on the push notification
     * and displays it.
     */
    private fun buildNotification(campaign: Campaign, photoBitmap: Bitmap?) {
        val notification = NotificationCompat
                .Builder(context, DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setColor(ContextCompat.getColor(context, R.color.theme_color_2))
                .setLights(ContextCompat.getColor(context, R.color.theme_color_4), 500, 500)
                .setVibrate(longArrayOf(0, 500, 250, 500, 250, 500))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(photoBitmap)
                .setContentTitle(context.getString(R.string.notification_missing_person_nearby))
                .setSubText("${campaign.missingPerson?.name} ${campaign.missingPerson?.lastname}")
                .setContentIntent(getNotificationOpenIntent(campaign))
                .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(photoBitmap))
                .build()

        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(
                campaign.id ?: DEFAULT_NOTIFICATION_INDIVIDUAL_ID,
                notification)
    }

    /**
     * Prepares an intent with deeplinking for the notification. This is used to open
     * the specific campaign details corresponding to the received notification.
     */
    private fun getNotificationOpenIntent(campaign: Campaign): PendingIntent {
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.putExtra(MainActivity.EXTRA_CAMPAIGN, campaign)

        return PendingIntent.getActivity(
                context,
                0,
                mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

}