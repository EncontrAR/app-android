package ar.com.encontrarpersonas.screens.detail

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.components.lists.RemoteImageComponent
import ar.com.encontrarpersonas.screens.chat.ChatScreen
import ar.com.encontrarpersonas.screens.detail.components.MissingPersonFieldComponent
import ar.com.encontrarpersonas.screens.fullScreenMap.FullMapScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class DetailView(context: Context) : BaseScreenView<DetailScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                val campaign = screen.store.state.item
                val missingPerson = campaign.missingPerson!!

                backgroundColor(ContextCompat.getColor(context, R.color.theme_color_1))

                frameLayout {
                    size(MATCH, MATCH)

                    scrollView {
                        size(MATCH, MATCH)
                        margin(0, 0, 0, dip(42))

                        linearLayout {
                            size(MATCH, MATCH)
                            orientation(LinearLayout.VERTICAL)

                            // MapView with the latest known location of the Missing Person
                            frameLayout {
                                size(MATCH, dip(260))
                                backgroundColor(ContextCompat.getColor(context, R.color.theme_color_2))

                                v(MapView::class.java) {
                                    size(MATCH, MATCH)

                                    init {
                                        // Must save the map somewhere accessible by the screen,
                                        // since the screen has to delegate its lifecycle to the map view
                                        screen.mapView = Anvil.currentView()
                                    }

                                    screen.mapView?.getMapAsync { googleMap ->
                                        googleMap.setOnMapClickListener {
                                            screen.navigator.goTo(FullMapScreen(campaign))
                                        }

                                        val cameraPosition = CameraUpdateFactory.newLatLng(
                                                LatLng(-34.5986068,
                                                        -58.4223893))

                                        val cameraZoom = CameraUpdateFactory.zoomTo(15f)

                                        googleMap.moveCamera(cameraPosition)
                                        googleMap.animateCamera(cameraZoom)
                                    }
                                }
                            }

                            linearLayout {
                                size(MATCH, WRAP)
                                orientation(LinearLayout.VERTICAL)

                                // Missing person big name
                                textView {
                                    size(MATCH, WRAP)
                                    text("${missingPerson?.name} ${missingPerson?.lastname}")
                                    gravity(CENTER_HORIZONTAL)
                                    textSize(sip(32f))
                                    margin(0, dip(16))
                                    textColor(ContextCompat.getColor(context, R.color.text_primary))
                                }

                                // Campaign description
                                textView {
                                    size(MATCH, WRAP)
                                    text(campaign.description)
                                    gravity(CENTER_HORIZONTAL)
                                    textSize(sip(24f))
                                    margin(0, dip(16))
                                    textColor(ContextCompat.getColor(context, R.color.text_primary))
                                }

                                linearLayout {
                                    size(MATCH, 0)
                                    weight(1f)
                                    orientation(LinearLayout.HORIZONTAL)
                                    padding(dip(8))

                                    // Missing person maing picture
                                    frameLayout {
                                        size(0, dip(300))
                                        weight(0.5f) // 50% of the screen width

                                        RemoteImageComponent(
                                                context,
                                                MATCH,
                                                MATCH,
                                                missingPerson?.photoUrl
                                        )

                                    }

                                    // Missing Person fields (age, gender, etc.)
                                    linearLayout {
                                        size(0, MATCH)
                                        weight(0.5f) // 50% of the screen width
                                        orientation(LinearLayout.VERTICAL)
                                        gravity(CENTER_VERTICAL)

                                        MissingPersonFieldComponent(
                                                context,
                                                description = context.getString(R.string.screen_detail_person_field_gender),
                                                value = if (missingPerson.gender == "female")
                                                    context.getString(R.string.screen_detail_person_gender_female)
                                                else
                                                    context.getString(R.string.screen_detail_person_gender_male)
                                        )

                                        MissingPersonFieldComponent(
                                                context,
                                                description = context.getString(R.string.screen_detail_person_field_age),
                                                value = missingPerson.age.toString()
                                        )

                                        MissingPersonFieldComponent(
                                                context,
                                                description = context.getString(R.string.screen_detail_person_field_national_id),
                                                value = missingPerson.nationalId.toString()
                                        )

                                    }

                                }

                                // Campaign images
                                campaign.imagesUrls?.forEach {
                                    view {
                                        size(MATCH, dip(8))
                                    }

                                    RemoteImageComponent(
                                            context,
                                            MATCH,
                                            dip(300),
                                            it
                                    )
                                }

                            }

                        }

                    }

                    linearLayout {
                        size(MATCH, WRAP)
                        orientation(LinearLayout.VERTICAL)
                        backgroundColor(ContextCompat.getColor(context, R.color.primary_background))
                        layoutGravity(BOTTOM)

                        view {
                            size(MATCH, dip(5))
                            background(ContextCompat.getDrawable(context, R.drawable.gradient_dropshadow_270))
                        }

                        button {
                            size(MATCH, WRAP)
                            text(R.string.screen_detail_report_button)
                            margin(dip(8))
                            textSize(sip(18f))
                            textColor(ContextCompat.getColor(context, R.color.text_secondary))
                            backgroundColor(ContextCompat.getColor(context, R.color.button_red))

                            onClick {
                                screen.navigator.goTo(ChatScreen(campaign))
                            }

                        }

                    }
                }

            }
        })
    }
}