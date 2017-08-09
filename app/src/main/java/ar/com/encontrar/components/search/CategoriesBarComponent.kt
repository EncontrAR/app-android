package ar.com.encontrar.components.search

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import ar.com.encontrar.R
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL.size
import trikita.anvil.BaseDSL.v
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
class CategoriesBarComponent(context: Context,
                             val w: Int = MATCH,
                             val h: Int = WRAP)
    : RenderableView(context) {

    val categoriesList = listOf("Happy", "Angry", "Crazy", "Awesome")

    init {
        view()
    }

    override fun view() {
        v(RecyclerView::class.java) {
            val recycler = Anvil.currentView<RecyclerView>()
            size(w, h)

            margin(0, dip(8), 0, dip(4))

            // Layout manager
            recycler.layoutManager =
                    LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL,
                            false)

            // Fixed height for improved performance
            recycler.setHasFixedSize(true)

            // Simple gifs list adapter
            recycler.adapter =
                    Recycler.Adapter.simple(categoriesList) {
                        viewHolder ->
                        size(WRAP, WRAP)

                        textView {
                            size(WRAP, WRAP)
                            textSize(sip(18f))
                            margin(0, 0, dip(32), 0)
                            text(categoriesList[viewHolder.adapterPosition])
                            textColor(ContextCompat.getColor(context, R.color.white))
                            singleLine(true)
                            typeface(Typeface.createFromAsset(
                                    context.assets,
                                    "fonts/Asap-Bold.ttf"))
                        }

                    }

            // Snap scrolling
            recycler.onFlingListener = null
            LinearSnapHelper().attachToRecyclerView(recycler)
        }
    }

}