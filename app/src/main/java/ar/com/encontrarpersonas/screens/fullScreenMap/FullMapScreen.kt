package ar.com.encontrarpersonas.screens.fullScreenMap

import android.content.Context
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.utils.AbstractMapScreen
import com.brianegan.bansa.BaseStore
import trikita.anvil.Anvil


class FullMapScreen(item: Campaign) : AbstractMapScreen<FullMapScreen>() {

    //Store
    val store = BaseStore(FullMapState(item), FullMapReducer().reducer)
    val presenter = FullMapPresenter(store)

    // View layer
    override fun createView(context: Context): FullMapView {
        store.subscribe { Anvil.render() }
        return FullMapView(context)
    }

    override fun shouldShowActionBar(): Boolean {
        return false
    }

}
