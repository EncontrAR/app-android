package ar.com.encontrar.screens.search

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrar.R
import ar.com.encontrar.components.lists.RemoteImageList
import ar.com.encontrar.components.search.CategoriesBarComponent
import ar.com.encontrar.components.search.SearchBarComponent
import ar.com.encontrar.screens.detail.DetailScreen
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.transitions.NoAnimationTransition
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class SearchView(context: Context) : BaseScreenView<SearchScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {
                linearLayout {
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)

                    linearLayout {
                        size(MATCH, WRAP)
                        orientation(LinearLayout.VERTICAL)
                        backgroundColor(ContextCompat.getColor(context, R.color.theme_color_3))
                        padding(dip(16), dip(36), dip(16), dip(8))

                        SearchBarComponent(
                                context,
                                isFetching = screen.store.state.isFetching,
                                searchHistory = screen.store.state.searchHistory) {
                            query ->
                            screen.store.dispatch(SearchReducer.NEW_SEARCH(query))
                            screen.presenter.searchGifs(query)
                        }

                        CategoriesBarComponent(context)

                    }

                    RemoteImageList(context, screen.store.state.gifs) { _, gif ->
                        screen.navigator.
                                overrideTransition(NoAnimationTransition())
                                .show(DetailScreen(gif))
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