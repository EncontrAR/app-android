package ar.com.encontrar.screens.detail

import android.content.Context
import ar.com.encontrar.App
import ar.com.encontrar.R
import ar.com.encontrar.models.Metadata
import ar.com.encontrar.utils.AbstractMapScreen
import com.brianegan.bansa.BaseStore
import trikita.anvil.Anvil


class DetailScreen(item: Metadata) : AbstractMapScreen<DetailScreen>() {

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
