package ar.com.encontrarpersonas.screens.detail

import android.app.AlertDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.activities.MainActivity
import ar.com.encontrarpersonas.components.lists.RemoteImageComponent
import ar.com.encontrarpersonas.data.UserRepository
import ar.com.encontrarpersonas.screens.chat.ChatScreen
import ar.com.encontrarpersonas.screens.detail.components.MissingPersonFieldComponent
import ar.com.encontrarpersonas.screens.fullScreenMap.FullMapScreen
import ar.com.encontrarpersonas.screens.settings.SettingsScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
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
                                        googleMap.uiSettings.isScrollGesturesEnabled = false

                                        googleMap.setOnMapClickListener {
                                            screen.navigator.goTo(FullMapScreen(campaign))
                                        }

                                        val cameraPosition = CameraPosition.Builder()
                                                .target(LatLng(-34.5986068, -58.4223893))
                                                .zoom(14f)
                                                .tilt(0f)
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

                            linearLayout {
                                size(MATCH, WRAP)
                                orientation(LinearLayout.VERTICAL)

                                // Missing person big name
                                textView {
                                    size(MATCH, WRAP)
                                    text("${missingPerson.name} ${missingPerson.lastname}")
                                    gravity(CENTER_HORIZONTAL)
                                    textSize(sip(32f))
                                    margin(0, dip(16))
                                    textColor(ContextCompat.getColor(context, R.color.text_primary))
                                }

                                // Campaign title
                                textView {
                                    size(MATCH, WRAP)
                                    text(campaign.title)
                                    gravity(CENTER_HORIZONTAL)
                                    textSize(sip(24f))
                                    margin(dip(16), dip(16), dip(16), dip(4))
                                    textColor(ContextCompat.getColor(context, R.color.text_primary))
                                }

                                // Campaign description
                                textView {
                                    size(MATCH, WRAP)
                                    text(campaign.description)
                                    gravity(CENTER_HORIZONTAL)
                                    textSize(sip(18f))
                                    margin(dip(16), dip(4), dip(16), dip(16))
                                    textColor(ContextCompat.getColor(context, R.color.text_primary))
                                }

                                // Missing person details
                                linearLayout {
                                    size(MATCH, 0)
                                    weight(1f)
                                    orientation(LinearLayout.HORIZONTAL)
                                    background(ContextCompat.getDrawable(
                                            context,
                                            R.drawable.shape_missin_person_detail_wrapper
                                    ))
                                    margin(dip(16), dip(24))

                                    // Missing person main picture
                                    RemoteImageComponent(
                                            context,
                                            dip(128),
                                            dip(128),
                                            missingPerson.photoUrl,
                                            rounded = true
                                    )

                                    // Missing Person fields (age, gender, etc.)
                                    linearLayout {
                                        size(0, MATCH)
                                        weight(1f)
                                        orientation(LinearLayout.VERTICAL)
                                        gravity(LEFT or CENTER_VERTICAL)
                                        padding(dip(8))
                                        margin(dip(8), 0, 0, 0)

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

                                // Artificial space between details and campaign images
                                view {
                                    size(MATCH, dip(16))
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
                        layoutGravity(BOTTOM)

                        view {
                            size(MATCH, dip(8))
                            background(ContextCompat.getDrawable(context, R.drawable.gradient_dropshadow_270))
                        }

                        frameLayout {
                            backgroundColor(ContextCompat.getColor(context, R.color.primary_background))
                            size(MATCH, WRAP)

                            button {
                                size(MATCH, WRAP)
                                text(R.string.screen_detail_report_button)
                                margin(dip(8))
                                textSize(sip(18f))
                                textColor(ContextCompat.getColor(context, R.color.text_secondary))
                                backgroundColor(ContextCompat.getColor(context, R.color.button_red))

                                onClick {

                                    // The user needs to have completed his personal details
                                    // before reporting any finding.
                                    if (UserRepository.userHasCompletePersonalDetails()) {
                                        screen.navigator.goTo(ChatScreen(campaign))
                                    } else {
                                        AlertDialog.Builder(context)
                                                .setTitle(context.getString(R.string.screen_detail_dialog_personal_details_title))
                                                .setMessage(context.getString(R.string.screen_detail_dialog_personal_details_message))
                                                .setPositiveButton(
                                                        context.getString(R.string.screen_detail_dialog_personal_details_complete),
                                                        { _, _ ->
                                                            screen.navigator.goTo(SettingsScreen())
                                                        })
                                                .create()
                                                .show()
                                    }

                                }

                            }

                        }

                    }
                }

            }
        })
    }
}