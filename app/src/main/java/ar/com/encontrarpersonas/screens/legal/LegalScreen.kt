package ar.com.encontrarpersonas.screens.legal

import android.content.Context
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.UserRepository
import com.brianegan.bansa.BaseStore
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil


class LegalScreen : Screen<LegalView>() {

    //Store
    val store = BaseStore(LegalState(), LegalReducer().reducer)

    // View layer
    override fun createView(context: Context): LegalView {
        store.subscribe { Anvil.render() }

        // If the LegalScreen is shown, we store that in the user preferences. In certain flows
        // of the app, we must show a reminder of the ToS in case the user hasn't seen them at least
        // once.
        UserRepository.setUserSawToS(true)

        return LegalView(context)
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_legal_title)
    }


}
