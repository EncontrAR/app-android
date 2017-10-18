package ar.com.encontrarpersonas.services

import android.app.WallpaperManager
import android.os.Bundle
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

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
class WallpaperRecoveryJobService : JobService() {

    companion object {
        val EXTRA_FILE_NAME = "fileNameToRecover"
    }

    override fun onStartJob(params: JobParameters): Boolean {
        // Execute code in a separate thread
        Thread(Runnable {

            if (params.extras != null) {
                recoverOriginalWallpaper(params.extras!!)
            }

            jobFinished(params, false) // Flags the job as done
        }).start()

        return true // Sets the job as "ongoing", must call jobFinished(..) later
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        return false // Answers the question: "Should this job be retried?"
    }

    private fun recoverOriginalWallpaper(extras: Bundle) {

        // Get the wallpaper sent to the service as a File reference
        val fileToRecoverName = extras.getString(EXTRA_FILE_NAME)
        val wallpaperToRecover = getFileStreamPath(fileToRecoverName)

        // Set back the user's original wallpaper
        WallpaperManager
                .getInstance(this)
                .setStream(wallpaperToRecover.inputStream())

        // Free resources
        wallpaperToRecover.delete()
    }

}