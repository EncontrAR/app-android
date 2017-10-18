package ar.com.encontrarpersonas.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.extensions.userHasGrantedLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions

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
class MapUtils {

    private val BOUNDS_PADDING_PX = 100
    private val ANGLED_CAMERA_TILT = 55f
    private val ANGLED_CAMERA_ZOOM = 15f

    fun drawSearchArea(
            context: Context,
            googleMap: GoogleMap,
            campaign: Campaign,
            tilted: Boolean = false) {

        if (campaign.lastSearchZone?.southWestLat != null
                && campaign.lastSearchZone.southWestLong != null
                && campaign.lastSearchZone.northEastLat != null
                && campaign.lastSearchZone.northEastLong != null) {

            // (X1, Y1)
            val southWestLoc = LatLng(
                    campaign.lastSearchZone.southWestLat,
                    campaign.lastSearchZone.southWestLong
            )

            // (X2, Y1)
            val southEastLoc = LatLng(
                    campaign.lastSearchZone.northEastLat,
                    campaign.lastSearchZone.southWestLong
            )

            // (X1, Y2)
            val northWestLoc = LatLng(
                    campaign.lastSearchZone.southWestLat,
                    campaign.lastSearchZone.northEastLong
            )

            // (X2, Y2)
            val northEastLoc = LatLng(
                    campaign.lastSearchZone.northEastLat,
                    campaign.lastSearchZone.northEastLong
            )

            // Draw search area on the map
            googleMap.addPolygon(PolygonOptions()
                    .add(southWestLoc)
                    .add(southEastLoc)
                    .add(northEastLoc)
                    .add(northWestLoc)
                    .strokeWidth(5F)
                    .strokeColor(ContextCompat.getColor(context, R.color.search_area_stroke))
                    .fillColor(ContextCompat.getColor(context, R.color.search_area_fill))
            )

            // Determine search area bounds
            val builder = LatLngBounds.Builder()
            builder.include(southWestLoc)
            builder.include(southEastLoc)
            builder.include(northEastLoc)
            builder.include(northWestLoc)
            val bounds = builder.build()

            // Move to camera to an appropriate position, depending if the tilted mode is enabled
            if (tilted) {
                val tiltedCamera = CameraPosition.Builder()
                        .target(bounds.center)
                        .zoom(ANGLED_CAMERA_ZOOM)
                        .tilt(ANGLED_CAMERA_TILT)
                        .build()

                googleMap.animateCamera(
                        CameraUpdateFactory
                                .newCameraPosition(tiltedCamera)
                )
            } else {
                googleMap.animateCamera(
                        CameraUpdateFactory
                                .newLatLngBounds(bounds, BOUNDS_PADDING_PX)
                )
            }

        }

        if (context.userHasGrantedLocationPermission()) {
            googleMap.isMyLocationEnabled = true
        }
    }

}