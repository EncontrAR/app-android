package ar.com.encontrarpersonas.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.api.EncontrarRestApi
import ar.com.encontrarpersonas.data.models.Position
import com.google.android.gms.location.*
import com.mcxiaoke.koi.ext.toast


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
class LocationUpdateService : Service() {

    private val fusedLocationClient = getFusedLocationClient()
    private val locationRequest = getLocationRequest()
    private val locationCallback = getLocationCallback()

    override fun onBind(intent: Intent?): IBinder? {
        // Used only in case of bound services.
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        if (isLocationPermissionGranted()) {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null)
        } else {
            stopSelf()
        }

    }

    override fun onDestroy() {
        fusedLocationClient.removeLocationUpdates(locationCallback)

        super.onDestroy()
    }

    private fun getFusedLocationClient(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(App.sInstance)
    }

    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = 1000 * 60 * 60 * 4 // Max refresh time: 4 hours
        locationRequest.fastestInterval = 1000 * 60 * 60 * 1 // Min refresh time: 1 hour
        // Mid accuracy (100 mts approx, coarse location) and mid power consumption
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        return locationRequest
    }

    private fun getLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.lastLocation != null) {
                    toast("SENDING LOCATION")
                    sendPositionToApi(locationResult.lastLocation)
                }
            }
        }
    }

    private fun sendPositionToApi(lastLocation: Location) {
        EncontrarRestApi
                .deviceUser
                .updateLoggedDevicePosition(
                        Position(
                                lastLocation.latitude.toString(),
                                lastLocation.longitude.toString())
                )
                .enqueue(null)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return checkCallingOrSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}