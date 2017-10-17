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

import ar.com.encontrarpersonas.data.models.MissingPerson
import ar.com.encontrarpersonas.notifications.TrayNotificationsHandler
import ar.com.encontrarpersonas.notifications.WallpaperNotificationsHandler
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

            // Deserialize push notification data
            val missingPerson = Gson().fromJson(
                    remoteMessage.data["missing_person"], MissingPerson::class.java
            )

            // Display the received notification on the system tray
            TrayNotificationsHandler(this).notify(missingPerson)

            // Display the received notification in the desktop wallpaper
            WallpaperNotificationsHandler(this).notify(missingPerson)
        }

    }
}
