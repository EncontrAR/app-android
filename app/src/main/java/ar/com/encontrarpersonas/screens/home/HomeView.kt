package ar.com.encontrarpersonas.screens.home

import android.content.Context
import android.support.v4.content.ContextCompat
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.screens.home.components.CaseListingHomeComponent
import ar.com.encontrarpersonas.screens.home.components.ErrorHomeComponent
import ar.com.encontrarpersonas.screens.home.components.LoadingHomeComponent
import ar.com.encontrarpersonas.screens.home.components.NoInternetHomeComponent
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class HomeView(context: Context) : BaseScreenView<HomeScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                frameLayout {
                    backgroundColor(ContextCompat.getColor(context, R.color.theme_color_2))
                    size(MATCH, MATCH)

                    // No internet connection state
                    if (screen.store.state.isNoInternetConnection) {
                        NoInternetHomeComponent(context, R.string.error_network) {
                            screen.presenter.startCampaignsRetrievalProcess()
                        }
                        return@frameLayout
                    }

                    // Error state
                    if (screen.store.state.isOnError) {
                        ErrorHomeComponent(context, R.string.screen_home_error) {
                            screen.presenter.startCampaignsRetrievalProcess()
                        }
                        return@frameLayout
                    }

                    // Registering device state
                    if (screen.store.state.isRegisteringDevice) {
                        LoadingHomeComponent(context, R.string.screen_home_registering_device)
                        return@frameLayout
                    }

                    // Retrieving campaigns state
                    if (screen.store.state.isFetching) {
                        LoadingHomeComponent(context, R.string.screen_home_retrieving_campaigns)
                        return@frameLayout
                    }

                    // Displaying campaigns state
                    CaseListingHomeComponent(context, screen)

                }

            }

            // This fixes an issue with Anvil and RecyclerViews
            // https://github.com/zserge/anvil/issues/95
            override fun onDetachedFromWindow() {
                Anvil.unmount(this, false)
            }
        })
    }
}