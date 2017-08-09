package ar.com.encontrar.screens.search

import android.content.Context
import com.brianegan.bansa.BaseStore
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil

class SearchScreen : Screen<SearchView>() {

    //Store
    val store = BaseStore(SearchState(), SearchReducer().reducer)
    val presenter = SearchPresenter(store)

    override fun createView(context: Context): SearchView {
        store.subscribe { Anvil.render() }
        return SearchView(context)
    }

}