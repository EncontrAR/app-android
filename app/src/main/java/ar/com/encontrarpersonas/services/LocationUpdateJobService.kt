package ar.com.encontrarpersonas.services

import android.location.Location
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.api.EncontrarRestApi
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.data.models.Position
import ar.com.encontrarpersonas.extensions.userHasGrantedLocationPermission
import com.crashlytics.android.Crashlytics
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.google.android.gms.location.LocationServices
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
class LocationUpdateJobService : JobService() {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(App.sInstance)
    }

    override fun onStartJob(jobParameters: JobParameters): Boolean {

        if (userHasGrantedLocationPermission()) {
            fusedLocationClient
                    .lastLocation
                    .addOnSuccessListener { lastKnownLocation ->
                        if (lastKnownLocation != null) {
                            sendPositionToApi(jobParameters, lastKnownLocation)
                        } else {
                            Crashlytics.log("Last known location was null, most probably" +
                                    " the device has the location setting disabled by the user.")
                            jobFinished(jobParameters, false)
                        }
                    }

            return true // Sets the job as "ongoing", must call jobFinished(..) later
        } else {
            Crashlytics.log("Aborted location update job, the user hasn't granted location" +
                    " permissions")
            return false // Sets the job as "finished"
        }

    }

    override fun onStopJob(job: JobParameters): Boolean {
        return false // Answers the question: "Should this job be retried?"
    }

    private fun sendPositionToApi(jobParameters: JobParameters, lastLocation: Location) {
        EncontrarRestApi
                .deviceUser
                .updateLoggedDevicePosition(
                        Position(
                                lastLocation.latitude.toString(),
                                lastLocation.longitude.toString())
                )
                .enqueue(object : Callback<List<Campaign>> {
                    override fun onFailure(call: Call<List<Campaign>>?, t: Throwable?) {
                        Crashlytics.log("Couldn't send the latest position to the " +
                                "server: $lastLocation")

                        // Network error: finish job and flag for rescheduling ASAP
                        jobFinished(jobParameters, true)
                    }

                    override fun onResponse(call: Call<List<Campaign>>?,
                                            response: Response<List<Campaign>>?) {
                        if (response == null || !response.isSuccessful) {
                            Crashlytics.log("The server rejected the latest position " +
                                    "update: $lastLocation")
                        }

                        // The API request was made, so no need for requesting and immediate
                        // job reschedule
                        jobFinished(jobParameters, false)
                    }

                })
    }

}