package ar.com.encontrarpersonas.screens.detail

import android.content.Context
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.utils.AbstractMapScreen
import com.brianegan.bansa.BaseStore
import trikita.anvil.Anvil


class DetailScreen(item: Campaign) : AbstractMapScreen<DetailScreen>() {

    //Store
    val store = BaseStore(DetailState(item), DetailReducer().reducer)
    val presenter = DetailPresenter(store)

    // View layer
    override fun createView(context: Context): DetailView {
        store.subscribe { Anvil.render() }
        return DetailView(context)
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_detail_title)
    }

}
