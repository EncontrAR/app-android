package ar.com.encontrarpersonas.notifications

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import ar.com.encontrarpersonas.R
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
class LockscreenNotificationsHandler(val context: Context) : INotificationHandler {

    override fun notify(remoteMessage: RemoteMessage) {
        if (supportsLockscreenWallpaper())
            useWallpaperManagerMethod()
        else
            userRemoteControlClientMethod()
    }

    /**
     * This method uses the system's WallpaperManager to modify the lockscreen wallpaper.
     *
     * Bare in mind that while this is the official way of modifying the lockscreen wallpaper,
     * it is only supported on API level 24 (Nougat) or higher.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun useWallpaperManagerMethod() {
        WallpaperManager.getInstance(context).setResource(
                R.raw.test_wallpaper,
                WallpaperManager.FLAG_LOCK
        )
    }

    private fun userRemoteControlClientMethod() {
        Log.e("TBD", "Lockscreen notifications are currently supported only on API 24+")
    }

    /**
     * Returns true if the device supports the official APIs for settings custom wallpapers on
     * the lockscreen. Returns false otherwise.
     */
    fun supportsLockscreenWallpaper(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

}