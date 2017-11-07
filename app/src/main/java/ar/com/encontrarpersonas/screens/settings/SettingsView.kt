package ar.com.encontrarpersonas.screens.settings

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.InputFilter
import android.text.InputType
import android.widget.CompoundButton
import android.widget.LinearLayout
import ar.com.encontrarpersonas.BuildConfig
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.UserRepository
import ar.com.encontrarpersonas.screens.legal.LegalScreen
import ar.com.encontrarpersonas.screens.settings.components.NotificationTypeSwitchView
import com.mcxiaoke.koi.ext.longToast
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class SettingsView(context: Context) : BaseScreenView<SettingsScreen>(context) {

    //Constants
    private val MAX_FIRST_NAME_LENGTH = 75
    private val MAX_LAST_NAME_LENGTH = 75
    private val MAX_NATIONAL_ID_LENGTH = 9
    private val MAX_EMAIL_LENGTH = 64

    private var progressDialog: ProgressDialog? = null

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                if (!screen.store.state.isSynchronising) {
                    if (progressDialog != null && progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }

                scrollView {
                    size(MATCH, MATCH)
                    backgroundColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.theme_color_1))

                    linearLayout {
                        size(MATCH, MATCH)
                        orientation(LinearLayout.VERTICAL)
                        padding(dip(24), dip(8))

                        textView {
                            size(WRAP, WRAP)
                            text(ar.com.encontrarpersonas.R.string.screen_settings_title_personal_data)
                            textSize(sip(24f))
                            textColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.text_primary))
                        }

                        editText {
                            size(MATCH, WRAP)
                            hint(ar.com.encontrarpersonas.R.string.screen_settings_firstname)
                            text(screen.store.state.firstName)
                            inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                            filters(arrayOf<InputFilter>(InputFilter.LengthFilter(MAX_FIRST_NAME_LENGTH)))
                            margin(0, dip(8))
                            if (screen.store.state.hasInvalidFirstName) {
                                error(null) // Dispose previous error object
                                error(context.getString(R.string.screen_settings_error_firstname))
                            }
                            onTextChanged { text ->
                                screen.store.dispatch(SettingsReducer.SET_FIRST_NAME(text.toString()))
                            }
                        }

                        editText {
                            size(MATCH, WRAP)
                            hint(ar.com.encontrarpersonas.R.string.screen_settings_lastname)
                            text(screen.store.state.lastName)
                            inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                            filters(arrayOf<InputFilter>(InputFilter.LengthFilter(MAX_LAST_NAME_LENGTH)))
                            margin(0, dip(8))
                            if (screen.store.state.hasInvalidLastName) {
                                error(null) // Dispose previous error object
                                error(context.getString(R.string.screen_settings_error_lastname))
                            }
                            onTextChanged { text ->
                                screen.store.dispatch(SettingsReducer.SET_LAST_NAME(text.toString()))
                            }
                        }

                        editText {
                            size(MATCH, WRAP)
                            hint(ar.com.encontrarpersonas.R.string.screen_settings_national_id_number)
                            text(screen.store.state.nationalIdNumber)
                            inputType(InputType.TYPE_CLASS_NUMBER)
                            filters(arrayOf<InputFilter>(InputFilter.LengthFilter(MAX_NATIONAL_ID_LENGTH)))
                            margin(0, dip(8))
                            if (screen.store.state.hasInvalidIdNumber) {
                                error(null) // Dispose previous error object
                                error(context.getString(R.string.screen_settings_error_national_id_number))
                            }
                            onTextChanged { text ->
                                screen.store.dispatch(SettingsReducer.SET_NATIONAL_ID(text.toString()))
                            }
                        }

                        editText {
                            size(MATCH, WRAP)
                            hint(ar.com.encontrarpersonas.R.string.screen_settings_email)
                            text(screen.store.state.email)
                            inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                            filters(arrayOf<InputFilter>(InputFilter.LengthFilter(MAX_EMAIL_LENGTH)))
                            margin(0, dip(8))
                            if (screen.store.state.hasInvalidEmail) {
                                error(null) // Dispose previous error object
                                error(context.getString(R.string.screen_settings_error_email))
                            }
                            onTextChanged { text ->
                                screen.store.dispatch(SettingsReducer.SET_EMAIL(text.toString()))
                            }
                        }

                        button {
                            size(WRAP, WRAP)
                            text(ar.com.encontrarpersonas.R.string.screen_settings_save)
                            layoutGravity(END)
                            margin(0, dip(16))
                            textColor(ContextCompat.getColor(context, R.color.text_secondary))
                            backgroundColor(ContextCompat.getColor(context, R.color.theme_color_2))

                            enabled(!screen.store.state.isSynchronising)

                            onClick {
                                // If the user hasn't been advised of the existence of the ToS,
                                // they should be advised with a dialog before sending the messages to the
                                // server.
                                if (!UserRepository.hasUserSeenToS()) {
                                    AlertDialog.Builder(context)
                                            .setTitle(context.getString(ar.com.encontrarpersonas.R.string.screen_settings_dialog_tos_title))
                                            .setMessage(context.getString(ar.com.encontrarpersonas.R.string.screen_settings_dialog_tos_message))
                                            .setPositiveButton(
                                                    context.getString(ar.com.encontrarpersonas.R.string.screen_settings_dialog_tos_continue),
                                                    { _, _ ->
                                                        UserRepository.setUserSawToS(true)
                                                        showProgressDialog()
                                                        screen.presenter.saveUserPersonalData()
                                                    })
                                            .setNegativeButton(
                                                    context.getString(ar.com.encontrarpersonas.R.string.screen_settings_dialog_tos_see),
                                                    { _, _ ->
                                                        context.longToast(context.getString(ar.com.encontrarpersonas.R.string.screen_settings_toast_tos_reminder))
                                                        screen.navigator.goTo(LegalScreen())
                                                    })
                                            .create()
                                            .show()
                                } else {
                                    showProgressDialog()
                                    screen.presenter.saveUserPersonalData()
                                }
                            }
                        }

                        // Section: Notifications

                        textView {
                            size(WRAP, WRAP)
                            text(ar.com.encontrarpersonas.R.string.screen_settings_title_notifications)
                            textSize(sip(24f))
                            textColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.text_primary))
                            margin(0, dip(16), 0, dip(16))
                        }

                        NotificationTypeSwitchView(context,
                                description = resources.getString(ar.com.encontrarpersonas.R.string.screen_settings_notification_tray),
                                isChecked = screen.store.state.trayNotificationsEnabled,
                                onChangeListener = CompoundButton.OnCheckedChangeListener { _, checked ->
                                    screen.presenter.saveUserTrayNotificationsSetting(checked)
                                })

                        NotificationTypeSwitchView(context,
                                description = resources.getString(ar.com.encontrarpersonas.R.string.screen_settings_notification_wallpaper),
                                isChecked = screen.store.state.wallpaperNotificationsEnabled,
                                onChangeListener = CompoundButton.OnCheckedChangeListener { _, checked ->
                                    screen.presenter.saveUserWallpaperNotificationsSetting(checked)
                                })

                        // Section: Terms of Service

                        textView {
                            size(MATCH, WRAP)
                            gravity(BOTTOM or CENTER)
                            text(ar.com.encontrarpersonas.R.string.screen_settings_tos)
                            textSize(sip(16f))
                            textColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.text_primary))
                            padding(dip(16))
                            margin(0, dip(32), 0, 0)

                            onClick {
                                screen.navigator.goTo(LegalScreen())
                            }
                        }

                        // Section: App version
                        textView {
                            size(MATCH, WRAP)
                            gravity(BOTTOM or CENTER)
                            text(context.getString(R.string.general_app_name) + " " + BuildConfig.VERSION_NAME)
                            textSize(sip(12f))
                            textColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.text_primary))
                            padding(dip(16))
                        }

                    }
                }

            }

        })
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage(context.getString(R.string.screen_settings_dialog_loading))
        progressDialog!!.show()
    }
}