package ar.com.encontrarpersonas.screens.settings

import com.brianegan.bansa.Action
import com.brianegan.bansa.Reducer

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
class SettingsReducer {

    // Actions
    data class SET_FIRST_NAME(val firstName: String) : Action
    data class SET_LAST_NAME(val lastName: String) : Action
    data class SET_NATIONAL_ID(val nationalId: String) : Action
    data class SET_SETTINGS_TRAY_NOTIFICATIONS(val enabled: Boolean) : Action
    data class SET_SETTINGS_WALLPAPER_NOTIFICATIONS(val enabled: Boolean) : Action
    data class IS_SYNCHRONISING(val isSynchronisingWithServer : Boolean) : Action

    // Reducer
    val reducer = Reducer<SettingsState> { oldState, action ->
        when (action) {
            is SET_FIRST_NAME -> oldState.copy(firstName = action.firstName)
            is SET_LAST_NAME -> oldState.copy(lastName = action.lastName)
            is SET_NATIONAL_ID -> oldState.copy(nationalIdNumber = action.nationalId)
            is SET_SETTINGS_TRAY_NOTIFICATIONS -> oldState.copy(trayNotificationsEnabled = action.enabled)
            is SET_SETTINGS_WALLPAPER_NOTIFICATIONS -> oldState.copy(wallpaperNotificationsEnabled = action.enabled)
            is IS_SYNCHRONISING -> oldState.copy(isSynchronising = action.isSynchronisingWithServer)
            else -> oldState
        }
    }
}