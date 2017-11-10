package ar.com.encontrarpersonas.services

import android.os.Bundle
import ar.com.encontrarpersonas.App
import com.firebase.jobdispatcher.*
import java.util.concurrent.TimeUnit


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
object JobDispatcher {

    val WALLPAPER_DURATION_SECONDS = 5L

    private val UPDATE_LOCATION_JOB_TAG = "LocationUpdateJob"
    private val WALLPAPER_RECOVERY_JOB_TAG = "WallpaperRecoveryJob"

    private val firebaseDispatcher by lazy { FirebaseJobDispatcher(GooglePlayDriver(App.sInstance)) }

    fun startRecurrentLocationUpdateJob() {
        val locationUpdateJob = firebaseDispatcher.newJobBuilder()
                .setTag(UPDATE_LOCATION_JOB_TAG)
                .setService(LocationUpdateJobService::class.java)
                .setLifetime(Lifetime.FOREVER)  // Persist job after device reboot
                .setConstraints(Constraint.ON_ANY_NETWORK) // This jobs requires the network
                .setReplaceCurrent(false) // Do not create a new job if there is an already scheduled one
                .setRecurring(true) // This job is periodic, it will be re-scheduled after completion
                .setTrigger(Trigger.executionWindow(
                        TimeUnit.HOURS.toSeconds(1).toInt(),
                        TimeUnit.HOURS.toSeconds(2).toInt())
                ) // Sets and execution window between 1 and 2 hours, ideally
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL) // Exponential backoff
                .build()

        firebaseDispatcher.mustSchedule(locationUpdateJob)
    }

    fun startWallpaperRecoveryJob(pathToWallpaperToRecover: String) {
        val bundleWithPath = Bundle()
        bundleWithPath.putString(
                WallpaperRecoveryJobService.EXTRA_FILE_NAME,
                pathToWallpaperToRecover
        )

        val wallpaperRecoveryJob = firebaseDispatcher.newJobBuilder()
                .setTag(WALLPAPER_RECOVERY_JOB_TAG)
                .setService(WallpaperRecoveryJobService::class.java)
                .setReplaceCurrent(false) // Do not create a new job if there is an already scheduled one
                .setRecurring(false) // This is a non periodic job, just run once
                .setTrigger(Trigger.executionWindow(
                        TimeUnit.SECONDS.toSeconds(WALLPAPER_DURATION_SECONDS - 1).toInt(),
                        TimeUnit.SECONDS.toSeconds(WALLPAPER_DURATION_SECONDS + 1).toInt())
                ) // Sets and execution window between 4 and 6 seconds, ideally
                .setExtras(bundleWithPath) // Sets a bundle with the path to the file to recover
                .build()

        firebaseDispatcher.mustSchedule(wallpaperRecoveryJob)
    }

}