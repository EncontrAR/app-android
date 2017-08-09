package ar.com.encontrar.screens.home

import android.content.Context
import android.graphics.Color
import ar.com.encontrar.screens.home.components.CaseListingHomeComponent
import ar.com.encontrar.screens.home.components.LoadingHomeComponent
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class HomeView(context: Context) : BaseScreenView<HomeScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {
                frameLayout {
                    backgroundColor(Color.BLACK)
                    size(MATCH, MATCH)

                    if (screen.store.state.isFetching) {
                        LoadingHomeComponent(context)
                    } else {
                        CaseListingHomeComponent(context, screen)
                    }

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