package ar.com.encontrarpersonas.screens.chat.components

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.models.ChatMessage
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
class MessagesListComponent(context: Context,
                            val messagesList: List<ChatMessage>)
    : RenderableView(context) {


    init {
        view()
    }

    override fun view() {

        // Chat messages recycler view
        v(RecyclerView::class.java) {
            val recycler = Anvil.currentView<RecyclerView>()
            size(MATCH, dip(0))
            weight(1f)
            padding(dip(0), dip(0), dip(0), dip(8))

            // Layout manager
            recycler.layoutManager =
                    LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL,
                            false)

            // Keeps the Linear Layout Manager anchor to the bottom
            (recycler.layoutManager as LinearLayoutManager).stackFromEnd = true

            // Fixed height for improved performance
            recycler.setHasFixedSize(true)

            // Simple items list adapter
            recycler.adapter =
                    Recycler.Adapter.simple(messagesList) { viewHolder ->

                        val chatMessage = messagesList[viewHolder.adapterPosition]

                        linearLayout {
                            size(MATCH, WRAP)
                            orientation(LinearLayout.VERTICAL)

                            // Separator after first message
                            if (viewHolder.adapterPosition > 0) {
                                view {
                                    size(WRAP, dip(16))
                                }
                            }

                            if (chatMessage.isMyMessage()) {
                                // Local message view
                                textView {
                                    size(WRAP, WRAP)
                                    layoutGravity(RIGHT)
                                    text(chatMessage.message)
                                    textColor(ContextCompat.getColor(context, R.color.text_primary))
                                    textSize(sip(16f))
                                    padding(dip(16), dip(20))
                                    background(ContextCompat.getDrawable(context, R.drawable.patch_chat_sent))
                                }
                            } else {
                                // Remote message view
                                textView {
                                    size(WRAP, WRAP)
                                    layoutGravity(LEFT)
                                    text(chatMessage.message)
                                    textColor(ContextCompat.getColor(context, R.color.text_secondary))
                                    textSize(sip(16f))
                                    padding(dip(16), dip(20))
                                    background(ContextCompat.getDrawable(context, R.drawable.patch_chat_received))
                                }
                            }

                        }
                    }

            // Scroll to last position
            //recycler.scrollToPosition(recycler.adapter.itemCount)
        }
    }

}