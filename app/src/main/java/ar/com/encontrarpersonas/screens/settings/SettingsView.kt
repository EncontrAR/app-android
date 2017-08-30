package ar.com.encontrarpersonas.screens.settings

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.screens.legal.LegalScreen
import ar.com.encontrarpersonas.screens.settings.components.NotificationTypeSwitchView
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class SettingsView(context: Context) : BaseScreenView<SettingsScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                linearLayout {
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)
                    padding(dip(24), dip(8))
                    backgroundColor(ContextCompat.getColor(context, R.color.theme_color_1))

                    textView {
                        size(WRAP, WRAP)
                        text(R.string.screen_settings_personal_data)
                        textSize(sip(24f))
                        textColor(ContextCompat.getColor(context, R.color.text_primary))
                    }

                    editText {
                        size(MATCH, WRAP)
                        hint(R.string.screen_settings_firstname)
                        margin(0, dip(8))
                    }

                    editText {
                        size(MATCH, WRAP)
                        hint(R.string.screen_settings_lastname)
                        margin(0, dip(8))
                    }

                    editText {
                        size(MATCH, WRAP)
                        hint(R.string.screen_settings_national_id_number)
                        margin(0, dip(8))
                    }

                    button {
                        size(WRAP, WRAP)
                        text(R.string.screen_settings_save)
                        layoutGravity(END)
                        margin(0, dip(16))

                        onClick {
                            // TODO Only send the user to the legal screen if they haven't
                            // accepted the ToS before
                            screen.navigator.goTo(LegalScreen())
                        }
                    }

                    NotificationTypeSwitchView(context,
                            description = resources.getString(R.string.screen_settings_notification_conventional))

                    NotificationTypeSwitchView(context,
                            description = resources.getString(R.string.screen_settings_notification_wallpaper))

                    NotificationTypeSwitchView(context,
                            description = resources.getString(R.string.screen_settings_notification_lockscreen))

                    // Dummy view to make an empty space before the ToS button
                    view {
                        size(MATCH, 0)
                        weight(1f)
                    }

                    textView {
                        size(MATCH, WRAP)
                        gravity(BOTTOM or CENTER)
                        text(R.string.screen_settings_tos)
                        textSize(sip(16f))
                        textColor(ContextCompat.getColor(context, R.color.text_primary))
                        padding(dip(16))

                        onClick {
                            screen.navigator.goTo(LegalScreen())
                        }
                    }

                }

            }

        })
    }
}