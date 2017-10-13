package ar.com.encontrarpersonas.screens.home

import android.content.Context
import android.view.Menu
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.activities.MainActivity
import com.brianegan.bansa.BaseStore
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil


class HomeScreen : Screen<HomeView>() {

    //Store
    val store = BaseStore(HomeState(), HomeReducer().reducer)
    val presenter = HomePresenter(store)

    // View layer
    override fun createView(context: Context): HomeView {
        store.subscribe { Anvil.render() }
        return HomeView(context)
    }

    override fun onShow(context: Context?) {
        super.onShow(context)

        // Retrieve campaigns
        if (store.state.campaigns.isEmpty()) presenter.startCampaignsRetrievalProcess()

        // Request location permissions
        (activity as MainActivity).requestLocationPermission()
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_home_title)
    }

    override fun onUpdateMenu(menu: Menu?) {
        menu?.findItem(R.id.main_menu_settings)?.isVisible = true
    }
}
