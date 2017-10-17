package ar.com.encontrarpersonas.screens.fullScreenMap

import android.annotation.SuppressLint
import android.content.Context
import ar.com.encontrarpersonas.activities.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class FullMapView(context: Context) : BaseScreenView<FullMapScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            @SuppressLint("MissingPermission")
            override fun view() {

                val DEFAULT_ZOOM = 15f
                val DEFAULT_TILT = 55f

                v(MapView::class.java) {
                    size(MATCH, MATCH)

                    init {
                        screen.mapView = Anvil.currentView()
                    }

                    screen.mapView?.getMapAsync { googleMap ->
                        val cameraPosition = CameraPosition.Builder()
                                .target(LatLng(-34.5986068, -58.4223893))
                                .zoom(DEFAULT_ZOOM)
                                .tilt(DEFAULT_TILT)
                                .build()

                        googleMap.animateCamera(
                                CameraUpdateFactory
                                        .newCameraPosition(cameraPosition)
                        )

                        if ((screen.activity as MainActivity).userHasLocationPermissionAcepted())
                            googleMap.isMyLocationEnabled = true
                    }
                }

            }
        })
    }
}