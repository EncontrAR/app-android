package ar.com.encontrarpersonas.screens.fullScreenMap

import android.annotation.SuppressLint
import android.content.Context
import ar.com.encontrarpersonas.utils.MapUtils
import com.google.android.gms.maps.MapView
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

                        MapUtils().drawSearchArea(
                                context,
                                googleMap,
                                screen.store.state.campaign,
                                true)

                    }
                }

            }
        })
    }
}