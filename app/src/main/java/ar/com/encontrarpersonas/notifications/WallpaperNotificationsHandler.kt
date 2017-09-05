package ar.com.encontrarpersonas.notifications

import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import ar.com.encontrarpersonas.R
import com.google.firebase.messaging.RemoteMessage
import com.mcxiaoke.koi.async.asyncDelay

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
class WallpaperNotificationsHandler(val context: Context) : INotificationHandler {

    val WALLPAPER_DURATION = 5000L

    override fun notify(remoteMessage: RemoteMessage) {
        useWallpaperManagerMethod()
    }

    /**
     * This method uses the system's WallpaperManager to modify the desktop wallpaper.
     */
    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    private fun useWallpaperManagerMethod() {
        val wallpaperManager = WallpaperManager.getInstance(context)

        val userWallpaper = wallpaperManager.drawable

        wallpaperManager.setResource(R.raw.test_wallpaper)

        asyncDelay(WALLPAPER_DURATION) {
            wallpaperManager.setBitmap((userWallpaper as BitmapDrawable).bitmap)
        }
    }

}