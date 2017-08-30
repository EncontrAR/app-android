package ar.com.encontrarpersonas.screens.legal

import android.content.Context
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import com.brianegan.bansa.BaseStore
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil


class LegalScreen : Screen<LegalView>() {

    //Store
    val store = BaseStore(LegalState(), LegalReducer().reducer)

    // View layer
    override fun createView(context: Context): LegalView {
        store.subscribe { Anvil.render() }
        return LegalView(context)
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_legal_title)
    }
}
