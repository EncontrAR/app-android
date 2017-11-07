package ar.com.encontrarpersonas.screens.settings

import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.UserRepository
import com.brianegan.bansa.Store
import com.crashlytics.android.Crashlytics
import com.mcxiaoke.koi.ext.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MIT License
 *
 * Copyright (c) 2017 Proyecto Encontrar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */
class SettingsPresenter(val store: Store<SettingsState>) {

    /**
     * Stores the user preferences for tray notifications locally.
     */
    fun saveUserTrayNotificationsSetting(enabled: Boolean) {
        UserRepository.setSettingTrayNotifications(enabled)
        store.dispatch(SettingsReducer.SET_SETTINGS_TRAY_NOTIFICATIONS(enabled))
    }

    /**
     * Stores the user preferences for wallpaper notifications locally.
     */
    fun saveUserWallpaperNotificationsSetting(enabled: Boolean) {
        UserRepository.setSettingWallpaperNotifications(enabled)
        store.dispatch(SettingsReducer.SET_SETTINGS_WALLPAPER_NOTIFICATIONS(enabled))
    }

    /**
     * Stores the user's personal messages (first name, last name, national id, etc.) and sends
     * the messages to the server.
     */
    fun saveUserPersonalData() {

        if (checkUserDataPreconditions()) {
            store.dispatch(SettingsReducer.IS_SYNCHRONISING(true))

            with(UserRepository) {
                setUserFirstname(store.state.firstName)
                setUserLastName(store.state.lastName)
                setUserNationalId(store.state.nationalIdNumber)
                setUserEmail(store.state.email)

                syncWithServer(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        App.sInstance.toast(R.string.error_network)
                        store.dispatch(SettingsReducer.IS_SYNCHRONISING(false))
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response != null && response.isSuccessful) {
                            App.sInstance.toast(R.string.screen_settings_saved_ok)
                        } else {
                            Crashlytics.log("Couldn't save user's preferences: $response")
                            App.sInstance.toast(R.string.screen_settings_saved_failed)
                        }

                        store.dispatch(SettingsReducer.IS_SYNCHRONISING(false))
                    }

                })
            }
        }
    }

    /**
     * Returns true if the user data is valid, false otherwise. This method will dispatch any
     * appropriate actions if it encounters an error.
     */
    private fun checkUserDataPreconditions(): Boolean {

        // The first name may be empty or contain only latin letters
        if (store.state.firstName.isNotEmpty() && !containsOnlyLetters(store.state.firstName)) {
            store.dispatch(SettingsReducer.ERROR_FIRST_NAME)
            return false
        }

        // The last name may be empty or contain only latin letters
        if (store.state.lastName.isNotEmpty() && !containsOnlyLetters(store.state.lastName)) {
            store.dispatch(SettingsReducer.ERROR_LAST_NAME)
            return false
        }

        // The National ID can be empty or contain only numbers
        if (store.state.nationalIdNumber.isNotEmpty() && !containsOnlyNumbers(store.state.nationalIdNumber)) {
            store.dispatch(SettingsReducer.ERROR_NATIONAL_ID)
            return false
        }

        // The email field may be empty or with a valid email address,
        // but cannot contain invalid email addressees
        if (store.state.email.isNotEmpty() && !isValidEmail(store.state.email)) {
            store.dispatch(SettingsReducer.ERROR_EMAIL)
            return false
        }

        // If every previous precondition was successful, then return true
        return true
    }

    /**
     * Returns true if the given string only contains letters (uppercase and lowercase) that are
     * present on the Unicode latin charset or whitespaces in the middle, false otherwise
     */
    private fun containsOnlyLetters(string: String): Boolean {
        return string.trim().matches(Regex("[\\p{L}\\s]+"))
    }

    /**
     * Returns true if the given string only contains numbers, false otherwise
     */
    private fun containsOnlyNumbers(string: String): Boolean {
        return string.trim().matches(Regex("[0-9]+"))
    }

    /**
     * Returns true if the provided string matches an email pattern, false otherwise
     */
    private fun isValidEmail(email: String): Boolean {
        return email.trim().isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

}