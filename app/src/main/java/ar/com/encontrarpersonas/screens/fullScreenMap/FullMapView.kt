package ar.com.encontrarpersonas.screens.fullScreenMap

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class FullMapView(context: Context) : BaseScreenView<FullMapScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                v(MapView::class.java) {
                    size(MATCH, MATCH)

                    init {
                        screen.mapView = Anvil.currentView()
                    }

                    screen.mapView?.getMapAsync { googleMap ->
                        val cameraPosition = CameraUpdateFactory.newLatLng(
                                LatLng(-34.5986068,
                                        -58.4223893))

                        val cameraZoom = CameraUpdateFactory.zoomTo(15f)

                        googleMap.moveCamera(cameraPosition)
                        googleMap.animateCamera(cameraZoom)
                    }
                }

            }
        })
    }
}