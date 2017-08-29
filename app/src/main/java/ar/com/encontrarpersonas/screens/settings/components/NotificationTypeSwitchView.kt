package ar.com.encontrarpersonas.screens.settings.components

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
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
class NotificationTypeSwitchView(context: Context,
                                 val w: Int = MATCH,
                                 val h: Int = WRAP,
                                 val description: String) : RenderableView(context) {

    init {
        view()
    }

    override fun view() {
        linearLayout {
            size(w, h)
            orientation(LinearLayout.HORIZONTAL)
            margin(dip(0), dip(8))

            textView {
                size(dip(0), WRAP)
                weight(1f)
                text(description)
                textSize(sip(18f))
                textColor(ContextCompat.getColor(context, R.color.text_primary))
            }

            switchView {
                size(WRAP, WRAP)
            }
        }
    }

}