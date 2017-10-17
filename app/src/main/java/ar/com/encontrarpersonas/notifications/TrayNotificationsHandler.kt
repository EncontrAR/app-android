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
import com.crashlytics.android.Crashlytics
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.datasource.DataSubscriber
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
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

        // Check if the user has tray notifications enabled
        if (UserRepository.getSettingTrayNotifications()) {
            processMessage(remoteMessage)
        }
    }

    /**
     * Starts the notification building process.
     */
    private fun processMessage(remoteMessage: RemoteMessage) {

        // TODO probar con lo que paso lucas
        // remoteMessage.data.get("message")
        // remoteMessage.data.get("images")

        // TODO unhardcode the image
        // Retrieve the image for the notification from the network
        fetchImageFromNetwork("https://i.gyazo.com/851b97f968db4df93a2bfa808c1eb325.png",
                object : BaseBitmapDataSubscriber() {
                    override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                        Crashlytics.log("Couldn't retrieve image for notification from the " +
                                "network")
                        buildNotification(remoteMessage, null)
                    }

                    override fun onNewResultImpl(bitmap: Bitmap?) {
                        buildNotification(remoteMessage, bitmap)
                    }
                })
    }

    /**
     * Builds a system (tray) notification with the data received on the push notification
     * and displays it.
     */
    private fun buildNotification(remoteMessage: RemoteMessage, personBitmap: Bitmap?) {
        val notification = NotificationCompat
                .Builder(context, DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.theme_color_2))
                .setLights(ContextCompat.getColor(context, R.color.theme_color_4), 500, 500)
                .setVibrate(longArrayOf(0, 500, 250, 500, 250, 500))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(personBitmap)
                .setContentTitle(context.getString(R.string.general_app_name))
                .setSubText(context.getString(R.string.notification_missing_person_nearby))
                .setContentText(remoteMessage.notification.body)
                .setContentIntent(getNotificationOpenIntent())
                .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(personBitmap)
                        .setBigContentTitle(context.getString(R.string.notification_missing_person_nearby)))
                .build()

        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(
                DEFAULT_NOTIFICATION_INDIVIDUAL_ID,
                notification)
    }

    /**
     * Retrieve the image for the notification from the network using Fresco
     */
    private fun fetchImageFromNetwork(
            url: String,
            callback: DataSubscriber<CloseableReference<CloseableImage>>) {
        Fresco.getImagePipeline()
                .fetchDecodedImage(ImageRequest.fromUri(url), null)
                .subscribe(callback, UiThreadImmediateExecutorService.getInstance())
    }

    /**
     * Prepares an intent with deeplinking for the notification. This is used to open
     * the specific campaign details corresponding to the received notification.
     */
    private fun getNotificationOpenIntent(): PendingIntent {
        return PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

}