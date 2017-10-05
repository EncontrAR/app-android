package ar.com.encontrarpersonas.screens.home.components

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import android.widget.ProgressBar
import ar.com.encontrarpersonas.R
import com.github.ybq.android.spinkit.style.DoubleBounce
import trikita.anvil.Anvil
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
class LoadingHomeComponent(context: Context,
                           @StringRes val stringRes : Int,
                           val w: Int = MATCH,
                           val h: Int = WRAP)
    : RenderableView(context) {
    

    init {
        view()
    }

    override fun view() {
        linearLayout {
            orientation(LinearLayout.VERTICAL)
            size(w, h)
            layoutGravity(CENTER)
            gravity(CENTER_HORIZONTAL)

            progressBar {
                size(dip(128), dip(128))
                val animationDrawable = DoubleBounce()
                animationDrawable.color = ContextCompat.getColor(context, R.color.text_secondary)
                Anvil.currentView<ProgressBar>().indeterminateDrawable = animationDrawable
            }

            textView {
                size(WRAP, WRAP)
                margin(dip(16), dip(32))
                textSize(sip(24f))
                text(stringRes)
                textColor(ContextCompat.getColor(context, R.color.text_secondary))
            }
        }
    }

}