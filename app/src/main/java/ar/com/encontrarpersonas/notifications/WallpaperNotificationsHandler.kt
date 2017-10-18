package ar.com.encontrarpersonas.notifications

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import ar.com.encontrarpersonas.data.UserRepository
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.services.JobDispatcher
import java.util.*

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

    override fun notify(campaign: Campaign, photoBitmap: Bitmap?) {

        // Check if the user has wallpaper notifications enabled
        if (UserRepository.getSettingWallpaperNotifications()) {

            // Ensure that there is an image to set as a wallpaper
            if (photoBitmap != null) {
                useOnlyWallpaperMethod(photoBitmap)
            }
        }
    }

    /**
     * This method uses the system's WallpaperManager to modify the desktop general wallpaper.
     */
    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    private fun useOnlyWallpaperMethod(photoBitmap: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val userWallpaper = (wallpaperManager.drawable as BitmapDrawable).bitmap

        // Sets the temporal wallpaper
        wallpaperManager.setBitmap(photoBitmap)

        // Generate a random UUID for the auxiliary file containing the original user's wallpaper
        val temporaryFileName = UUID.randomUUID().toString()

        // Save the user's origin wallpaper to a temporary file and free resources
        val fileOutputStream = context.openFileOutput(temporaryFileName, Context.MODE_PRIVATE)
        userWallpaper.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
        userWallpaper.recycle()

        // Start a service to recover the original user's wallpaper after a while
        // Its important to do this in a service, since the app may get killed before it
        // recovers the original wallpaper.
        JobDispatcher.startWallpaperRecoveryJob(temporaryFileName)
    }

}