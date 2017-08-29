package ar.com.encontrarpersonas.screens.settings

import android.content.Context
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import com.brianegan.bansa.BaseStore
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil


class SettingsScreen : Screen<SettingsView>() {

    //Store
    val store = BaseStore(SettingsState(), SettingsReducer().reducer)
    val presenter = SettingsPresenter(store)

    // View layer
    override fun createView(context: Context): SettingsView {
        store.subscribe { Anvil.render() }
        return SettingsView(context)
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_settings_title)
    }
}
