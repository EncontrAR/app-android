package ar.com.encontrarpersonas.components.lists

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ar.com.encontrarpersonas.data.models.Campaign
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView
import trikita.anvil.recyclerview.Recycler

/**
 * MIT License
 *
 * Copyright (c) 2017 Proyecto Encontrar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */
class RemoteImageGrid(
        context: Context,
        val items: List<Campaign>,
        val onItemSelected: (view: View, gif: Campaign) -> Unit) : RenderableView(context) {

    val GRID_COLUMNS = 2

    init {
        view()
    }

    override fun view() {

        v(RecyclerView::class.java) {
            val recycler = Anvil.currentView<RecyclerView>()
            size(MATCH, MATCH)

            // Layout manager
            recycler.layoutManager = GridLayoutManager(context, GRID_COLUMNS)

            // Fixed height for improved performance
            recycler.setHasFixedSize(true)

            // Simple items list adapter
            recycler.adapter =
                    Recycler.Adapter.simple(items) {
                        viewHolder ->

                        RemoteImageGridItem(context, items[viewHolder.adapterPosition])


                        // Callback to report an campaign being clicked
                        onClick { view ->
                            onItemSelected(view, items[viewHolder.adapterPosition])
                        }
                    }
        }
    }

}