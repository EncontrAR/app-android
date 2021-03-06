package ar.com.encontrarpersonas.services

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

import android.graphics.Bitmap
import ar.com.encontrarpersonas.api.AnalyticsManager
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.notifications.TrayNotificationsHandler
import ar.com.encontrarpersonas.notifications.WallpaperNotificationsHandler
import com.crashlytics.android.Crashlytics
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.datasource.DataSubscriber
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class FirebasePushNotificationsService : FirebaseMessagingService() {

    /**
     * This callback gets executed when a Firebase issued notification is received.
     * Bare in mind that notifications sent by Firebase with a "Notification" field won't
     * call this method if the app is in the background. Instead, they will display a system
     * tray notification directly.
     *
     * To receive notifications on this callback, Firebase issued notifications must be sent with
     * a "Data" field (instead of a "Notification" one) or either the app must be in foreground.
     *
     * More details about Firebase notifications types can be found here:
     * https://firebase.google.com/docs/cloud-messaging/concept-options
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage != null) {

            // Deserialize push notification alert ID
            val alertId = Gson().fromJson(
                    remoteMessage.data["alert_id"], Int::class.java
            )

            // Deserialize push notification data
            val campaign = Gson().fromJson(
                    remoteMessage.data["message"], Campaign::class.java
            )

            // Fetch the image for the notification from the network
            if (campaign.missingPerson?.photoUrl != null) {
                fetchImageFromNetwork(campaign.missingPerson.photoUrl,
                        object : BaseBitmapDataSubscriber() {
                            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                                Crashlytics.log("Couldn't retrieve image for notification from the " +
                                        "network")
                                sendNotificationDataToHandlers(alertId, campaign, null)
                            }

                            override fun onNewResultImpl(bitmap: Bitmap?) {
                                sendNotificationDataToHandlers(alertId, campaign, bitmap)
                            }
                        })

            }

        }
    }

    /**
     * Retrieve the image for the notification from the network using Fresco
     */
    private fun fetchImageFromNetwork(
            url: String?,
            callback: DataSubscriber<CloseableReference<CloseableImage>>) {
        Fresco.getImagePipeline()
                .fetchDecodedImage(ImageRequest.fromUri(url), null)
                .subscribe(callback, UiThreadImmediateExecutorService.getInstance())
    }

    /**
     * Delegate notification display to specific handlers for each type of notification
     */
    private fun sendNotificationDataToHandlers(alertId: Int, campaign: Campaign, photoBitmap: Bitmap?) {

        // Display the received notification in the desktop wallpaper
        //
        // Recommendation: Call this handler before any other one, since it is the slowest one
        // and if not called at first, there may be a delay between the wallpaper and any other
        // form of notification
        val wallpaperNotificationMade = WallpaperNotificationsHandler(this).notify(campaign, photoBitmap)

        // Display the received notification on the system tray
        val trayNotificationMade = TrayNotificationsHandler(this).notify(campaign, photoBitmap)

        // If any form of notification was successfully performed (that means, it was shown to the
        // user), then log that as a metric
        if (trayNotificationMade || wallpaperNotificationMade) {
            AnalyticsManager.logNotificationSeen(alertId)
        }
    }
}
