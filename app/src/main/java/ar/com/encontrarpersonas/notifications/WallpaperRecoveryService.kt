package ar.com.encontrarpersonas.notifications

import android.app.IntentService
import android.app.WallpaperManager
import android.content.Intent

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
class WallpaperRecoveryService : IntentService("WallpaperRecovery") {

    val WALLPAPER_DURATION = 5000L

    companion object {
        val EXTRA_WALLPAPER_BITMAP = "wallpaperPath"
    }

    override fun onHandleIntent(intent: Intent) {

        // Wait a fixed amount of time before recovering the user's original wallpaper
        Thread.sleep(WALLPAPER_DURATION)

        // Get the wallpaer sent to the service as a File reference
        val wallpaperPath = intent.getStringExtra(EXTRA_WALLPAPER_BITMAP)
        val wallpaperFile = getFileStreamPath(wallpaperPath)

        // Set back the user's original wallpaper
        WallpaperManager
                .getInstance(this)
                .setStream(wallpaperFile.inputStream())

        // Free resources
        wallpaperFile.delete()
    }
}