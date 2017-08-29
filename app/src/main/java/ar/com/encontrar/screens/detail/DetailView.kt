package ar.com.encontrar.screens.detail

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrar.R
import com.google.android.gms.maps.MapView
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class DetailView(context: Context) : BaseScreenView<DetailScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                backgroundColor(ContextCompat.getColor(context, R.color.theme_color_5))

                linearLayout {
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)

                    frameLayout {
                        size(MATCH, 0)
                        weight(0.3f) // 30% of the screen height

                        v(MapView::class.java) {
                            size(MATCH, MATCH)

                            init {
                                // Must save the map somewhere accessible by the screen,
                                // since the screen has to delegate its lifecycle to the map view
                                screen.mapView = Anvil.currentView()
                            }

                            screen.mapView?.getMapAsync { googleMap ->
                                googleMap.setOnMapClickListener {
                                    screen.navigator.goTo(FullMapScreen(screen.store.state.item))
                                }
                            }
                        }
                    }

                    linearLayout {
                        size(MATCH, 0)
                        orientation(LinearLayout.VERTICAL)
                        weight(0.7f) // 70% of the screen height

                        textView {
                            size(MATCH, WRAP)
                            text("Juan Perez")
                            gravity(CENTER_HORIZONTAL)
                            textSize(sip(32f))
                            margin(0, dip(16))
                            textColor(ContextCompat.getColor(context, R.color.theme_color_3))
                        }

                        linearLayout {
                            size(MATCH, 0)
                            weight(1f)
                            orientation(LinearLayout.HORIZONTAL)
                            padding(dip(8))

                            imageView {
                                size(0, MATCH)
                                weight(0.5f) // 50% of the screen width
                                backgroundColor(Color.BLACK)
                            }

                            linearLayout {
                                size(0, MATCH)
                                weight(0.5f) // 50% of the screen width
                                orientation(LinearLayout.VERTICAL)
                                gravity(CENTER_VERTICAL)

                                var x = 5
                                while (x > 0) {
                                    x--

                                    textView {
                                        size(MATCH, WRAP)
                                        text("Dato a recibir por API")
                                        gravity(CENTER_HORIZONTAL)
                                        textSize(sip(16f))
                                        textColor(Color.BLACK)
                                        padding(dip(8))
                                    }

                                }

                            }

                        }

                        button {
                            size(MATCH, WRAP)
                            text("Reportar")
                            margin(dip(8))
                            textSize(sip(18f))
                            textColor(Color.WHITE)
                            backgroundColor(ContextCompat.getColor(context, R.color.button_red))

                        }

                    }

                }
            }
        })
    }
}