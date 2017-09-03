package ar.com.encontrarpersonas.components.lists

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import ar.com.encontrarpersonas.data.models.Metadata
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
class RemoteImageList(
        context: Context,
        val items: List<Metadata>,
        val onItemSelected: (view: View, gif: Metadata) -> Unit) : RenderableView(context) {

    init {
        view()
    }

    override fun view() {

        v(RecyclerView::class.java) {
            val recycler = Anvil.currentView<RecyclerView>()
            size(MATCH, MATCH)

            // Layout manager
            recycler.layoutManager =
                    LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL,
                            false)

            // Fixed height for improved performance
            recycler.setHasFixedSize(true)

            // Simple items list adapter
            recycler.adapter =
                    Recycler.Adapter.simple(items) {
                        viewHolder ->

                        RemoteImageComponent(
                                context,
                                MATCH,
                                dip(360),
                                items[viewHolder.adapterPosition].images.scrollItem.webp)

                        // Callback to report a item being clicked
                        onClick { view ->
                            onItemSelected(view, items[viewHolder.adapterPosition])
                        }
                    }

            // Snap scrolling
            recycler.onFlingListener = null
            LinearSnapHelper().attachToRecyclerView(recycler)
        }
    }

}