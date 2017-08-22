package ar.com.encontrar.screens.detail

import android.content.Context
import ar.com.encontrar.App
import ar.com.encontrar.R
import ar.com.encontrar.models.Metadata
import com.brianegan.bansa.BaseStore
import com.google.android.gms.maps.MapView
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil


class DetailScreen(item: Metadata) : Screen<DetailView>() {

    //Store
    val store = BaseStore(DetailState(item), DetailReducer().reducer)
    val presenter = DetailPresenter(store)
    var mapView : MapView? = null

    // View layer
    override fun createView(context: Context): DetailView {
        store.subscribe { Anvil.render() }
        return DetailView(context)
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_detail_title)
    }

    override fun onShow(context: Context?) {
        super.onShow(context)
        mapView?.onCreate(null)
    }

    override fun onHide(context: Context?) {
        super.onHide(context)
        mapView?.onDestroy()
    }

    override fun onPause(context: Context?) {
        super.onPause(context)
        mapView?.onPause()
    }

    override fun onResume(context: Context?) {
        super.onResume(context)
        mapView?.onResume()
    }

}
