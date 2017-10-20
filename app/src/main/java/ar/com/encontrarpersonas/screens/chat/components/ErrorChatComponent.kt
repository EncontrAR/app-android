package ar.com.encontrarpersonas.screens.chat.components

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import trikita.anvil.BaseDSL.dip
import trikita.anvil.BaseDSL.layoutGravity
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
class ErrorChatComponent(context: Context,
                         @StringRes val stringRes: Int,
                         val onRetryClick: (view: View) -> Unit)
    : RenderableView(context) {


    init {
        view()
    }

    override fun view() {
        linearLayout {
            orientation(LinearLayout.VERTICAL)
            size(MATCH, 0)
            weight(1f)
            layoutGravity(CENTER)

            imageView {
                size(dip(128), dip(128))
                imageResource(R.drawable.ic_warning)
            }

            textView {
                size(MATCH, WRAP)
                margin(dip(16), dip(32))
                textSize(sip(24f))
                gravity(CENTER)
                text(stringRes)
                textColor(ContextCompat.getColor(context, R.color.text_primary))
            }

            button {
                size(WRAP, WRAP)
                margin(0, dip(32))
                padding(dip(16), 0)
                text(R.string.screen_home_retry)
                textColor(ContextCompat.getColor(context, R.color.text_secondary))
                backgroundColor(ContextCompat.getColor(context, R.color.theme_color_2))

                onClick { view ->
                    onRetryClick(view)
                }
            }
        }
    }

}