package ar.com.encontrarpersonas.components.lists

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrarpersonas.data.models.Campaign
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

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
class RemoteImageGridItem(
        context: Context,
        val item: Campaign) : RenderableView(context) {

    init {
        view()
    }

    override fun view() {
        frameLayout {
            size(MATCH, dip(240))
            padding(dip(2), dip(4))

            RemoteImageComponent(
                    context,
                    MATCH,
                    MATCH,
                    item.imagesUrls?.first())

            linearLayout {
                size(MATCH, WRAP)
                orientation(LinearLayout.VERTICAL)
                backgroundColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.black_60))
                padding(dip(8))
                layoutGravity(BOTTOM)

                textView {
                    size(MATCH, WRAP)
                    text(item.title)
                    textSize(sip(18f))
                    textColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.white))
                }

                textView {
                    size(MATCH, WRAP)
                    text(item.description)
                    textSize(sip(12f))
                    textColor(ContextCompat.getColor(context, ar.com.encontrarpersonas.R.color.white))
                }
            }
        }
    }

}