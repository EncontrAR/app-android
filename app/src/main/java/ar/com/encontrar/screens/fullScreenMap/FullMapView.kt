package ar.com.encontrar.screens.detail

import android.content.Context
import com.google.android.gms.maps.MapView
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
                }

            }
        })
    }
}