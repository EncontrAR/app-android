package ar.com.encontrarpersonas.api

import com.crashlytics.android.Crashlytics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MIT License
 *
 * Copyright (c) 2017 Wolox S.A
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
object AnalyticsManager {

    /**
     * Logs a "notification seen" event. Usually used by NotificationsHandlers to report that
     * the notification that has been displayed was seen by the user.
     *
     * Eg: When the user opens a push notification o the wallpaper is replaced by a missing person's
     * image.
     *
     * alertId: The ID of the notification received.
     */
    fun logNotificationSeen(alertId: Int) {
        EncontrarRestApi
                .notifications
                .notificationReceived(alertId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response != null && response.isSuccessful) {
                            // Do nothing...
                        } else {
                            Crashlytics.log("Couldn't send a \"notification seen\" event to" +
                                    "the API for analytics tracking. Alert ID: $alertId")
                        }
                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Crashlytics.log("Couldn't send a \"notification seen\" event to" +
                                "the API for analytics tracking. Alert ID: $alertId")
                    }
                })
    }

}