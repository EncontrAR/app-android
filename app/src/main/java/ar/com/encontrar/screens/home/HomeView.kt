package ar.com.encontrar.screens.home

import android.content.Context
import android.graphics.Color
import android.support.design.widget.FloatingActionButton
import ar.com.encontrar.R
import ar.com.encontrar.components.gif.RemoteImageRecycler
import ar.com.encontrar.screens.detail.DetailScreen
import ar.com.encontrar.screens.search.SearchScreen
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.transitions.NoAnimationTransition
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

                    } else {
                        RemoteImageRecycler(context, screen.store.state.gifs) { _, gif ->
                            screen.navigator.
                                    overrideTransition(NoAnimationTransition())
                                    .show(DetailScreen(gif))
                        }
                    }

                    v(FloatingActionButton::class.java) {
                        size(WRAP, WRAP)
                        margin(0, 0, dip(16), dip(64))
                        layoutGravity(BOTTOM or END)
                        imageResource(R.drawable.ic_search)

                        onClick {
                            screen.navigator.goTo(SearchScreen())
                        }
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