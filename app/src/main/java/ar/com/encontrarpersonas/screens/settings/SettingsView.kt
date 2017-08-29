package ar.com.encontrarpersonas.screens.settings

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
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

                    textView {
                        size(WRAP, WRAP)
                        text(R.string.screen_settings_personal_data)
                        textSize(sip(24f))
                        textColor(ContextCompat.getColor(context, R.color.text_primary))
                    }

                    editText {
                        size(MATCH, WRAP)
                        hint(R.string.screen_settings_firstname)
                        margin(0, dip(16))
                    }

                    editText {
                        size(MATCH, WRAP)
                        hint(R.string.screen_settings_lastname)
                        margin(0, dip(16))
                    }

                    editText {
                        size(MATCH, WRAP)
                        hint(R.string.screen_settings_national_id_number)
                        margin(0, dip(16))
                    }

                    button {
                        size(WRAP, WRAP)
                        text(R.string.screen_settings_save)
                        layoutGravity(END)
                        margin(0, dip(16))
                    }

                    NotificationTypeSwitchView(context,
                            description = resources.getString(R.string.screen_settings_notification_conventional))

                    NotificationTypeSwitchView(context,
                            description = resources.getString(R.string.screen_settings_notification_wallpaper))

                    NotificationTypeSwitchView(context,
                            description = resources.getString(R.string.screen_settings_notification_lockscreen))

                }

            }

        })
    }
}