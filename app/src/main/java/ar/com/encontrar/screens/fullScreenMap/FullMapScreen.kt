package ar.com.encontrar.screens.detail

import android.content.Context
import ar.com.encontrar.App
import ar.com.encontrar.R
import ar.com.encontrar.models.Metadata
import ar.com.encontrar.utils.AbstractMapScreen
import com.brianegan.bansa.BaseStore
import trikita.anvil.Anvil


class FullMapScreen(item: Metadata) : AbstractMapScreen<FullMapScreen>() {

    //Store
    val store = BaseStore(FullMapState(item), FullMapReducer().reducer)
    val presenter = FullMapPresenter(store)

    // View layer
    override fun createView(context: Context): FullMapView {
        store.subscribe { Anvil.render() }
        return FullMapView(context)
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_map_title)
    }

}
