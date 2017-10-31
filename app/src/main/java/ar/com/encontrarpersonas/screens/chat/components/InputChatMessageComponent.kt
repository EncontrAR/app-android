package ar.com.encontrarpersonas.screens.chat.components

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.LinearLayout
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
class InputChatMessageComponent(context: Context,
                                val w: Int = MATCH,
                                val h: Int = WRAP,
                                val text: String = "",
                                val enabled: Boolean = true,
                                val onMessageChange: TextWatcher,
                                val onSendClick: OnClickListener) : RenderableView(context) {

    init {
        view()
    }

    override fun view() {
        linearLayout {
            size(w, h)
            orientation(LinearLayout.HORIZONTAL)
            background(ContextCompat.getDrawable(context, ar.com.encontrarpersonas.R.drawable.shape_send_message_wrapper_background))

            editText {
                size(dip(0), WRAP)
                weight(1f)
                hint(ar.com.encontrarpersonas.R.string.screen_chat_message_input_hint)
                margin(dip(16), dip(0), dip(16), dip(0))
                inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                scaleType(ImageView.ScaleType.CENTER_INSIDE)
                maxLines(3)
                enabled(enabled)
                text(text)

                onTextChanged(onMessageChange)
            }

            imageView {
                size(dip(52), dip(52))
                padding(dip(10))
                imageResource(ar.com.encontrarpersonas.R.drawable.ic_send)
                background(ContextCompat.getDrawable(context, ar.com.encontrarpersonas.R.drawable.shape_send_message_button_background))
                layoutGravity(CENTER)
                enabled(enabled)

                onClick(onSendClick)
            }
        }
    }

}